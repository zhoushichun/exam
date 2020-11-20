package com.garm.common.redis.redislock.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLockAable {
    String root() default "";

    String key() default "";
}
