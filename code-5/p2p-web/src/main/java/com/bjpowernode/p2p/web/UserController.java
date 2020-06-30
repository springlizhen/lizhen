package com.bjpowernode.p2p.web;


import com.alibaba.fastjson.JSONObject;

import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;
import com.bjpowernode.p2p.service.loan.BidInforService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.loan.RedisServer;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;


import org.apache.commons.lang3.StringUtils;

import org.apache.zookeeper.server.SessionTracker;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName：UserController
 * Package:com.bjpowernode.p2p.web
 * Description:
 *
 * @date:2019/2/25 15:53
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInforService bidInforService;
    @Autowired
    private RedisServer redisServer;
    @RequestMapping(value="/loan/checkPhone" )
    public @ResponseBody Object checkPhone(HttpServletRequest request,
                      @RequestParam(value="phone",required = true) String phone){
        Map<String,Object> map = new HashMap<>();
       User user =  userService.queryUserByPhone(phone);
        if (user != null) {
            map.put(Constants.ERROR_MESSAGE,"该手机号已经被注册，请输入其他手机号");
        }
        map.put(Constants.ERROR_MESSAGE,Constants.OK);
        return map;

    }
    @RequestMapping(value="/loan/checkCaptcha",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> checkCaptcha(HttpServletRequest request,
                                                            @RequestParam (value="captcha",required = true) String captcha){
        Map<String,Object> map = new HashMap<>();
        //从session取得图形验证码
         String sessionCaptcha=(String)request.getSession().getAttribute(Constants.CAPTCHA);
         //判断两个验证码是否相等
         if(!StringUtils.equalsIgnoreCase(captcha,sessionCaptcha)){
             map.put(Constants.ERROR_MESSAGE,"请输入正确的图片验证码");
             return map;

         }
         map.put(Constants.ERROR_MESSAGE,Constants.OK);
         return map;
    }

    /*
    *
    * 注册用户
    *
    * */
    @RequestMapping(value = "/loan/register",method = RequestMethod.GET)
    public @ResponseBody Object register(HttpServletRequest request,
                           @RequestParam(value ="phone",required = true)  String phone,
                            @RequestParam(value = "loginPassword",required = true)  String loginPassword){
            Map<String,Object> map = new HashMap<>();
            //用户注册【1.新增用户，开立账户】 （手机号，密码） ----int
            ResultObject resultObject = userService.register(phone,loginPassword);
            if(StringUtils.equals(resultObject.getErrorCode(),Constants.SUCCESS)){
            //将用户信息存放到session中
//            request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(phone));
                request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(phone));

                map.put(Constants.ERROR_MESSAGE,Constants.OK);
            }else{
            map.put(Constants.ERROR_MESSAGE,"注册失败");
        }

        return map;
    }

    @RequestMapping(value = "/loan/myFinanceAccount",method = RequestMethod.GET)
    public @ResponseBody FinanceAccount myFinaceAccount(HttpServletRequest request){
        //从session 中取出用户的信息
        User sessionUser= (User)request.getSession().getAttribute(Constants.SESSION_USER);
        //根据用户id取出可用余额
        FinanceAccount financeAccount = financeAccountService.queryFinAccountById(sessionUser.getId());
        return financeAccount;

    }


    @RequestMapping(value="/loan/verifyRealName",method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> verifyRealName(HttpServletRequest request,
                                                           @RequestParam (value = "idCard",required = true) String idCard,
                                                           @RequestParam(value="realName",required = true) String realName) throws Exception {
        Map<String,Object> map = new HashMap<>();
        //准备需要实名认证的参数
        Map<String,Object> map1 = new HashMap<>();
        map1.put("idCard",idCard);
        map1.put("realName",realName);
        map1.put("appkey","");
        //通过实名认证服务，返回json字符串
       /* String jsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test",map1);*/
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"error_code\": 0,\n" +
                "        \"reason\": \"成功\",\n" +
                "        \"result\": {\n" +
                "            \"realname\": \"乐天磊\",\n" +
                "            \"idcard\": \"350721197702134399\",\n" +
                "            \"isok\": true\n" +
                "        }\n" +
                "    }\n" +
                "}";
        //将获取到的json字符串转换成json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获取通信标识
        String code = jsonObject.getString("code");
        if(StringUtils.equals("10000",code)){
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            if(isok){
                //将真实姓名和身份证号更新到session中
                User sessionUser =(User) request.getSession().getAttribute(Constants.SESSION_USER);
                User updateUser = new User();
                updateUser.setId(sessionUser.getId());
                updateUser.setIdCard(idCard);
                updateUser.setName(realName);
                int modifyUserCount = userService.modifyUserById(updateUser);
                if(modifyUserCount>0){
                    //更新session的值
                    sessionUser.setName(realName);
                    sessionUser.setIdCard(idCard);
                    request.getSession().setAttribute(Constants.SESSION_USER,sessionUser);
                    map.put(Constants.ERROR_MESSAGE,Constants.OK);

                }else{
                    map.put(Constants.ERROR_MESSAGE,"实名认证异常");
                    return map;
                }


            }else{
                map.put(Constants.ERROR_MESSAGE,"你的真实姓名和身份证号不匹配");
                return map;
            }

        }else{
            map.put(Constants.ERROR_MESSAGE,"通信异常");
                return map;
        }
        return map;



    }
    @RequestMapping(value="/loan/logout")
    public String logout(HttpServletRequest request){
        //让session失效
        request.getSession().invalidate();
        return "redirect:/index";
    }
           @RequestMapping(value="/loan/loadStart")
        public @ResponseBody Object loadStart(){
            Map<String,Object> map =new HashMap<>();
            //获取历史平均年化利率
            Double histroyAverageRate = loanInfoService.queryHistoryAverageRate();
            //获取平台总人数
               Long allUserCount = userService.queryAllUserCount();
        //获取累计成交总额
        Double allBidMoney  = bidInforService.queryAllBidMoney();
        map.put(Constants.HISTORY_AVERAGE_RATE,histroyAverageRate);
        map.put(Constants.ALL_USER_COUNT,allUserCount);
        map.put(Constants.ALL_BID_MONEY,allBidMoney);
        return map;


    }
    @RequestMapping(value = "/loan/login")
    public @ResponseBody Object login(HttpServletRequest request,
                                        @RequestParam(value="phone",required = true) String phone,
                                        @RequestParam(value = "loginPassword",required = true) String loginPassword,
                                        @RequestParam(value = "messageCode",required = true) String messageCode){
        Map<String,Object> map = new HashMap<>();
        //从redis中获取短信验证码
        String redisMessageCode = redisServer.get(phone);
        //验证短信验证码
        if(StringUtils.equals(messageCode,redisMessageCode)){
        //用户登录功能【1.根据手机号和密码查询用户 2.更新最近登录时间】（手机号，密码） -> 返回User
        User user = userService.login(phone,loginPassword);
        if(user == null){
            map.put(Constants.ERROR_MESSAGE,"你的手机号不能是空的");
            return map;
        }
        map.put(Constants.ERROR_MESSAGE,Constants.OK);
        request.getSession().setAttribute(Constants.SESSION_USER,user);
        }else{
            map.put(Constants.ERROR_MESSAGE,"你的短信验证码有问题");
            return map;
        }

        return map;



    }
    @RequestMapping(value = "/loan/messageCode")
    public @ResponseBody Object messageCode(HttpServletRequest request,
                                            @RequestParam(value = "phone",required = true) String phone) throws Exception {
        Map<String,Object> map = new HashMap<>();
        //生成一个随机数字
        String messageCode = this.getRandomCode(4);
        //准备发送短信的内容
        String content = "【凯信通】您的验证码是："+messageCode;
        //准备发送短信的请求内容
        Map<String,Object> map1 = new HashMap<>();
        map1.put("appkey" ,"");
        map1.put("mobile",phone);
        map1.put("content",content);
        //发送短信，调用互联网接口
//        String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong",map1);
        //String jsonString =HttpClienUtils.doPost（“”，map1）;
        /***
         * JsonObject jsonobject = jsonObject.parseObject(jsonString);
         * String code = jsonject.getString();
         *
         *
         *
         *
         *
         *
         * **/
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 0,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-653770</remainpoint>\\n <taskID>81905343</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                "}";

        //将json格式字符串转化为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获得通信标识
        String code = jsonObject.getString("code");
        //判断是否符合通信标识
        if(StringUtils.equals("10000",code)){
            //获取xml格式
            String resultxml = jsonObject.getString("result");
            //根据dom4j解析xml格式字符串
            //
            Document document = DocumentHelper.parseText(resultxml);
            //获取节点对象
            Node node = document.selectSingleNode("/returnsms/returnstatus");
            //获取当前节点的文本内容
            String returnstatus = node.getText();
            //判断节点内容是否符合
            if(StringUtils.equals("Success",returnstatus)){
                redisServer.put(phone,messageCode);
                map.put(Constants.ERROR_MESSAGE,Constants.OK);
                map.put(Constants.MESSAGE_CODE,messageCode);

            }else{
                map.put(Constants.ERROR_MESSAGE,"内容失败");
                return map;
            }

        }else{
            map.put(Constants.ERROR_MESSAGE,"通信失败");
            return map;
        }
        return map;



    }
    private String getRandomCode(int count){
        String arr[]={"0","1","2","3","4","5","6","7","8","9"};
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<count;i++){
            int index = (int) Math.round(Math.random()*9);
            stringBuilder.append(arr[index]);

        }
        return stringBuilder.toString();
    }
    @RequestMapping(value="/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model){
        //获取用户信息
        User sessionUser = (User)request.getSession().getAttribute(Constants.SESSION_USER);



        //根据用户标识获取账户资金信息
           FinanceAccount financeAccount = financeAccountService.queryFinAccountById(sessionUser.getId());
           //准备需要的参数
            Map<String,Object> map = new HashMap<>();
            map.put("uid", sessionUser.getId());
            map.put("currentPage",0);
            map.put("pageSize",5);
        //根据用户标识获取投资记录 显示第一页，每页显示5条
        List<BidInfo> list = bidInforService.queryBidInfoListByUid(map);






        //根据用户标识获取充值记录






        //根据用户标识获取用户收益记录

        model.addAttribute("financeAccount",financeAccount);
        model.addAttribute("list",list);



        return "myCenter";
    }



}
