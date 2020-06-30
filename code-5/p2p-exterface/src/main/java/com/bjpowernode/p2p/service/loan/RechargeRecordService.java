package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.RechargeRecord;

import java.util.Map;

/**
 * ClassName：RechargeRecordService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/3/2 13:32
 * @author:guoxin@bjpowernode.com
 */
public interface RechargeRecordService {

    int addRechargeCount(RechargeRecord rechargeRecord);

    int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);


    int recharge(Map<String, Object> map);
}
