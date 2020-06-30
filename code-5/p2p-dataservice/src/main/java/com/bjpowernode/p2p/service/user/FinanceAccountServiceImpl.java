package com.bjpowernode.p2p.service.user;


import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName：FinanceAccountServiceImpl
 * Package:com.bjpowernode.p2p.service.user
 * Description:
 *
 * @date:2019/2/25 23:17
 * @author:guoxin@bjpowernode.com
 *
 */
@Service
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public FinanceAccount queryFinAccountById(Integer uid) {
        return financeAccountMapper.selectFinanceAccountById(uid);
    }
}
