package com.carlson.elasticjob3.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticDataflowJob {

    String jobName() default "";

    String corn() default "";

    int shardingTotalCount() default 0;

    boolean overwire() default false;

    boolean streamingProcess() default false;
}
