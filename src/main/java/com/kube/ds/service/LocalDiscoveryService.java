package com.kube.ds.service;

import com.kube.ds.config.Properties;
import com.kube.ds.model.InstanceType;
import com.kube.ds.utils.ServiceInstancePropertyResolver;
import org.springframework.cloud.kubernetes.client.discovery.reactive.KubernetesInformerReactiveDiscoveryClient;
import org.springframework.stereotype.Service;


@Service
public class LocalDiscoveryService extends DiscoveryService{

    public LocalDiscoveryService(KubernetesInformerReactiveDiscoveryClient localReactiveDiscoveryClient,
                                 Properties properties, ServiceInstancePropertyResolver serviceInstancePropertyResolver) {
        super(localReactiveDiscoveryClient, serviceInstancePropertyResolver, properties);
    }

    @Override
    public InstanceType getInstanceType() {
        return InstanceType.LOCAL;
    }
}
