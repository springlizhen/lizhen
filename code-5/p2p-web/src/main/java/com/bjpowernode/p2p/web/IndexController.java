package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInforService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;

import com.bjpowernode.p2p.service.user.UserService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName：${Name}
 * Package:${Package_Name}
 * Description:
 *
 * @date:2019/2/22 19:36
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class IndexController {
   /* @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidInforService bindInforService;
   @RequestMapping(value="/index")

    public  String index(HttpServletRequest request, Model model){
       //获得历史平均年化利率
            Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
             model.addAttribute(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);



       //获得平台总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        //平台累计投资金额
        Double allBidMoney = bindInforService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);


        *//*将以下查询看做一个分页，根据产品类型查询产品列表（产品类型，页码，每页显示的记录数）返回list<产品>
            将参数放到map集合

        *
        *
        *
        *
        *
        * *//*
        //页码
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("currentPage",0);
       //新手宝 显示第一页，每页显示一条
        //每页显示系数
       map.put("pageSize",1);


       //产品类型
       map.put("productType",Constants.PRODUCT_TYPE_X);
       List<LoanInfo>  xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
       model.addAttribute("xLoanInfoList",xLoanInfoList);

       //优选产品 显示第一页，每页显示4条
       //每页显示条数
       map.put("pageSize",4);
       //产品类型
       map.put("productType",Constants.PRODUCT_TYPE_U);
       List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
       model.addAttribute("uLoanInfoList",uLoanInfoList);
       //散标产品 显示第一页，每页显示八条
       map.put("pageSize",8);
       map.put("productType",Constants.PRODUCT_TYPE_S);
       List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
       model.addAttribute("sLoanInfoList",sLoanInfoList);
       return "index";


   }
*/
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInforService bidInforService;
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/index")
    public String index(HttpRequest request,Model model){
        //获取年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);
        //获得平台总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        //平台累计投资金额
        Double allBidMoney = bidInforService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY,allBidMoney);
        //分页查询将（产品类型，页码，每页显示的记录数） 返回集合，将参数放到map集合中
        Map<String,Object> map = new HashMap<>();
        map.put("currentPage",0);
        //新手宝每页显示系数
        map.put("pageSize",1);
        //产品类型
        map.put("pageType",Constants.PRODUCT_TYPE_X);
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
        model.addAttribute("xLoanInfoList",loanInfoList);
        //优选产品 显示第一页，每页显示4条
        //每页显示条数
        map.put("pageSize",4);
        //产品类型
        map.put("productType",Constants.PRODUCT_TYPE_U);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
        model.addAttribute("uLoanInfoList",uLoanInfoList);
        //散标产品 显示第一页，每页显示八条
        map.put("pageSize",8);
        map.put("productType",Constants.PRODUCT_TYPE_S);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(map);
        model.addAttribute("sLoanInfoList",sLoanInfoList);
        return "index";




    }



}
