package test;

import com.bjpowernode.p2p.common.contants.Constants;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.Assert.*;

/**
 * ClassName：BidInfoServiceImplTest
 * Package:test
 * Description:
 *
 * @date:2019/6/5 23:05
 * @author:lizhen@ziyoulianmeng.com
 */
public class BidInfoServiceImplTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void queryAllBidMone
        Double allBIdMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
        System.out.println(allBIdMoney);
    }
}