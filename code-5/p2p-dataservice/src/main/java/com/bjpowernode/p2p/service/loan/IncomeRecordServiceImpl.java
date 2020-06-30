package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.common.util.DateUtils;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.IncomeRecordMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.IncomeRecord;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName：IncomeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/3/1 13:26
 * @author:guoxin@bjpowernode.com
 */
@Service
public class IncomeRecordServiceImpl implements IncomeRecordService {
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public void generateIncomeplan() {
        //获取产品状态为一已满标的产品 返回LIst【满标产品】
        List<LoanInfo> loanInfoList  = loanInfoMapper.selectLoanInfoListByProductStatus(1);
        //循环遍历取得每一个产品
        for(LoanInfo loanInfo :loanInfoList){
            //根据产品标识获取所有投资记录---》list【投资记录】
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoByLoanId(loanInfo.getId());
            //根据投资记录获取收益记录
            for(BidInfo bidInfo :bidInfoList){
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setIncomeStatus(0);
                incomeRecord.setBidId(bidInfo.getId());


                //收益时间
                Date incomeDate  = null;
                //收益金额
                Double incomeMoney  = null;
                if(Constants.PRODUCT_TYPE_X == loanInfo.getProductType()){
                    incomeDate  = DateUtils.getDateByAdds(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle();
                }else{
                    incomeDate =DateUtils.getDateByAdds(loanInfo.getProductFullTime(),loanInfo.getCycle());
                    incomeMoney = bidInfo.getBidMoney()*(loanInfo.getRate()/100/365)*loanInfo.getCycle()*30;
                }
                incomeMoney = Math.round(incomeMoney*Math.pow(10,2))/Math.pow(10,2);
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecordMapper.insertSelective(incomeRecord);

            }
            //将产品状态更新并生成新的收益记录
            LoanInfo updateLoanInfo = new LoanInfo();
             updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);
            loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);

    }




    }

    @Override
    public void generateIncomeBack() {
        //查询收益记录为零（未返还）与收益时间相等的记录----list[收益记录]
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByIncomeStatusAndCurds();
        //循环收益记录获取每一条收益记录
        for(IncomeRecord incomeRecord :incomeRecordList){
            //将收益记录返还给用户的账户
            Map<String,Object> map = new HashMap<>();
            map.put("uid",incomeRecord.getUid());
            map.put("bidMoney",incomeRecord.getBidMoney());
            map.put("incomeMoney",incomeRecord.getIncomeMoney());
            int updateCount = financeAccountMapper.updateFinanceAccountByIncmeBack(map);

            //更新收益记录状态为1已返还
            IncomeRecord updateIncomeRecord  = new IncomeRecord();
            updateIncomeRecord.setId(incomeRecord.getId());
            updateIncomeRecord.setIncomeStatus(1);
            incomeRecordMapper.updateByPrimaryKeySelective(updateIncomeRecord);
        }











    }

}
