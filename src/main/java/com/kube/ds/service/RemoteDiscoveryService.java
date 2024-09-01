package com.kube.ds.service;

import com.kube.ds.config.KubernetesReactiveDiscoveryClient;
import com.kube.ds.config.Properties;
import com.kube.ds.model.InstanceType;
import com.kube.ds.utils.ServiceInstancePropertyResolver;
import org.springframework.cloud.kubernetes.client.discovery.reactive.KubernetesInformerReactiveDiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class RemoteDiscoveryService extends DiscoveryService{

    public RemoteDiscoveryService(KubernetesReactiveDiscoveryClient remoteReactiveDiscoveryClient,
                                  Properties properties, ServiceInstancePropertyResolver serviceInstancePropertyResolver) {
        super(remoteReactiveDiscoveryClient, serviceInstancePropertyResolver, properties);
    }

    @Override
    protected InstanceType getInstanceType() {
        return InstanceType.REMOTE;
    }
}
