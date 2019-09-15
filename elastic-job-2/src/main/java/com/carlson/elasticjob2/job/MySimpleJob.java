package com.carlson.elasticjob2.job;

import com.carlson.elasticjob2.annotation.ElasticSimpleJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@ElasticSimpleJob(jobName = "MySimpleJob",
        corn = "0/5 * * * * ?",
        shardingTotalCount = 2
)
@Component
public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("分片号：{}，总分片数：{}", shardingContext.getShardingItem(), shardingContext.getShardingTotalCount());
    }
}
