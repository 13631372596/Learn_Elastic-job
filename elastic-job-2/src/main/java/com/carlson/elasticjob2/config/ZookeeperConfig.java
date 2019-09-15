package com.carlson.elasticjob2.config;

import com.carlson.elasticjob2.constant.ZookeeperProperties;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfig {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    /**
     * zookeeper注册中心
     *
     * @return
     */
    @Bean(initMethod = "init")
    public CoordinatorRegistryCenter zkCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration(zookeeperProperties.getServerList(),
                zookeeperProperties.getNamespace());
        CoordinatorRegistryCenter crc = new ZookeeperRegistryCenter(zc);
        return crc;
    }
}
