package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.BiduserTop;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName：BidInfoServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/23 17:01
 * @author:guoxin@bjpowernode.com
 */
@Service
public class BidInfoServiceImpl implements  BidInforService {
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private  FinanceAccountMapper financeAccountMapper;
    @Override
    public Double queryAllBidMoney() {
        //设置redis对象的key序列化方式提高可读性
          redisTemplate.setKeySerializer(new StringRedisSerializer());
        //首先从redis缓存中取出数值
        Double allBIdMoney = (Double) redisTemplate.opsForValue().get(Constants.ALL_BID_MONEY);
        //如果没有从数据库中取得数据
        if (allBIdMoney == null) {
            allBIdMoney = bidInfoMapper.selectAllBidMoney();
            //将该数值放入redis缓存中
            redisTemplate.opsForValue().set(Constants.ALL_BID_MONEY,allBIdMoney,15, TimeUnit.MINUTES);

        }
        return allBIdMoney;
    }

    @Override
    public List<BidInfo> queryBidInfoListById(Integer loanId) {
        return bidInfoMapper.selectBidInfoListLoanId(loanId);
    }

    @Override
    public List<BidInfo> queryBidInfoListByUid(Map<String, Object> map) {
        return bidInfoMapper.selectBidInfoByPage(map);
    }

    @Override
    public PaginationVo<BidInfo> queryBidInfoByPage(Map<String, Object> map) {
        PaginationVo<BidInfo> paginationVo = new PaginationVo<>();
        paginationVo.setTotal(bidInfoMapper.selectTotal(map));
        paginationVo.setDataList(bidInfoMapper.selectBidInfoByPage(map));


        return paginationVo;
    }

    @Override
    public ResultObject invest(Map<String, Object> map) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);
        Integer uid = (Integer) map.get("uid");
        String  phone = (String) map.get("phone");
        Double bidMoney = (Double) map.get("bidMoney");
        Integer loanId = (Integer) map.get("loanId");
        //超卖现象
        //查询版本号
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(loanId);
        map.put("version",loanInfo.getVersion());


        //更新账户可投金额
        int updateMoney = loanInfoMapper.updateleftPrductMoneyByLoanId(map);
        if(updateMoney>0) {
            //更新账户剩余金额
            int updateFinanceAccount = financeAccountMapper.updateFinanceAccountByInvest(map);
            if (updateFinanceAccount > 0) {
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                bidInfo.setBidTime(new Date());
                bidInfo.setLoanId(loanId);
                bidInfo.setBidStatus(1);

                bidInfo.setUid(uid);
                bidInfo.setBidMoney(bidMoney);
                int insertBidCount = bidInfoMapper.insertSelective(bidInfo);
                if (insertBidCount > 0) {
                    //再次查询剩余可投金额
                    LoanInfo loanDetil = loanInfoMapper.selectByPrimaryKey(loanId);

                    //判断是否满标
                    if (0 == loanDetil.getLeftProductMoney()) {
                        //将产品的状态更新为满标状态
                        LoanInfo updateLoanInfo = new LoanInfo();
                        updateLoanInfo.setProductStatus(1);
                        updateLoanInfo.setId(loanId);
                        updateLoanInfo.setProductFullTime(new Date());
                        int updateCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
                        if(updateCount<0){
                            resultObject.setErrorCode(Constants.FAIL);
                        }

                    }
                    redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,phone,bidMoney);

                }else{
                    resultObject.setErrorCode(Constants.FAIL);
                }

            }else{
                resultObject.setErrorCode(Constants.FAIL);
            }
        }else{
            resultObject.setErrorCode(Constants.FAIL);
        }










        return resultObject;
    }

    @Override
    public List<BiduserTop> querybidUserTopList() {

        List<BiduserTop> list = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 9);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
       while(iterator.hasNext()){
           ZSetOperations.TypedTuple<Object> next = iterator.next();
           String phone = (String) next.getValue();
           Double score = next.getScore();
           BiduserTop biduserTop = new BiduserTop();
           biduserTop.setPhone(phone);
           biduserTop.setScore(score);
           list.add(biduserTop);

       }
        return list;
    }



}
