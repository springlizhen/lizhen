package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.vo.BiduserTop;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.model.vo.ResultObject;

import java.util.List;
import java.util.Map;

/**
 * ClassName：BidInforService
 * Package:com.bjpowernode.p2p.service.loan
 * Description:
 *
 * @date:2019/2/23 17:01
 * @author:guoxin@bjpowernode.com
 */
public interface BidInforService {
    Double queryAllBidMoney();

    List<BidInfo> queryBidInfoListById(Integer loanId);

    List<BidInfo> queryBidInfoListByUid(Map<String, Object> map);

    PaginationVo<BidInfo> queryBidInfoByPage(Map<String, Object> map);

    ResultObject invest(Map<String, Object> map);

    List<BiduserTop> querybidUserTopList();
}
