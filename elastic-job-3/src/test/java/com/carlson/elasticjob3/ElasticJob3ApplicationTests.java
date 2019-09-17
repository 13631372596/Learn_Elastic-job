package com.carlson.elasticjob3;

import com.carlson.elasticjob3.model.TbOrder;
import com.carlson.elasticjob3.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticJob3ApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testProduceOrder(){
        TbOrder order = new TbOrder();
        order.setAmount(BigDecimal.TEN);
        order.setOrderName("订单1");
        order.setOrderStatus(1);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        int i = orderService.insertOrder(order);
        System.out.println(i);
    }

    @Test
    public void testConsumeOrder(){
        orderService.getOrder(new Date(),2, 0);
    }
}
