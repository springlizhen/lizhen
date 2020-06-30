package com.chinags.auth.service;

import com.chinags.auth.dao.CommDao;
import com.chinags.auth.dao.MsgInnerDao;
import com.chinags.auth.dao.MsgInnerRecordDao;
import com.chinags.auth.dao.SysUserDao;
import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.MsgInner;
import com.chinags.auth.entity.MsgInnerRecord;
import com.chinags.auth.entity.system.Message;
import com.chinags.common.entity.PageResult;
import com.chinags.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class MsgInnerService {
    @Autowired
    private MsgInnerDao msgInnerDao;

    @Autowired
    private MsgInnerRecordService msgInnerRecordService;

    @Autowired
    private MsgInnerRecordDao msgInnerRecordDao;

    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<MsgInner> findAll() {
        return PageResult.getPageResult(msgInnerDao.findAll());
    }

    /**
     * 查询全部列表
     * @return
     */
    public List<MsgInner> findAll(MsgInner msgInner){
        Example<MsgInner> ex = Example.of(msgInner);
        return msgInnerDao.findAll(ex);
    }

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<MsgInner> find(MsgInner msgInner, String id) {
        List<String> msgInnerId = null;
        if(StringUtils.isNotEmpty(id)) {
            msgInnerId = msgInnerRecordDao.getMsgInnerId(id);
        }
        PageRequest pageable;
        if(msgInner.getOrderBy()==null||msgInner.getOrderBy().equals("")) {
            pageable = PageRequest.of(msgInner.getPageNo(), msgInner.getPageSize());
        } else{
            pageable = PageRequest.of(msgInner.getPageNo(), msgInner.getPageSize(), msgInner.getDesc(), msgInner.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        List<String> finalMsgInnerId = msgInnerId;
        PageResult msgInners =
                PageResult.getPageResult(msgInnerDao.findAll(
                        (Specification<MsgInner>) (root, query, cb) -> {
                            List<Predicate> predicates = new ArrayList<>();
                            if (finalMsgInnerId != null && finalMsgInnerId.size()>0) {
                                CriteriaBuilder.In<Object> id1 = cb.in(root.get("id"));
                                for (String ids : finalMsgInnerId){
                                    id1.value(ids);
                                }
                                predicates.add(id1);
                            }
                            Predicate[] pre = new Predicate[predicates.size()];
                            query.where(predicates.toArray(pre));
                            return cb.and(predicates.toArray(pre));
                        },pageable));
        return msgInners;
    }

    /**
     * 获得用户信息 by loginCode
     * @param id
     * @return
     */
    public MsgInner getById(String id){
        return msgInnerDao.getById(id);
    }

    public void save(MsgInner msgInner) {
        msgInnerDao.save(msgInner);
    }

    public void delete(String id) {
        msgInnerDao.deleteById(id);
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveMsg(Message message, String code, String name) {
        String[] userCode = message.getReceives().split(",");
        String userCodeName = sysUserDao.getUserCodeName(userCode);
        MsgInner msgInner = new MsgInner();
        if(StringUtils.isNotEmpty(message.getMsgid())){
            msgInner.setId(message.getMsgid());
        }else{
            msgInner.setId(UUID.randomUUID().toString());
        }
        msgInner.setMsgTitle(message.getTitle());
        msgInner.setContentLevel((message.getType()+1)+"");
//        msgInner.setMsgContent("<!--HTML--><p>"+message.getContent()+"<p>");
        msgInner.setMsgContent(message.getContent());
        msgInner.setReceiveType("1");
        msgInner.setContentType("4");
        msgInner.setReceiveCodes(message.getReceives());
        msgInner.setReceiveNames(userCodeName);
        msgInner.setSendDate(message.getSenddate());
        msgInner.setCreateBy(name);
        msgInner.setUpdateBy(name);
        msgInner.setSendUserName("系统提示");
        msgInner.setStatus("0");
        save(msgInner);
        String[] userCodeNames = userCodeName.split(",");
        for (int i=0;i< userCode.length;i++){
            MsgInnerRecord r = new MsgInnerRecord();
            r.setMsgInnerId(msgInner.getId());
            r.setReceiveUserCode(userCode[i]);
            r.setReceiveUserName(userCodeNames[i]);
            r.setReadStatus(MsgInnerRecord.READ_STATUS_UNREAD);
            msgInnerRecordService.save(r);
        }
    }
}
