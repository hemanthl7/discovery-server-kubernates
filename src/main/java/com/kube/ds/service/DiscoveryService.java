package com.kube.ds.service;

import com.kube.ds.config.Properties;
import com.kube.ds.model.InstanceType;
import com.kube.ds.model.ServiceInstanceContext;
import com.kube.ds.utils.ServiceInstancePropertyResolver;
import com.kube.ds.utils.ServiceInstanceTransformer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.kubernetes.commons.discovery.DefaultKubernetesServiceInstance;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesServiceInstance;
import org.springframework.cloud.kubernetes.commons.discovery.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DiscoveryService {

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;
    private final ServiceInstancePropertyResolver serviceInstancePropertyResolver;
    private final Properties properties;

    public DiscoveryService(ReactiveDiscoveryClient reactiveDiscoveryClient, ServiceInstancePropertyResolver serviceInstancePropertyResolver, Properties properties1) {
        this.reactiveDiscoveryClient = reactiveDiscoveryClient;
        this.serviceInstancePropertyResolver = serviceInstancePropertyResolver;
        this.properties = properties1;
    }

    public Flux<Service> apps() {
        return reactiveDiscoveryClient.getServices()
                .flatMap(service -> reactiveDiscoveryClient.getInstances(service).collectList()
                        .flatMap(serviceInstances -> Mono.just(new Service(service,
                                serviceInstances.stream().map(x -> (DefaultKubernetesServiceInstance) x).toList()))));
    }

    public Flux<ServiceInstance> appInstances(String name) {
        return reactiveDiscoveryClient.getInstances(name);
    }

    public Mono<ServiceInstance> appInstance(String name, String instanceId) {
        return reactiveDiscoveryClient.getInstances(name)
                .filter(serviceInstance -> serviceInstance.getInstanceId().equals(instanceId)).singleOrEmpty();
    }


    /**
     * should override in Child to know if Service Instance is Local or Remote
     * @return Local or Remote
     */
    protected InstanceType getInstanceType() {
        return null;
    }

    public Mono<ServiceInstance> getService(String name) {
        var context = new ServiceInstanceContext();
        setServiceId(context,name);
        context.setInstanceType(getInstanceType());


        return Mono.just(context)
                .doOnNext(this::setHost)
                .doOnNext(this::setCluster)
                .doOnNext(this::setSecure)
                .flatMap(this::setWeight)
                .flatMap(this::getServiceInstance);
    }

    private Mono<ServiceInstance> getServiceInstance(ServiceInstanceContext context) {
       return reactiveDiscoveryClient.getInstances(context.getServiceId())
                .next()
                .map(s-> transform(s, context));
    }

    private ServiceInstance transform(ServiceInstance serviceInstance, ServiceInstanceContext context) {
        context.setServiceInstance((KubernetesServiceInstance)serviceInstance);
        return ServiceInstanceTransformer.transform(context);
    }

    public void setHost(ServiceInstanceContext context) {
        var instanceType = getInstanceType();
        var whereAmI = properties.whereAmI();
        var host = serviceInstancePropertyResolver.resolveHost(whereAmI, context.getServiceId(), instanceType);
        context.setHost(host);
    }

    public void setCluster(ServiceInstanceContext context) {
        var instanceType = getInstanceType();
        var whereAmI = properties.whereAmI();
        var cluster = serviceInstancePropertyResolver.resolveCluster(whereAmI, instanceType);
        context.setCluster(cluster);
    }

    public void setSecure(ServiceInstanceContext context) {
        var instanceType = getInstanceType();
        var whereAmI = properties.whereAmI();
        var secure = serviceInstancePropertyResolver.isSecure(whereAmI, instanceType);
        context.setSecure(secure);
    }

    public void setServiceId(ServiceInstanceContext context, String serviceId) {
        context.setServiceId(serviceId);
    }

    public Mono<ServiceInstanceContext> setWeight(ServiceInstanceContext context) {
        return getWeight(context.getServiceId())
                .map(weight-> {
                    context.setWeight(weight);
                    return context;
                });
    }

    public Mono<String> getWeight(String serviceId) {
        return Mono.just("1");
    }
}
