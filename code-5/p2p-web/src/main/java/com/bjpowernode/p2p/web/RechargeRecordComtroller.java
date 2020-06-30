package com.bjpowernode.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.http.HttpClientUtils;
import com.bjpowernode.p2p.common.contants.Constants;
import com.bjpowernode.p2p.common.util.DateUtils;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.RedisServer;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName：RechargeRecordComtroller
 * Package:com.bjpowernode.p2p.web
 * Description:
 *
 * @date:2019/3/2 11:49
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class RechargeRecordComtroller {
    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private RedisServer redisServer;

    @RequestMapping(value = "/loan/toalipayRecharge")
    public String toalipayRecharge(HttpServletRequest request, Model model,
                                   @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) {
        //从session获取用户
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //获取用户唯一标识（时间戳+redis全局唯一数字）
        String rechargeNo = DateUtils.getTimeStamp() + redisServer.getOnlyNumber();
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("支付宝充值");
        rechargeRecord.setRechargeNo(rechargeNo);
        int addRecharge = rechargeRecordService.addRechargeCount(rechargeRecord);
        if (addRecharge > 0) {
            model.addAttribute("p2p_par_alipay_url","http://localhost:9091/par/api/alipay");
            model.addAttribute("rechargeNo",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("subject","支付宝充值");
            model.addAttribute("body","支付宝充值");

        } else {
            model.addAttribute("trade_msg", "充值人数过多，请稍后重试");


        }
        return "p2pToPayAlipay";
    }
    @RequestMapping(value="/loan/alipayBack")
    public String alipayBack(HttpServletRequest request,Model model,
                                @RequestParam(value = "out_trade_no ",required = true) String out_trade_no ,
                                    @RequestParam(value = "total_amount " ,required = true) Double total_amount ,
                             @RequestParam(value = "signVerified" ,required = true) String signVerifie) throws Exception {
        //验证签名
        if(StringUtils.equals(Constants.SUCCESS,signVerifie)){
            Map<String,Object> map = new HashMap<>();
            map.put("out_trade_on",out_trade_no);
            //调用pay工程的查询接口，返回的是json的格式字符串
            String jsonString  = HttpClientUtils.doPost("http://localhost:9091/par/api/alipayQuery",map);
            //将json字符串转换为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            //获取json对象的属性值
            JSONObject parset = jsonObject.getJSONObject("alipay_trade_query_response");
            //获取通信标识
            String code  = parset.getString("code");
            //判断通信是否成功
            if(StringUtils.equals("10000",code)){
                //获取交易状态

                String tradestatus = parset.getString("tarade_status");

                if(StringUtils.equals("TRADE_CLOSED",tradestatus)){
                    RechargeRecord rechargeRecord = new RechargeRecord();
                    rechargeRecord.setRechargeNo(out_trade_no);
                    rechargeRecord.setRechargeStatus("2");
                    int modifyrechargeCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(rechargeRecord);
                    if(modifyrechargeCount<=0){
                        model.addAttribute("trade_msg","充值失败，请稍后再试");
                        return "toRechargeBack";
                    }
                }
                if(StringUtils.equals("TRADE_SUCCESS",tradestatus)){
                    //更新用户可用余额,更新交易状态
                    User sessionUser = (User)request.getSession().getAttribute(Constants.SESSION_USER);
                    map.put("rechargeNo",out_trade_no);
                    map.put("rechargeMoney",total_amount);
                    map.put("uid",sessionUser.getId());
                    int rechargeCount = rechargeRecordService.recharge(map);
                    if(rechargeCount<=0){
                        model.addAttribute("trade_msg","用户充值失败");
                        return  "toRechargeBack";
                    }

                }

            }else{
                model.addAttribute("trade_msg","通信失败");
                return  "toRechargeBack";
            }

        }else{
            model.addAttribute("trade_msg","验证签名失败");
            return  "toRechargeBack";
        }
        return "redirect:/loan/myCenter";

    }
    @RequestMapping(value="/loan/towxpayRecharge")
    public String towxpayRecharge(HttpServletRequest request,Model model,
                                  @RequestParam(value = "rechargeMoney",required = true) Double rechargeMoney){
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //获取用户唯一标识（时间戳+redis全局唯一数字）
        String rechargeNo = DateUtils.getTimeStamp() + redisServer.getOnlyNumber();
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeStatus("0");
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("微信充值");
        rechargeRecord.setRechargeNo(rechargeNo);
        int addRecharge = rechargeRecordService.addRechargeCount(rechargeRecord);
        if(addRecharge>0){
            model.addAttribute("rechargeNo",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("rechargeTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        }else{
            model.addAttribute("trade_msg","充值人数过多请稍后重试");
            return "toRechargeBack";
        }
        return "showQRcode";
    }
    @RequestMapping(value = "/loan/generateQRcode")
    public void generateQRcode(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
                               @RequestParam(value = "total_fee",required = true) String total_fee) throws Exception {
        //准备支付的参数
        Map<String,Object> map2  = new HashMap<>();
        map2.put("body","微信充值");
        map2.put("out_trade_no",out_trade_no);
        map2.put("total_fee",total_fee);
        //调用pay工程下单的接口返回url
        String jsonString = HttpClientUtils.doPost("http://localhost:9091/par/api/wxpay",map2);
        //解析json字符串格式
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获取通信标识
        String returncode = jsonObject.getString("return_code");
        if(StringUtils.equals(Constants.SUCCESS,returncode)){
            //获取业务处理结果
            String resultcode =jsonObject.getString("result_code");
            if(StringUtils.equals(Constants.SUCCESS,resultcode)){
                //获取微信支付的连接
                String codeurl = jsonObject.getString("code_url");
                Map<EncodeHintType,Object> map = new HashMap<>();
                map.put(EncodeHintType.CHARACTER_SET,"utf-8");
                //创建一个矩阵对象
                BitMatrix bitMatrix = new MultiFormatWriter().encode(codeurl, BarcodeFormat.QR_CODE,200,200,map);
                //获取输出流对象
                OutputStream outputStream  = response.getOutputStream();
                //将矩阵对象转换为二维码格式
                MatrixToImageWriter.writeToStream(bitMatrix,"png",outputStream);
                outputStream.flush();
                outputStream.close();

            }else{
                response.sendRedirect(request.getContextPath()+"toRechargeBack.jsp");
            }
        }else{
            response.sendRedirect(request.getContextPath()+"toRechargeBack.jsp");
        }


    }

}
