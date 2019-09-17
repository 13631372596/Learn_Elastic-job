package com.carlson.elasticjob3.config;

import com.carlson.elasticjob3.annotation.ElasticSimpleJob;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Configuration
@ConditionalOnBean(ZookeeperConfig.class)
@AutoConfigureAfter(ZookeeperConfig.class)
public class SimpleJobConfig {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initSimpleJob(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ElasticSimpleJob.class);
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object instance = entry.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface.equals(SimpleJob.class)) {
                    ElasticSimpleJob annotation = instance.getClass().getAnnotation(ElasticSimpleJob.class);
                    JobCoreConfiguration jcc = JobCoreConfiguration.newBuilder(annotation.jobName(),
                            annotation.corn(), annotation.shardingTotalCount()).build();
                    SimpleJobConfiguration sjc = new SimpleJobConfiguration(jcc,
                            instance.getClass().getCanonicalName());
                    LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(sjc).overwrite(annotation.overwire())
                            .build();
                    new SpringJobScheduler((ElasticJob) instance, coordinatorRegistryCenter, ljc).init();
                }
            }
        }
    }
}
