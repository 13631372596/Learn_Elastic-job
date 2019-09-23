package com.carlson.elasticjob3.config;

import com.carlson.elasticjob3.annotation.ElasticDataflowJob;
import com.carlson.elasticjob3.annotation.ElasticSimpleJob;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
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
public class DataflowJobConfig {

    @Autowired
    private CoordinatorRegistryCenter coordinatorRegistryCenter;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void initSimpleJob(){
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ElasticDataflowJob.class);
        Set<Map.Entry<String, Object>> entries = beans.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object instance = entry.getValue();
            Class<?>[] interfaces = instance.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                if (anInterface.equals(DataflowJob.class)) {
                    ElasticDataflowJob annotation = instance.getClass().getAnnotation(ElasticDataflowJob.class);
                    JobCoreConfiguration jcc = JobCoreConfiguration.newBuilder(annotation.jobName(),
                            annotation.corn(), annotation.shardingTotalCount()).build();
                    DataflowJobConfiguration djc = new DataflowJobConfiguration(jcc,
                            instance.getClass().getCanonicalName(), annotation.streamingProcess());
                    LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(djc).overwrite(annotation.overwire())
                            .build();
                    new SpringJobScheduler((ElasticJob) instance, coordinatorRegistryCenter, ljc).init();
                }
            }
        }
    }
}
