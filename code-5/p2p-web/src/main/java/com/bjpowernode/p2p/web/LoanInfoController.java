package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BiduserTop;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.service.loan.BidInforService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import javafx.scene.control.Pagination;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName：LoanInfoController
 * Package:com.bjpowernode.p2p.web
 * Description:
 *
 * @date:2019/2/23 22:05
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class LoanInfoController {
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInforService bidInforService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @RequestMapping(value="/loan/loan")
    public String Loan(HttpServletRequest request, Model model,
                        @RequestParam(value="ptype1", required=false) Integer ptype,
                        @RequestParam(value = "currentPage",required = false) Integer currentPage){
        if (currentPage == null) {
            currentPage = 1;
        }
        Map<String,Object> map = new HashMap<>();
        if (ptype != null) {
            map.put("productType",ptype);
        }
        int pageSize = 9;
        //跳过的记录数，页码
        map.put("currentPage",(currentPage-1)*pageSize);


        //每页显示的记录数
        map.put("pageSize",pageSize);
        //分页查询产品（产品类型，页码，每页显示的记录数）

        PaginationVo<LoanInfo> paginationVo = loanInfoService.queryLoanInfoByPage(map);
        //总页数
        int totalPage = paginationVo.getTotal().intValue()/pageSize;
        int mod = paginationVo.getTotal().intValue()%pageSize;
        if(mod>0){
            totalPage = totalPage+1;
        }
        model.addAttribute("totalRows",paginationVo.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("loanInfoList",paginationVo.getDataList());
        model.addAttribute("currentPage",currentPage);
        if (ptype != null) {
            model.addAttribute("ptype",ptype);
        }
            List<BiduserTop> biduserTopList= bidInforService.querybidUserTopList();
            model.addAttribute("biduserTopList",biduserTopList);

        return "loan";


    }

    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                                @RequestParam(value="id",required=true) Integer id){
        //根据id获取产品详细信息
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);



        //根据id获取投资记录
        List<BidInfo> list = bidInforService.queryBidInfoListById(id);
        //从session中获取投资记录
        User sessionUser = (User)request.getSession().getAttribute(Constants.SESSION_USER);


        //判断用户是否登录
        if (sessionUser != null) {
                //根据用户标识获取用户账户资金信息
            FinanceAccount financeAccount = financeAccountService.queryFinAccountById(sessionUser.getId());
            model.addAttribute("financeAccount",financeAccount);
        }


        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("list",list);

        return  "loanInfo";

    }






}
