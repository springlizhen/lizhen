package com.chinags.auth.dao.plus;

import com.chinags.auth.entity.system.Message;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface MsgInnerRecordDaoPlus {

    Page<Map<String, Object>> selectMsgListByCondition(@RequestParam("usercode") String usercode, @RequestParam("msgType") String msgType,
                                                       @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
}
