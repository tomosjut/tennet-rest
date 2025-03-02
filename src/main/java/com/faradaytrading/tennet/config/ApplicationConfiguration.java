package com.faradaytrading.tennet.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import java.util.List;

@StaticInitSafe
@ConfigMapping(namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ApplicationConfiguration extends SoapApplicationConfiguration {
    
    @WithName("faraday.ean")
    @WithDefault("8720844058549")
    String faradayEAN();

    @WithName("knownEANs")
    List<String> knownEans();

    @WithName("fileArchiveBaseDir")
    String fileArchiveBaseDir();
}
