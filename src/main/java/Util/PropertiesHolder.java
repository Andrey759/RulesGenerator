package Util;

import enums.RT;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesHolder {

    private static String propertiesName = "cfg.properties";
    private final Properties cfg;

    private PropertiesHolder(String...args) {
        cfg = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesName)) {
            cfg.load(inputStream);
            List<String> argList = Arrays.asList(args);
            if(argList.contains("rs") || argList.contains("nb") || argList.contains("ch")) {
                cfg.setProperty("realtyType.rs", argList.remove("rs") ? "true" : "false");
                cfg.setProperty("realtyType.nb", argList.remove("nb") ? "true" : "false");
                cfg.setProperty("realtyType.ch", argList.remove("ch") ? "true" : "false");
            }
            if(argList.size() <= 2) {
                if(argList.size() == 1) {
                    cfg.setProperty("name.site", argList.get(0));
                } else if(argList.size() == 2) {
                    cfg.setProperty("name.module", argList.get(0));
                    cfg.setProperty("name.site", argList.get(1));
                }
            } else {
                throw new RuntimeException("Too much arguments");
            }
            if(StringUtils.isEmpty(cfg.getProperty("name.module"))) {
                cfg.setProperty("name.module",
                        cfg.getProperty("name.site").replaceAll("-", "").split("\\.")[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't find properties file: " + propertiesName);
        }
    }

    public static PropertiesHolder prepare(String...args) {
        return new PropertiesHolder(args);
    }

    public String getSiteName() {
        return cfg.getProperty("name.site");
    }

    public String getModuleName() {
        return cfg.getProperty("name.module");
    }

    public boolean isRealtyTypeNeeded(RT realtyType) {
        return "true".equals(cfg.getProperty("realtyType." + realtyType.name()));
    }

    public String getPathToExpress() {
        return cfg.getProperty("path.toExpress");
    }

    public String getPathToProfiles() {
        return cfg.getProperty("path.toProfiles");
    }

}
