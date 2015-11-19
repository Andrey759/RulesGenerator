import Util.Function;
import Util.MapWrapper;
import Util.PropertiesHolder;
import enums.ChaletFields;
import enums.FlatFields;
import enums.NewbuildingFields;
import enums.RT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// todo выбирать swith case через split и добавлять значение, которые было в последнем case
public class RulesGenerator {

    public static void main(String...args) throws IOException {
        PropertiesHolder prop = PropertiesHolder.prepare(args);
        new RulesGenerator(prop).generate(prop);
    }




    private static String pathInExpressToParser = "Express/Hydra/%1$s/src/main/java/com/srg/hydra/%1$sParser.java";
    private static String pathInExpressToRules = "Express/com.srg.profile.builder/src/main/resources/rules/";

    private Path parser;
    private Path rulesRS;
    private Path rulesNB;
    private Path rulesCH;

    private MapWrapper fields = new MapWrapper(new LinkedHashMap<>());
    private Map<String, String> fieldsEnd = new TreeMap<>();
    private Map<String, String> ignoreFields = new TreeMap<>();

    public RulesGenerator(PropertiesHolder prop) {
        parser = Paths.get(prop.getPathToExpress() + String.format(pathInExpressToParser, prop.getModuleName()));
        rulesRS = Paths.get(prop.getPathToExpress() + pathInExpressToRules + prop.getModuleName() + ".json");
        rulesNB = Paths.get(prop.getPathToExpress() + pathInExpressToRules + "nb-" + prop.getModuleName() + ".json");
        rulesCH = Paths.get(prop.getPathToExpress() + pathInExpressToRules + "ch-" + prop.getModuleName() + ".json");
        begin.put("sampleUrl", "http://" + prop.getModuleName());
        begin.put("sampleTitle",  "Продажа недвижимости на " + prop.getModuleName());
        begin.put("id", DEFAULT_TEXT);
        begin.put("advertNum", DEFAULT_TEXT);
        begin.put("published", "NEEDS_TO_WRITE");
        begin.put("noticed", DEFAULT_TEXT);
        begin.put("downloaded", DEFAULT_TEXT);
        begin.put("realtyType", DEFAULT_TEXT);
        begin.put("marketType", DEFAULT_TEXT);
        begin.put("region", DEFAULT_TEXT);
        begin.put("currency", DEFAULT_TEXT);
    }

    private Function renameFields = current -> {
        if(current.containsKey("phone")) {
            current.put("contacts", current.remove("phone"));
        }
        if(current.containsKey("salesPerson")) {
            current.put("contacts", current.remove("salesPerson"));
        }
        if(current.containsKey("salesCompany")) {
            current.put("contacts", current.remove("salesCompany"));
        }
        return current;
    };

    private static Map<String, String> FIELDS_WITH_DEFAULT_VALUE = new LinkedHashMap<String, String>() {{
        put("latitude", "в html-коде");
        put("longitude", "в html-коде");
        put("mapType", "в html-коде");
    }};

    private Function setDefaultValues = current -> {
        FIELDS_WITH_DEFAULT_VALUE.keySet().stream().filter(current::containsKey).forEach(field -> {
            current.put(field, FIELDS_WITH_DEFAULT_VALUE.get(field));
        });
        return current;
    };

    private static List<String> NOT_STRING_FIELDS = Arrays.asList("lastUpdate", "published","realtyType",
            "price", "pricePerMetr", "currency", "latitude", "longitude", "mapType", "chute", "lift", "intercom",
            "floor", "numberOfLevelsInFlat", "numberOfRooms", "total", "living", "kitchen", "bathroom",
            "bathroomOrig", "balcony", "balconyOrig", "phoneExists", "height", "walls", "wallsOrig");

    private String separator(String key) {
        return NOT_STRING_FIELDS.contains(key) ? " | " : " & ";
    }

    // Wrapper
    private void put(String key, String value) {
        if(!key.contains("Orig")) {
            putInMap(key, value);
            if(key.equals("bathroom") || key.equals("balcony") || key.contains("walls")) {
                putInMap(key + "Orig", value);
            }
        }
    }

    private void putInMap(String key, String value) {
        Map<String, String> map = key.contains("ignor") ? ignoreFields :
                key.equals("latitude") || key.equals("longitude") || key.equals("text") ? fieldsEnd : fields;
        key = key.trim();
        if(!value.startsWith("{comment")) {
            value = value.replaceAll(":", "");
        }
        value = value.trim();
        if(map.containsKey(key)) {
            // If field doesn't contains this value
            if(!Arrays.asList(map.get(key).split(separator(key))).contains(value)) {
                map.put(key, map.get(key) + separator(key) + value);
            }
        } else {
            map.put(key, value);
        }
    }

    private static String DEFAULT_TEXT = "{comment:\"определяется в процессе загрузки\"}";
    private Map<String, String> begin = new LinkedHashMap<>();

    private Function addFields = current -> {
        current.addAllToBegin(begin);
        current.addAllToEnd(fieldsEnd);
        current.put("url", DEFAULT_TEXT);
        current.addAllToEnd(ignoreFields);
        return current;
    };

