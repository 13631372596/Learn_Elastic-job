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
import java.time.LocalTime;
import java.util.Date;

@ElasticSimpleJob(
        corn = "0/2 * * * * ?",
        jobName = "ProduceOrderJob",
        shardingTotalCount = 1,
        overwire = true
)
@Component
@Slf4j
public class ProduceOrderJob implements SimpleJob {

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info(LocalTime.now() + "ProduceOrderJob.shardingContext.getShardingItem():{}", shardingContext.getShardingItem());
        int total = 10;
        for (int i = 0; i < total; i++) {
            TbOrder order = new TbOrder();
            order.setAmount(BigDecimal.TEN);
            order.setOrderName("订单" + i);
            order.setOrderStatus(1);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderService.insertOrder(order);
        }
    }
}
