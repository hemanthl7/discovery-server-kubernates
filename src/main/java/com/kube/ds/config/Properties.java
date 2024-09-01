package com.kube.ds.config;

import com.kube.ds.model.WhereAmI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(Properties.PREFIX)
public record Properties (
        @DefaultValue(PHANTOM_HOST_PATTERN) String phantomHostPattern,
        @DefaultValue(BOSUN_HOST_PATTERN) String bosunHostPattern,
        WhereAmI whereAmI){

    public static final String PREFIX = "application.discovery";
    public static final String PHANTOM_HOST_PATTERN = "spring.cloud.kubernetes.discovery";
    public static final String BOSUN_HOST_PATTERN = "spring.cloud.kubernetes.discovery";


    @ConstructorBinding
    public Properties {
    }
}
