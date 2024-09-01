package com.kube.ds.config;

import org.springframework.cloud.kubernetes.discovery.DiscoveryServerUrlInvalidException;
import reactor.core.publisher.Flux;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.kubernetes.commons.discovery.DefaultKubernetesServiceInstance;
import org.springframework.cloud.kubernetes.commons.discovery.KubernetesDiscoveryProperties;
import org.springframework.cloud.kubernetes.commons.discovery.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;


public class KubernetesReactiveDiscoveryClient implements ReactiveDiscoveryClient {

	private final WebClient webClient;

	KubernetesReactiveDiscoveryClient(WebClient.Builder webClientBuilder, KubernetesDiscoveryProperties properties) {
		if (!StringUtils.hasText(properties.discoveryServerUrl())) {
			throw new DiscoveryServerUrlInvalidException();
		}
		webClient = webClientBuilder.baseUrl(properties.discoveryServerUrl()).build();
	}

	@Override
	public String description() {
		return "Reactive Kubernetes Discovery Client";
	}

	@Override
	@Cacheable("serviceinstances")
	public Flux<ServiceInstance> getInstances(String serviceId) {
		return webClient.get().uri("/apps/" + serviceId)
				.exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(DefaultKubernetesServiceInstance.class));
	}

	@Override
	@Cacheable("services")
	public Flux<String> getServices() {
		return webClient.get().uri("/apps")
				.exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Service.class).map(Service::name));
	}

}
