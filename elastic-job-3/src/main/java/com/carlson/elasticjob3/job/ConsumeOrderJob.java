package com.carlson.elasticjob3.job;

import com.carlson.elasticjob3.annotation.ElasticSimpleJob;
import com.carlson.elasticjob3.model.TbOrder;
import com.carlson.elasticjob3.service.OrderService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ElasticSimpleJob(
        corn = "0/20 * * * * ?",
        jobName = "ConsumeOrderJob",
        shardingTotalCount = 2,
        overwire = true
)
@Component
@Slf4j
public class ConsumeOrderJob implements SimpleJob {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("ConsumeOrderJob.shardingContext.getShardingItem():{}", shardingContext.getShardingItem());
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, -5);
        List<TbOrder> orders = orderService.getOrder(instance.getTime(), shardingContext.getShardingTotalCount(),
                shardingContext.getShardingItem());
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
    }
}
