package com.bjpowernode.p2p.service.loan;

/**
 * ClassName：RedisServer
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/26 22:05
 * @author:guoxin@bjpowernode.com
 */
public interface RedisServer {
    /**
     *
     * 通过redis将指定的value存放到缓存中
     *
     *
     *
     * **/
    void put(String key, String value);
    /**
     *
     * 从redis指定key的值
     *
     *
     *
     * **/

    String get(String key);

    Long getOnlyNumber();
}
