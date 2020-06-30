package com.chinags.common.redis;

import com.chinags.common.utils.Global;
import com.chinags.common.utils.StringUtils;

import java.util.List;

public class RedisUtils {
    /**
     * Jedis工具类
     */
    protected final JedisUtil jedisUtil = JedisUtil.getInstance();
    /**
     * 前缀
     */
    protected String prefix = Global.getRedisKeyName() + JedisUtil.NAME_LINK;
    /**
     * 超时时间
     */
    protected Integer timeout = JedisUtil.TIME_OUT;

    public static RedisUtils getRedisUtils(){
        return new RedisUtils();
    }

    /**
     * 设置用户权限
     * @param userCode 用户code
     * @param code code集合
     */
    protected static void set(String userCode, List<String> code, String key) throws Exception {
        if(code==null||StringUtils.isEmpty(userCode)){
            throw new Exception("用户code，code不允许为空");
        }
        RedisUtils redisUtils = getRedisUtils();
        JedisUtil.Hash hash = redisUtils.jedisUtil.new Hash();
        hash.hset(redisUtils.prefix + key, userCode,StringUtils.join(code.toArray(),","));
        redisUtils.jedisUtil.expire(redisUtils.prefix + key, redisUtils.timeout);
    }

    /**
     * 获取用户权限
     * @param userCode 用户code
     * @return code数组
     */
    protected static String[] get(String userCode, String key) throws Exception {
        if(StringUtils.isEmpty(userCode)){
            throw new Exception("code不允许为空");
        }
        RedisUtils redisUtils = getRedisUtils();
        JedisUtil.Hash hash = redisUtils.jedisUtil.new Hash();
        if(!hash.hexists(redisUtils.prefix + key, userCode)){
            return null;
        }
        String stringStringMap =  hash.hget(redisUtils.prefix + key, userCode);
        return stringStringMap.split(",");
    }

    /**
     * 删除用户权限
     * @param userCode 用户code
     * @param names redis权限名称
     * @return code数组
     */
    protected static void delete(String userCode, String... names) throws Exception {
        if(StringUtils.isEmpty(userCode)){
            throw new Exception("code不允许为空");
        }
        RedisUtils redisUtils = getRedisUtils();
        JedisUtil.Hash hash = redisUtils.jedisUtil.new Hash();
        for (String name : names) {
            if(hash.hexists(redisUtils.prefix + name,userCode)){
                hash.hdel(redisUtils.prefix + name,userCode);
            }
        }
    }
}
