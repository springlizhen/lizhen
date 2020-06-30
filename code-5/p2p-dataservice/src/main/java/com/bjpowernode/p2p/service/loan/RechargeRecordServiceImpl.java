package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.mapper.loan.RechargeRecordMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * ClassName：RechargeRecordServiceImpl
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/3/2 13:46
 * @author:guoxin@bjpowernode.com
 */
@Service
public class RechargeRecordServiceImpl implements  RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public int addRechargeCount(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);

    }

    @Override
    public int recharge(Map<String, Object> map) {
        int updateFinanceAccount  = financeAccountMapper.updateFinAccountByRecharge(map);
        if(updateFinanceAccount>0){
            RechargeRecord rechargeRecord  = new RechargeRecord();
            rechargeRecord.setRechargeStatus("1");
            rechargeRecord.setRechargeNo((String)map.get("rechargeNo"));
            int updateCount  = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
            if(updateCount<=0){
                return 0;
            }
        }else{
            return 0;
        }
        return 1;
    }


}
