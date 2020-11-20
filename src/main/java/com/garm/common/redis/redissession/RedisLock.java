package com.garm.common.redis.redissession;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RedisLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);
    private RedissonClient redissonClient;
    private boolean locked = false;
    private RLock lock;

    public RedisLock() {
    }

    public void lock(String name) {
        this.redissonClient = RedissonUtil.getRedissonClient();
        this.lock = this.redissonClient.getLock(name);

        try {
            this.locked = this.lock.tryLock(20L, 20L, TimeUnit.SECONDS);
            if (this.locked) {
                logger.info("获取锁 [{}] 线程 [{}]", name, Thread.currentThread().getName());
            }
        } catch (InterruptedException var3) {
            logger.error("捕获到 InterruptedException", var3);
            Thread.currentThread().interrupt();
        }

    }

    public void unLock() {
        if (this.locked) {
            if (this.lock.getHoldCount() > 0) {
                String name = this.lock.getName();
                logger.info("解锁[{}] 线程[{}]", name, Thread.currentThread().getName());
                this.lock.unlock();
            } else {
                logger.warn("解锁 线程 [{}],当前线程已不再占有锁", Thread.currentThread().getName());
            }
        }

    }
}

