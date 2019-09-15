package com.carlson;

import com.carlson.job.MyDataflowJob;
import com.carlson.job.MySimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

public class App {
    public static void main(String[] args) {
        System.out.println("=========START=========");
        new JobScheduler(zkCenter(), configScript()).init();
        System.out.println("=========END=========");
    }

    /**
     * zk配置
     *
     * @return
     */
    public static CoordinatorRegistryCenter zkCenter() {
        ZookeeperConfiguration zc = new ZookeeperConfiguration("localhost:2181", "elastic-job-1");
        CoordinatorRegistryCenter crc = new ZookeeperRegistryCenter(zc);
        crc.init();
        return crc;
    }

    /**
     * job配置
     *
     * @return
     */
    public static LiteJobConfiguration configSimple() {
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("mySimpleJob", "0/5 * * * * ?", 2)
                .build();
        //job类型配置
        JobTypeConfiguration jtc = new SimpleJobConfiguration(jcc,
                MySimpleJob.class.getCanonicalName());
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc)
                .overwrite(true)
                .build();
        return ljc;
    }

    public static LiteJobConfiguration configDataflow(){
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("MyDataflowJob", "0/5 * * * * ?", 2)
                .build();
        //job类型配置
        JobTypeConfiguration jtc = new DataflowJobConfiguration(jcc,
                MyDataflowJob.class.getCanonicalName(), true);
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc)
                .overwrite(true)
                .build();
        return ljc;
    }

    public static LiteJobConfiguration configScript(){
        //job核心配置
        JobCoreConfiguration jcc = JobCoreConfiguration
                .newBuilder("MyScriptJob", "0/5 * * * * ?", 2)
                .build();
        //job类型配置
        JobTypeConfiguration jtc = new ScriptJobConfiguration(jcc, "D:\\Projects\\MyProject\\Learn_Elastic-job\\elastic-job-1\\src\\main\\resources\\test.cmd");
        //job根的配置
        LiteJobConfiguration ljc = LiteJobConfiguration.newBuilder(jtc)
                .overwrite(true)
                .build();
        return ljc;
    }
}
