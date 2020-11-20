package com.garm.common.utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SRedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;
    private static SRedisUtil sRedisUtil;

    public SRedisUtil() {
    }

    @PostConstruct
    public void init() {
        sRedisUtil = this;
        sRedisUtil.redisTemplate = this.redisTemplate;
    }

    public static RedisTemplate getRedisTemplate() {
        return sRedisUtil.redisTemplate;
    }

    public static boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                sRedisUtil.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static long getExpire(String key) {
        return sRedisUtil.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean hasKey(String key) {
        try {
            return sRedisUtil.redisTemplate.hasKey(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                sRedisUtil.redisTemplate.delete(key[0]);
            } else {
                sRedisUtil.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }

    public static void remove(String key) {
        if (exists(key)) {
            sRedisUtil.redisTemplate.delete(key);
        }

    }

    public static Object hget(String key, String item) {
        return sRedisUtil.redisTemplate.opsForHash().get(key, item);
    }

    public static Map<Object, Object> hmget(String key) {
        return sRedisUtil.redisTemplate.opsForHash().entries(key);
    }

    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            sRedisUtil.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            sRedisUtil.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value) {
        try {
            sRedisUtil.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value, long time) {
        try {
            sRedisUtil.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static void hdel(String key, Object... item) {
        sRedisUtil.redisTemplate.opsForHash().delete(key, item);
    }

    public static boolean hHasKey(String key, String item) {
        return sRedisUtil.redisTemplate.opsForHash().hasKey(key, item);
    }

    public static double hincr(String key, String item, double by) {
        return sRedisUtil.redisTemplate.opsForHash().increment(key, item, by);
    }

    public static double hdecr(String key, String item, double by) {
        return sRedisUtil.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public static boolean leftPush(String key, Object value) {
        boolean result = false;

        try {
            ListOperations<Serializable, Object> operations = sRedisUtil.redisTemplate.opsForList();
            operations.leftPush(key, value);
            result = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static long size(byte[] key) {
        return sRedisUtil.redisTemplate.opsForList().size(key);
    }

    public static Object index(byte[] key, long index) {
        return sRedisUtil.redisTemplate.opsForList().index(key, index);
    }

    public static Object leftPop(byte[] key) {
        return sRedisUtil.redisTemplate.opsForList().leftPop(key);
    }

    public static Object rightPop(byte[] key) {
        return sRedisUtil.redisTemplate.opsForList().rightPop(key);
    }

    public static Long rpush(String key, String value) {
        return sRedisUtil.redisTemplate.opsForList().rightPush(key, value);
    }

    public static List<Object> range(String key, long start, long end) {
        try {
            return sRedisUtil.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static Object lGetIndex(String key, long index) {
        try {
            return sRedisUtil.redisTemplate.opsForList().index(key, index);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean lSet(String key, Object value) {
        try {
            sRedisUtil.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, Object value, long time) {
        try {
            sRedisUtil.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value) {
        try {
            sRedisUtil.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            sRedisUtil.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            sRedisUtil.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = sRedisUtil.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0L;
        }
    }

    public static Set<Object> sGet(String key) {
        try {
            return sRedisUtil.redisTemplate.opsForSet().members(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static boolean sHasKey(String key, Object value) {
        try {
            return sRedisUtil.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static long sSet(String key, Object... values) {
        try {
            return sRedisUtil.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = sRedisUtil.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                expire(key, time);
            }

            return count;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0L;
        }
    }

    public static long sGetSetSize(String key) {
        try {
            return sRedisUtil.redisTemplate.opsForSet().size(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0L;
        }
    }

    public static long setRemove(String key, Object... values) {
        try {
            Long count = sRedisUtil.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static void removePattern(String pattern) {
        Set<Serializable> keys = sRedisUtil.redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            sRedisUtil.redisTemplate.delete(keys);
        }

    }

    public static boolean exists(String key) {
        return sRedisUtil.redisTemplate.hasKey(key);
    }

    public static Object get(String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = sRedisUtil.redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    public static boolean set(String key, Object value) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = sRedisUtil.redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static boolean set(String key, Object value, Long expireTime) {
        boolean result = false;

        try {
            ValueOperations<Serializable, Object> operations = sRedisUtil.redisTemplate.opsForValue();
            operations.set(key, value);
            sRedisUtil.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public static boolean existsHash(String key, Object hashKey) {
        HashOperations<String, String, Object> operations = sRedisUtil.redisTemplate.opsForHash();
        return operations.hasKey(key, hashKey);
    }

    public static boolean setHash(String key, Object hashKey, Object value) {
        boolean result = false;

        try {
            HashOperations<String, Object, Object> operations = sRedisUtil.redisTemplate.opsForHash();
            operations.put(key, hashKey, value);
            result = true;
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public static boolean setHash(String key, Object hashKey, Object value, Long expireTime) {
        boolean result = false;

        try {
            HashOperations<String, Object, Object> operations = sRedisUtil.redisTemplate.opsForHash();
            operations.put(key, hashKey, value);
            sRedisUtil.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public static List<Object> getHashAll(String key) {
        List result = null;

        try {
            HashOperations<String, Object, Object> operations = sRedisUtil.redisTemplate.opsForHash();
            result = operations.values(key);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return result;
    }

    public static Object getHashByHashKey(String key, Object hashKey) {
        Object result = null;

        try {
            HashOperations<String, Object, Object> operations = sRedisUtil.redisTemplate.opsForHash();
            result = operations.get(key, hashKey);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static boolean delHashKey(String key, Object... hashKey) {
        boolean result = false;

        try {
            HashOperations<String, Object, Object> operations = sRedisUtil.redisTemplate.opsForHash();
            operations.delete(key, hashKey);
            result = true;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return result;
    }

    public static Set<String> keys(String key) {
        return sRedisUtil.redisTemplate.keys(key);
    }
}

