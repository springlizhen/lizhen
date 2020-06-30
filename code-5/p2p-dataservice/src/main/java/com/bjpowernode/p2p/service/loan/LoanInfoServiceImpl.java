package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName：LoanInfoServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/22 20:05
 * @author:guoxin@bjpowernode.com
 */
@Service
public class LoanInfoServiceImpl implements  LoanInfoService{
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public Double queryHistoryAverageRate() {
            //去redis缓存中取年化收益利率
       /* Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        if (historyAverageRate == null) {
            //从数据库中提取数值并放到redis中
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE,historyAverageRate,15, TimeUnit.MINUTES);

        }
        return historyAverageRate;
    }
    */
       Double histroyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
       if(histroyAverageRate ==null){
            histroyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE,histroyAverageRate,15,TimeUnit.MINUTES);

       }
       return histroyAverageRate;
    }



    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> map) {
        return loanInfoMapper.selectLoanInfoByPage(map);
    }

    @Override
    public PaginationVo<LoanInfo> queryLoanInfoByPage(Map<String, Object> map) {
       /* PaginationVo<LoanInfo> paginationVo = new PaginationVo<>();
        Long total = loanInfoMapper.selectTotal(map);
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(map);
        paginationVo.setDataList(loanInfoList);
        paginationVo.setTotal(total);
        return paginationVo;
*/
       PaginationVo<LoanInfo> paginationVo  = new PaginationVo<>();
       long total = loanInfoMapper.selectTotal(map);
       List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(map);
       paginationVo.setDataList(loanInfoList);
       paginationVo.setTotal(total);
       return paginationVo;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }


}
