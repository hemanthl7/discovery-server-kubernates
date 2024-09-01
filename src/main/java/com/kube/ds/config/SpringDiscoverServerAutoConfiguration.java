package com.kube.ds.config;

import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.log.LogAccessor;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryConstants.CATALOG_WATCHER_DEFAULT_DELAY;
import static org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryConstants.CATALOG_WATCH_PROPERTY_NAME;

@Configuration
@EnableConfigurationProperties({ Properties.class})
public class SpringDiscoverServerAutoConfiguration {

    private static final LogAccessor LOG = new LogAccessor(LogFactory.getLog(SpringDiscoverServerAutoConfiguration.class));

    @Bean
    @ConditionalOnMissingBean
    KubernetesReactiveDiscoveryClient kubernetesReactiveDiscoveryClient(WebClient.Builder webClientBuilder,
                                                                        KubernetesDiscoveryProperties properties) {
        LOG.info("server url:"+properties.discoveryServerUrl());
        LOG.info("namespace:"+properties.namespaces());
        return new KubernetesReactiveDiscoveryClient(webClientBuilder, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    KubernetesCatalogWatch remoteKubernetesCatalogWatch(RestTemplateBuilder builder, KubernetesDiscoveryProperties properties,
                                                  Environment environment) {

        String watchDelay = environment.getProperty(CATALOG_WATCH_PROPERTY_NAME);
        if (watchDelay != null) {
            LOG.debug("using delay : " + watchDelay);
        }
        else {
            LOG.debug("using default watch delay : " + CATALOG_WATCHER_DEFAULT_DELAY);
        }

        return new KubernetesCatalogWatch(builder, properties);
    }

}
