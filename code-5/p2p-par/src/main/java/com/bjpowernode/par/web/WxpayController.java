package com.bjpowernode.par.web;


import com.bjpowernode.par.util.HttpClientUtils;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName：WxpayController
 * Package:com.bjpowernode.par.web
 * Description:
 *
 * @date:2019/3/4 17:41
 * @author:guoxin@bjpowernode.com
 */
@Controller
public class WxpayController {
    @RequestMapping(value = "/api/wxpay")
    public @ResponseBody Object wxpay(HttpServletRequest request,
                 @RequestParam(value = "out_trade_no",required = true) String out_trade_no,
                 @RequestParam(value = "total_fee",required = true) String total_fee,
                 @RequestParam(value = "body" ,required = true) String body) throws Exception {
        //微信调用api接口
        //传递需要的参数
        Map<String,String> map = new HashMap<>();
        map.put("appid","wx8a3fcf509313fd74");
        map.put("out_trade_no",out_trade_no);
        map.put("body",body);
        map.put("mch_id","1361137902");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("spbill_create_ip","127.0.0.1");
        map.put("notify_url","http://localhost:9091/par/wxpayNotfiy");
        map.put("trade_type","NATIVE");
        map.put("product_id",out_trade_no);
        BigDecimal bigDecimal  = new BigDecimal(total_fee);
        BigDecimal  multiply = bigDecimal.multiply(new BigDecimal(100));
        int valueof = multiply.intValue();
        map.put("total_fee",String.valueOf(valueof));
        String sign = WXPayUtil.generateSignature(map, "367151c5fd0d50f1e34a68a802d6bbca");
        map.put("sign",sign);
        //将map集合的请求参数转换成xml的格式的请求参数
        String mapToxml = WXPayUtil.mapToXml(map);
        //将xml格式参数传递给微信指定地址
        String xml = HttpClientUtils.doPostByXml("https://api.mch.weixin.qq.com/pay/unifiedorder",mapToxml);
        //将响应的xml转换成map集合
           Map<String,String> map1 =  WXPayUtil.xmlToMap(xml);

        return  map1;


    }

}
