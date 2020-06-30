package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;

import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.PaginationVo;
import com.bjpowernode.p2p.model.vo.ResultObject;
import com.bjpowernode.p2p.service.loan.BidInforService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName：BidInfoController
 * Package:com.bjpowernode.p2p.web
 * Description:
 *
 * @date:2019/2/28 13:47
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class BidInfoController {
    @Autowired
    private BidInforService bidInforService;
    @RequestMapping(value = "/loan/myInvest")
    public String myInvest(HttpServletRequest request, Model model,
                                @RequestParam (value="currentPage", required = false) Integer currentPage ){
        if (currentPage == null) {
            currentPage = 1;
        }



        //从session中获取用户参数
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //获取参数
        Map<String,Object> map = new HashMap<>();
        int pageSize = 10;
        map.put("uid",sessionUser.getId());
        map.put("currentPage",currentPage);
        map.put("pageSize",pageSize);
        //根据用户标识返回投资记录{用户标识，页码，每页显示的记录数}-----返回用户模型（每页显示的记录数，总记录数）
        PaginationVo<BidInfo> paginationVo = bidInforService.queryBidInfoByPage(map);
        int totalPage = paginationVo.getTotal().intValue()/pageSize;
        int mod = paginationVo.getTotal().intValue()%pageSize;
        if(mod>0){
            totalPage = totalPage +1;
        }

        model.addAttribute("totalRows",paginationVo.getTotal());
        model.addAttribute("currentPage",currentPage);
        model.addAttribute("bidInfoList",paginationVo.getDataList());
        model.addAttribute("totalPage",totalPage);
        return "myInvest";


    }

    @RequestMapping(value = "/loan/invest" ,method= RequestMethod.POST)
    public @ResponseBody Object invest(HttpServletRequest request,Model model,
                                       @RequestParam (value = "loanId",required = true) Integer loanId,
                                        @RequestParam(value = "bidMoney" ,required = true) Double bidMoney ){
        //从session中取得user对象
        User sessionUser = (User)request.getSession().getAttribute(Constants.SESSION_USER);
        Map<String,Object> map = new HashMap<>();
        map.put("uid",sessionUser.getId());
        map.put("phone",sessionUser.getPhone());
        map.put("loanId",loanId);
        map.put("bidMoney",bidMoney);

        //投资之后【更新账户余额，更新投资记录，更新剩余可投金额，更新是否满标】-----【用户标识，产品标识，投资金额】
       ResultObject resultObject = bidInforService.invest(map);
        if(StringUtils.equals(Constants.SUCCESS,resultObject.getErrorCode())){
            map.put(Constants.ERROR_MESSAGE,Constants.OK);
        }else{
            map.put(Constants.ERROR_MESSAGE,"投资失败");
            return map;
        }
        return map;
    }






}
