package com.kube.ds.utils;

import com.kube.ds.model.ServiceInstanceContext;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import java.util.Map;

public class ServiceInstanceTransformer {

    public static final String CLUSTER = "CLUSTER";
    public static final String WEIGHT = "WEIGHT";
    public static final String INSTANCE_TYPE = "INSTANCE";


    public static ServiceInstance transform(ServiceInstanceContext context) {
        var instance = context.getServiceInstance();

        var copyInstance = new DefaultServiceInstance();
        Map<String, String> metaData = copyInstance.getMetadata();
        metaData.put(CLUSTER, context.getCluster());
        metaData.put(WEIGHT, context.getWeight());
        metaData.put(INSTANCE_TYPE, context.getInstanceType().name());

        copyInstance.setInstanceId(context.getCluster()+"-"+context.getServiceId());
        copyInstance.setServiceId(instance.getServiceId());
        copyInstance.setHost(context.getHost());
        copyInstance.setPort(context.getPort());
        copyInstance.setSecure(context.isSecure());
        return copyInstance;
    }
}
