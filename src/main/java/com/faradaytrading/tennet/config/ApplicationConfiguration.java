package com.faradaytrading.tennet.config;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

@StaticInitSafe
@ConfigMapping(namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface ApplicationConfiguration extends SoapApplicationConfiguration {
    
    @WithName("faraday.ean")
    @WithDefault("8720844058549")
    String faradayEAN();	
}
