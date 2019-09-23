package com.carlson.elasticjob3.job;

import com.carlson.elasticjob3.annotation.ElasticDataflowJob;
import com.carlson.elasticjob3.model.TbOrder;
import com.carlson.elasticjob3.service.OrderService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@ElasticDataflowJob(
        jobName = "ConsumeOrderDataflowJob",
        corn = "0/10 * * * * ?",
        overwire = true,
        shardingTotalCount = 2,
        streamingProcess = true
)
@Component
@Slf4j
public class ConsumeOrderDataflowJob implements DataflowJob<Object> {
    @Autowired
    private OrderService orderService;

    @Override
    public List<Object> fetchData(ShardingContext shardingContext) {
        log.info(LocalTime.now() + "ConsumeOrderDataflowJob.shardingContext.getShardingItem():{} ======START======", shardingContext.getShardingItem());
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, -5);
        List<TbOrder> orders = orderService.getOrder(instance.getTime(), shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem());
        return new ArrayList<>(orders);
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Object> datas) {
        List<TbOrder> orders = datas.stream().map(d -> (TbOrder) d).collect(Collectors.toList());
        if(orders != null){
            ExecutorService es = Executors.newFixedThreadPool(4);
            for (TbOrder order : orders) {
                es.execute(() ->{
                    order.setOrderStatus(2);
                    order.setUpdateTime(new Date());
                    orderService.updateOrder(order);
                });
            }
            es.shutdown();
        }
        log.info(LocalTime.now() + "ConsumeOrderDataflowJob.shardingContext.getShardingItem():{} ======END======", shardingContext.getShardingItem());
    }
}
