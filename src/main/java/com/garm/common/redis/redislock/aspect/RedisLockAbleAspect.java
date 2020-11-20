package com.garm.common.redis.redislock.aspect;


import com.garm.common.redis.redislock.annotation.RedisLockAable;
import com.garm.common.redis.redislock.util.AspectUtils;
import com.garm.common.redis.redislock.util.SpringExpressionUtils;
import com.garm.common.redis.redissession.RedisLock;
import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Component
public class RedisLockAbleAspect {
    private Logger logger = LoggerFactory.getLogger(RedisLockAable.class);

    public RedisLockAbleAspect() {
    }

    @Around("@annotation(lock)")
    public Object lockAble(ProceedingJoinPoint pjp, RedisLockAable lock) throws Throwable {
        Method method = AspectUtils.getMethod(pjp);
        String key = lock.key();
        String keyVal = "";
        if (!StringUtils.isEmpty(lock.key())) {
            keyVal = SpringExpressionUtils.parseKey(key, method, pjp.getArgs());
        }

        if (!StringUtils.isEmpty(lock.root())) {
            keyVal = Joiner.on("").join(lock.root(), keyVal, new Object[0]);
        } else {
            keyVal = Joiner.on("").join(pjp.getTarget().getClass().getSimpleName() + ":" + method.getName() + ":", keyVal, new Object[0]);
        }

        this.logger.info("------redis lock key------" + keyVal);
        RedisLock doLock = new RedisLock();
        doLock.lock(keyVal);

        Object var7;
        try {
            var7 = pjp.proceed();
        } finally {
            doLock.unLock();
        }

        return var7;
    }
}

