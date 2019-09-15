package com.carlson.elasticjob2.config;

import com.carlson.elasticjob2.annotation.ElasticSimpleJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
@ConditionalOnBean(ZookeeperConfig.class)
@AutoConfigureAfter(ZookeeperConfig.class)
public class SimpleJobConfig {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initSimpleJob() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ElasticSimpleJob.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object instance = entry.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> superInterface : interfaces) {
                if (superInterface == SimpleJob.class) {
                    ElasticSimpleJob annotation = instance.getClass().getAnnotation(ElasticSimpleJob.class);
                    String jobName = annotation.jobName();
                    String corn = annotation.corn();
                    int shardingTotalCount = annotation.shardingTotalCount();
                    boolean overwire = annotation.overwire();
                    //job核心配置
                    JobCoreConfiguration jcc = JobCoreConfiguration
                            .newBuilder(jobName, corn, shardingTotalCount)
                            .build();
                    //job类型配置
                    JobTypeConfiguration jtc = new SimpleJobConfiguration(jcc,
                            instance.getClass().getCanonicalName());
                    //job根的配置
                    LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc)
                            .overwrite(overwire)
                            .build();
                    new JobScheduler(coordinatorRegistryCenter, ljc).init();
                }
            }
        }
    }
}
