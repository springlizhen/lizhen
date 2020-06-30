package com.chinags.device.client;

import com.chinags.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("service-auth")
public interface UserClient {
    @RequestMapping(value = "/user/hello", method = RequestMethod.GET)
    public Result hello();
    @RequestMapping(value = "/office/officeList", method = RequestMethod.GET)
    public Result officeList(@RequestParam("officeLevel") String officeLevel);
    @RequestMapping(value = "/office/getofficeEndType", method = RequestMethod.GET)
    public Result getofficeEndType(@RequestParam("id") String id, @RequestParam("type")String type);
    @RequestMapping(value = "/office/officeListTo", method = RequestMethod.GET)
    public Result find(@RequestParam("userCode") String userCode);
    @RequestMapping(value = "/office/officeListToLb", method = RequestMethod.GET)
    public Result findTo(@RequestParam("officeCode") String officeCode);
    @RequestMapping(value = "/office/officeListToLbZu", method = RequestMethod.POST)
    public Result findQuery(@RequestParam("userCode") String userCode);
    @RequestMapping(value = "/office/officeListToLbZuLo", method = RequestMethod.GET)
   public Result findQueryOfficeCode(@RequestParam("officeCode") String officeCode);
    @RequestMapping(value = "/office/QueryBytreeDate", method = RequestMethod.POST)
   public Result QueryBytreeDate(@RequestParam("userCode") String userCode);
    @RequestMapping(value = "/office/toUser", method = RequestMethod.GET)
   public Result toUser(@RequestParam("userCode") String userCode);
    @RequestMapping(value = "/office/Yu", method = RequestMethod.POST)
   public Result getYu(String userCode);
    @RequestMapping(value = "/office/pid", method = RequestMethod.GET)
  public Result getPointAlarmsByPidTo(@RequestParam("sendUserName") String sendUserName);
    @RequestMapping(value = "/office/ziji", method = RequestMethod.GET)
   public Result getziji(@RequestParam("officeCode") String officeCode);
    @RequestMapping(value = "/office/shangji", method = RequestMethod.GET)
  public Result findQueryOfficeCodeP(@RequestParam("officeCode") String officeCode);
    @RequestMapping(value = "/office/officeListToLbLo", method = RequestMethod.GET)
   public Result sdd(@RequestParam("userName") String userName);
    @RequestMapping(value = "/office/officeListFenzhongxin", method = RequestMethod.GET)
   public Result officeListFenzhongxin(@RequestParam("officeName") String officeName);
}
