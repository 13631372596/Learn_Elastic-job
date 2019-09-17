package com.carlson.elasticjob3.service;

import com.carlson.elasticjob3.dao.TbOrderMapper;
import com.carlson.elasticjob3.model.TbOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    public int insertOrder(TbOrder order){
        return tbOrderMapper.insert(order);
    }

    public List<TbOrder> getOrder(Date date, int shardingTotalCount, int shardingItem) {
        return tbOrderMapper.getOrder(date, shardingTotalCount, shardingItem);
    }

    public void updateOrder(TbOrder order) {
        tbOrderMapper.updateByPrimaryKeySelective(order);
    }
}
