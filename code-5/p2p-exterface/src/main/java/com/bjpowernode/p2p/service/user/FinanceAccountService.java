package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

/**
 * ClassName：FinanceAccountService
 * Package:com.bjpowernode.p2p.service.user
 * Description:
 *
 * @date:2019/2/25 22:58
 * @author:guoxin@bjpowernode.com
 */

public interface FinanceAccountService {


    FinanceAccount queryFinAccountById(Integer uid);


}
