package com.carlson.elasticjob2.job;

import com.carlson.elasticjob2.annotation.ElasticDataflowJob;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ElasticDataflowJob(
        jobName = "MyDataflowJob",
        corn = "0/5 * * * * ?",
        shardingTotalCount = 2,
        overwire = true,
        streamProcess = true
)
@Component
public class MyDataflowJob implements DataflowJob<Integer> {

    private List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

    @Override
    public List<Integer> fetchData(ShardingContext shardingContext) {
        log.info("分片号：{}，总分片数：{}", shardingContext.getShardingItem(), shardingContext.getShardingTotalCount());
        List<Integer> newList = new ArrayList<>();
        for (Integer i : list) {
            if(i % shardingContext.getShardingTotalCount() == shardingContext.getShardingItem()){
                newList.add(i);
                break;
            }
        }
        return newList;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Integer> data) {
        log.info("分片号：{}，总分片数：{}，移除数据：{}", shardingContext.getShardingItem(),
                shardingContext.getShardingTotalCount(), data.get(0));
        list.removeAll(data);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
