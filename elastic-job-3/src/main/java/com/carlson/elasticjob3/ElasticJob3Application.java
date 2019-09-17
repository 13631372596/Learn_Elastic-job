package com.carlson.elasticjob3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.carlson.elasticjob3.dao")
public class ElasticJob3Application {

    public static void main(String[] args) {
        SpringApplication.run(ElasticJob3Application.class, args);
    }

}
