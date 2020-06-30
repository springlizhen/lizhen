package com.chinags.auth.dao;

import com.chinags.auth.dao.plus.MsgInnerRecordDaoPlus;
import com.chinags.auth.entity.MsgInnerRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface MsgInnerRecordDao extends JpaRepository<MsgInnerRecord, String>, JpaSpecificationExecutor<MsgInnerRecord>, MsgInnerRecordDaoPlus {

    public MsgInnerRecord getById(String id);

    @Query(value = "select distinct(msgInnerId) from MsgInnerRecord where receiveUserCode=:userCode")
    List<String> getMsgInnerId(String userCode);

    @Modifying
    @Transactional
    @Query(value = "update T_PUB_SYS_MSG_INNER_RECORD set READ_STATUS = '1', READ_DATE = systimestamp where MSG_INNER_ID = :msgId and RECEIVE_USER_CODE = :usercode", nativeQuery = true)
    void updateMsgReadStatus(String usercode, String msgId);

    @Query(value = "select count(id) from T_PUB_SYS_MSG_INNER_RECORD where RECEIVE_USER_CODE = :usercode and READ_STATUS = '2'", nativeQuery = true)
    Long selectUnReadMsgCount(String usercode);
}
