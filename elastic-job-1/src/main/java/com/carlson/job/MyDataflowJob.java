package com.carlson.job;

import com.carlson.model.Order;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyDataflowJob implements DataflowJob<Order> {

    private List<Order> orders = new ArrayList<>();

    {
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.setOrderid(i + 1);
            order.setStatus(0);
            orders.add(order);
        }
    }

    @Override
    public List<Order> fetchData(final ShardingContext shardingContext) {
        List<Order> orderList = orders.stream().filter(o -> o.getStatus() == 0)
                .filter(o -> o.getOrderid() % shardingContext.getShardingTotalCount() == shardingContext.getShardingItem())
                .collect(toList());
        List<Order> subList = null;
        if (orderList != null && orderList.size() > 0) {
            subList = orderList.subList(0, 10);
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalTime.now() + "，分片号：" + shardingContext.getShardingItem() + "获取数据：" + subList);
        return subList;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Order> data) {
        data.forEach(d -> d.setStatus(1));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocalTime.now() + "，分片号：" + shardingContext.getShardingItem() + "处理数据");
    }
}
