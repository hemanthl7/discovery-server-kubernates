package com.kube.ds.model;

import org.springframework.cloud.kubernetes.commons.discovery.KubernetesServiceInstance;

public class ServiceInstanceContext {
    private KubernetesServiceInstance serviceInstance;
    private String serviceId;
    private String cluster;
    private String host;
    private boolean secure;
    private int port=-1;
    private String weight;
    private InstanceType instanceType;

    public KubernetesServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(KubernetesServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public InstanceType getInstanceType() {
        return instanceType;
    }
    public void setInstanceType(InstanceType instanceType) {
        this.instanceType = instanceType;
    }
}
