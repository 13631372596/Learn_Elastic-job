package com.carlson.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class MySimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        System.out.println("分片号：" + shardingContext.getShardingItem() + ", 总分片数："
                + shardingContext.getShardingTotalCount());
    }
}
