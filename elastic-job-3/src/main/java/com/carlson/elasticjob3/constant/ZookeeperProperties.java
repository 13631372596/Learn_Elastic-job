package com.carlson.elasticjob3.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "elasticjob.zookeeper")
@Getter
@Setter
public class ZookeeperProperties {
    //zookeeper地址
    private String serverList;
    //zookeeper命名空间
    private String namespace;
}
