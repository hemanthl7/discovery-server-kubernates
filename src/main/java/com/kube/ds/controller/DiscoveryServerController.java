package com.kube.ds.controller;

import com.kube.ds.config.KubernetesReactiveDiscoveryClient;
import com.kube.ds.config.Properties;
import com.kube.ds.model.WhereAmI;
import com.kube.ds.service.LocalDiscoveryService;
import com.kube.ds.service.RemoteDiscoveryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.kubernetes.client.discovery.reactive.KubernetesInformerReactiveDiscoveryClient;
import org.springframework.cloud.kubernetes.commons.discovery.DefaultKubernetesServiceInstance;
import org.springframework.cloud.kubernetes.commons.discovery.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@RestController
public class DiscoveryServerController {

	private final LocalDiscoveryService localDiscoveryService;
	private final RemoteDiscoveryService remoteDiscoveryService;
	private final Properties properties;

    public DiscoveryServerController(LocalDiscoveryService localDiscoveryService, RemoteDiscoveryService remoteDiscoveryService, Properties properties) {
        this.localDiscoveryService = localDiscoveryService;
        this.remoteDiscoveryService = remoteDiscoveryService;
        this.properties = properties;
    }


    @GetMapping("/service-instances/{name}")
	public Flux<ServiceInstance> getServiceInstancesByName(@PathVariable String name) {
		return Flux.merge(
				Flux.defer(()->localDiscoveryService.getService(name)),
				Flux.defer(()->remoteDiscoveryService.getService(name))
		);
	}

	@GetMapping("/where-am-i")
	public Mono<WhereAmI> whereAmI() {
		return Mono.just(properties.whereAmI());
	}

	@GetMapping("/apps")
	public Flux<Service> apps() {
		return localDiscoveryService.apps();
	}

	@GetMapping("/apps/{name}")
	public Flux<ServiceInstance> appInstances(@PathVariable String name) {
		return localDiscoveryService.appInstances(name);
	}

	@GetMapping("/apps/{name}/{instanceId}")
	public Mono<ServiceInstance> appInstance(@PathVariable String name, @PathVariable String instanceId) {
		return localDiscoveryService.appInstance(name, instanceId);
	}

	@GetMapping("/appsRemote")
	public Flux<Service> appsRemoteIndirect() {
		return remoteDiscoveryService.apps();
	}

	@GetMapping("/appsRemote/{name}")
	public Flux<ServiceInstance> appRemoteInstances(@PathVariable String name) {
		return remoteDiscoveryService.appInstances(name);
	}

	@GetMapping("/appsRemote/{name}/{instanceId}")
	public Mono<ServiceInstance> appRemoteInstance(@PathVariable String name, @PathVariable String instanceId) {
		return remoteDiscoveryService.appInstance(name, instanceId);
	}

}
