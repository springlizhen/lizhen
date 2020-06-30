package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * ClassName：LoanInfoService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/22 19:54
 * @author:guoxin@bjpowernode.com
 */
public interface LoanInfoService {
    /*
    * 获取平台历史年化利率的方法
    *
    * */

    Double queryHistoryAverageRate();

    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> map);

    PaginationVo<LoanInfo> queryLoanInfoByPage(Map<String, Object> map);

    LoanInfo queryLoanInfoById(Integer id);
}
