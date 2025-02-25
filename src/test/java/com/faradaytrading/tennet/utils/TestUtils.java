package com.faradaytrading.tennet.utils;

import com.faradaytrading.tennet.config.ApplicationConfiguration;
import io.smallrye.config.PropertiesConfigSourceProvider;
import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtils {

    public static String readFile(String path) throws IOException {
        return Files.readString(java.nio.file.Path.of("src/test/resources/" + path));
    }

    public static ApplicationConfiguration readConfig() {
        List<ConfigSource> propertiesFileResource = PropertiesConfigSourceProvider.propertiesSources("application.properties", TestUtils.class.getClassLoader());

        SmallRyeConfig mpConfig = new SmallRyeConfigBuilder()
                .withSources(propertiesFileResource)
                .withMapping(ApplicationConfiguration.class)
                .withValidateUnknown(false) // Must be false to prevent issues with properties that are not in ApplicationConfiguration
                .build();

        return mpConfig.getConfigMapping(ApplicationConfiguration.class);
    }

    public static String normalizeXml(String xml){
        return StringUtils.normalizeSpace(StringUtils.deleteWhitespace(xml));
    }

    public static String replaceCipherValue(String expectedXml, String actualXml) {
        String cipherValue = getCipherValue(actualXml);
        return expectedXml.replaceAll("(?s)(<xenc:CipherValue>)(.*?)(</xenc:CipherValue>)", "$1" + cipherValue + "$3");
    }


    public static String getCipherValue(String xml) {
        Pattern pattern = Pattern.compile("<xenc:CipherValue>(.*?)</xenc:CipherValue>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(xml);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    }
