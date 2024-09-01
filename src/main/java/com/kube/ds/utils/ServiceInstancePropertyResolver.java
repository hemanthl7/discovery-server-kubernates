package com.kube.ds.utils;

import com.kube.ds.config.Properties;
import com.kube.ds.model.InstanceType;
import com.kube.ds.model.WhereAmI;
import org.springframework.stereotype.Component;

@Component
public class ServiceInstancePropertyResolver {
    private final Properties properties;

    private static final String SERVICE_NAME = "SERVICE_NAME";

    public ServiceInstancePropertyResolver(Properties props) {
        this.properties = props;
    }

    public String resolveHost(WhereAmI whereAmI, String serviceId, InstanceType instanceType) {
        if(InstanceType.LOCAL == instanceType) {
            return serviceId;
        } else if (InstanceType.REMOTE == instanceType && WhereAmI.BOSUN == whereAmI) {
            return properties.phantomHostPattern().replace(SERVICE_NAME ,serviceId);
        }else {
            return properties.bosunHostPattern().replace(SERVICE_NAME ,serviceId);
        }
    }

    public boolean isSecure(WhereAmI whereAmI, InstanceType instanceType){
        if(InstanceType.REMOTE == instanceType) {
            return true;
        }
        return InstanceType.LOCAL != instanceType || WhereAmI.PHANTOM != whereAmI;
    }

    public String resolveCluster(WhereAmI whereAmI, InstanceType instanceType) {
        if (instanceType == InstanceType.LOCAL) {
            return whereAmI.name();
        }
        return WhereAmI.PHANTOM == whereAmI ? WhereAmI.BOSUN.name() : WhereAmI.PHANTOM.name();
    }

}