    private static Pattern PUT_PATTERN = Pattern.compile("put\\(\"(.+?)\",\\s*Flat\\.FIELD\\.(.+?)\\);");
    private static Pattern SET_PATTERN = Pattern.compile("flat\\.set(.+?)\\((?:doc.select\\(\"(.+?)\"\\))?");
    private static Pattern PARSE_BLOCK_PATTERN = Pattern.compile("parse(?:Block|Element)\\(Flat\\.FIELD\\.(.+?),");
    private static Pattern FLAT_FIELD_PATTERN = Pattern.compile("Flat\\.FIELD\\.([\\w\\d]+)");

    private void parseCodeBlock(String code, String key) {
        Matcher matcher = SET_PATTERN.matcher(code);
        while(matcher.find()) {
            put(toLowerChar(matcher.group(1)), matcher.group(2) != null ? matcher.group(2) : key);
        }
        matcher = PARSE_BLOCK_PATTERN.matcher(code);
        while(matcher.find()) {
            put(toLowerChar(matcher.group(1)), key);
        }
    }

    private void parseCaseBlock(String code, String key) {
        Matcher matcher = SET_PATTERN.matcher(code);
        while(matcher.find()) {
            put(toLowerChar(matcher.group(1)), matcher.group(2) != null ? matcher.group(2) : key);
        }
        matcher = FLAT_FIELD_PATTERN.matcher(code);
        while(matcher.find()) {
            put(toLowerChar(matcher.group(1)), key);
        }
    }

    private String[] skipFirst(String[] array) {
        return Arrays.copyOfRange(array, 1, array.length);
    }

    private void parseSwitchCaseCodeBlock(String switchCase) {
        switchCase = switchCase.split("default:")[0];
        String[] parts = switchCase.split("\\s+case\\s+\"");
        for(String block : skipFirst(parts)) {
            block = block.split("\\s+break;\\s+")[0];
            String key = block.split("\":")[0].replaceAll(":", "").trim();
            parseCaseBlock(block, key);
        }
    }

    private int indexOfCloseParentheses(String code, char open, char close) {
        char[] chars = code.toCharArray();
        int countOpened = 0;
        for(int i = 0; i < chars.length; i++) {
            if(chars[i] == open) countOpened++;
            if(chars[i] == close) {
                countOpened--;
                if(countOpened <= 0) {
                    return i;
                }
            }
        }
        return chars.length - 1;
    }

    private void parseSwitchCases(String code) {
        String[] parts = code.split("switch\\(");
        parseCodeBlock(parts[0], "NEEDS_TO_WRITE");
        for(String switchCase : skipFirst(parts)) {
            switchCase = switchCase.substring(switchCase.indexOf("{"));
            int lastIndex = indexOfCloseParentheses(switchCase, '{', '}');
            String afterSwitchCase = switchCase.substring(lastIndex + 1);
            switchCase = switchCase.substring(0, lastIndex + 1);
            parseSwitchCaseCodeBlock(switchCase);
            parseCodeBlock(afterSwitchCase, "NEEDS_TO_WRITE");
        }
    }

    public void generate(PropertiesHolder prop) throws IOException {
        String code = new String(Files.readAllBytes(parser), "UTF-8");

        Matcher matcher = PUT_PATTERN.matcher(code);
        while(matcher.find()) {
            put(matcher.group(2), matcher.group(1));
        }

        parseSwitchCases(code);

        matcher = FLAT_FIELD_PATTERN.matcher(code);
        while(matcher.find()) {
            String key = toLowerChar(matcher.group(1));
            if(!fields.containsKey(key) &&
                    !fieldsEnd.containsKey(key) &&
                    !ignoreFields.containsKey(key)) {
                put(toLowerChar(matcher.group(1)), "NEEDS_TO_WRITE");
            }
        }

        if(code.contains("parseMapBloc(")) {
            put("latitude", "в html-коде");
            put("longitude", "в html-коде");
            put("mapType", "в html-коде");
        }

        if(fields.containsKey("whenWillBeBuild")) {
            put("howMuchIsBuilt", "{comment:\"'Срок сдачи' содержит 'сдан'\"}");
        }

        fields.execute(renameFields);

        if(prop.isRealtyTypeNeeded(RT.rs)) {
            begin.put("invalidFlat", "{comment:\"'Заголовок' содержит 'новостройк'\"}");
            Files.write(rulesRS, fields.filter(e -> FlatFields.contains(e.getKey())).execute(addFields).execute(setDefaultValues).toJson().getBytes());
            System.out.println("Resale rules is ready:");
            System.out.println(rulesRS);
        }

        if(prop.isRealtyTypeNeeded(RT.nb)) {
            begin.put("invalidFlat", "{comment:\"'Заголовок' не содержит 'новостройк'\"}");
            Files.write(rulesNB, fields.filter(e -> NewbuildingFields.contains(e.getKey())).execute(addFields).execute(setDefaultValues).toJson().getBytes());
            System.out.println("Newbuilding rules is ready:");
            System.out.println(rulesNB);
        }

        if(prop.isRealtyTypeNeeded(RT.ch)) {
            begin.remove("invalidFlat");
            Files.write(rulesCH, fields.filter(e -> ChaletFields.contains(e.getKey())).execute(addFields).execute(setDefaultValues).toJson().getBytes());
            System.out.println("Chalet rules is ready:");
            System.out.println(rulesCH);
        }

    }

    private static String toLowerChar(String text) {
        return text.toLowerCase().charAt(0) + text.substring(1);
    }

}
