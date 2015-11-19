import Util.MistakeMap;
import Util.PropertiesHolder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import enums.ChaletFields;
import enums.FlatFields;
import enums.RT;
import org.jsoup.Jsoup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Corrector {

    public static void main(String...args) throws IOException {
        PropertiesHolder prop = PropertiesHolder.prepare(args);
        new Corrector(prop).generate(prop);
    }



    private static String pathInExpressToRules = "Express/com.srg.profile.builder/src/main/resources/rules/";

    private String siteName;
    private Path profileRS;
    private Path profileNB;
    private Path profileCH;
    private Path rulesRS;
    private Path rulesNB;
    private Path rulesCH;

    public Corrector(PropertiesHolder prop) {
        siteName = prop.getSiteName();
        rulesRS = Paths.get(prop.getPathToExpress() + pathInExpressToRules + prop.getModuleName() + ".json");
        rulesNB = Paths.get(prop.getPathToExpress() + pathInExpressToRules + "nb-" + prop.getModuleName() + ".json");
        rulesCH = Paths.get(prop.getPathToExpress() + pathInExpressToRules + "ch-" + prop.getModuleName() + ".json");
        profileRS = Paths.get(prop.getPathToProfiles() + "profiles/resale/" + siteName + "/index.html");
        profileNB = Paths.get(prop.getPathToProfiles() + "profiles/newbuilding/" + siteName + "/index.html");
        profileCH = Paths.get(prop.getPathToProfiles() + "profiles/chalet/" + siteName + "/index.html");
    }

    private Map<String, String> loadXml(Path path) throws IOException {
        return ((DBObject) JSON.parse(new String(Files.readAllBytes(path), "UTF-8"))).toMap();
    }

    private Collection<String> loadRules(Path path) throws IOException {
        Collection<String> result = loadXml(path).keySet();
        result.remove("sampleUrl");
        result.remove("sampleTitle");
        return result;
    }

    private Collection<String> loadProfiles(Path path) throws IOException {
        return Jsoup.parse(new String(Files.readAllBytes(path), "UTF-8"), siteName)
                .select("tr")
                .subList(5, 93)
                .stream()
                .filter(e -> !"-".equals(e.select("td").last().text()))
                .map(e -> e.select("td a").text())
                .collect(Collectors.toList());
    }

    private void findAllMistakes(Collection<String> rules, Collection<String> profiles, MistakeMap mistakes, RT rt) {
        Collection<String> profilesClone = new LinkedHashSet<>(profiles);
        profilesClone.removeAll(rules);
        mistakes.putAllToAdd(profilesClone, rt);

        Collection<String> rulesClone = new LinkedHashSet<>(rules);
        rulesClone.removeAll(profiles);
        mistakes.putAllToRemove(rulesClone, rt);
    }

    public MistakeMap generate(PropertiesHolder prop) throws IOException {
        MistakeMap mistakes = new MistakeMap();
        Collection<String> rules;
        Collection<String> profiles;

        if(prop.isRealtyTypeNeeded(RT.rs) && Files.exists(profileRS)) {
            if(Files.notExists(rulesRS)) {
                throw new FileNotFoundException("Файл " + rulesRS + " не найден.");
            }
            rules = loadRules(rulesRS);
            profiles = loadProfiles(profileRS).stream()
                    .map(e -> FlatFields.findByText(e).name())
                    .collect(Collectors.toList());
            findAllMistakes(rules, profiles, mistakes, RT.rs);
        }

        if(prop.isRealtyTypeNeeded(RT.nb) && Files.exists(profileNB)) {
            if(Files.notExists(rulesNB)) {
                throw new FileNotFoundException("Файл " + rulesNB + " не найден.");
            }
            rules = loadRules(rulesNB);
            profiles = loadProfiles(profileNB).stream()
                    .map(e -> FlatFields.findByText(e).name())
                    .collect(Collectors.toList());
            findAllMistakes(rules, profiles, mistakes, RT.nb);
        }

        if(prop.isRealtyTypeNeeded(RT.ch) && Files.exists(profileCH)) {
            if(Files.notExists(rulesCH)) {
                throw new FileNotFoundException("Файл " + rulesCH + " не найден.");
            }
            rules = loadRules(rulesCH);
            profiles = loadProfiles(profileCH).stream()
                    .map(e -> ChaletFields.findByText(e).name())
                    .collect(Collectors.toList());
            findAllMistakes(rules, profiles, mistakes, RT.ch);
        }

        // todo show error if rules contains "NEEDS_TO_WRITE"
        System.out.println(mistakes.size() > 0 ? mistakes : "Всё ок ;)");
        return mistakes;
    }

}
