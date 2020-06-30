package com.chinags.auth.service;

import com.chinags.auth.dao.MsgInnerRecordDao;
import com.chinags.auth.entity.MsgInnerRecord;
import com.chinags.auth.entity.system.Message;
import com.chinags.common.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MsgInnerRecordService {
    @Autowired
    private MsgInnerRecordDao msgInnerRecordDao;

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<MsgInnerRecord> findAll() {
        return PageResult.getPageResult(msgInnerRecordDao.findAll());
    }

    /**
     * 查询全部列表
     * @return
     */
    public List<MsgInnerRecord> findAll(MsgInnerRecord msgInnerRecord){
        Example<MsgInnerRecord> ex = Example.of(msgInnerRecord);
        return msgInnerRecordDao.findAll(ex);
    }

    /**
     * 查询全部列表
     * @return
     */
    public PageResult<MsgInnerRecord> find(MsgInnerRecord msgInnerRecord) {
        PageRequest pageable;
        if(msgInnerRecord.getOrderBy()==null||msgInnerRecord.getOrderBy().equals("")) {
            pageable = PageRequest.of(msgInnerRecord.getPageNo(), msgInnerRecord.getPageSize());
        } else{
            pageable = PageRequest.of(msgInnerRecord.getPageNo(), msgInnerRecord.getPageSize(), msgInnerRecord.getDesc(), msgInnerRecord.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult<MsgInnerRecord> msgInnerRecordPageResult =  PageResult.getPageResult(msgInnerRecordDao.findAll(
                new Specification<MsgInnerRecord>() {
                    @Override
                    public Predicate toPredicate(Root<MsgInnerRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        List<Predicate> predicates = new ArrayList<>();
//                        if (comm.getAuthName() != null && !comm.getAuthName().equals("")) {
//                            predicates.add(cb.like(root.get("authName").as(String.class), "%" + comm.getAuthName() + "%"));
//                        }
//                        if (comm.getAuthCode() != null && !comm.getAuthCode().equals("")) {
//                            predicates.add(cb.like(root.get("authCode").as(String.class), "%" + comm.getAuthCode() + "%"));
//                        }

                        Predicate[] pre = new Predicate[predicates.size()];
                        query.where(predicates.toArray(pre));
                        return cb.and(predicates.toArray(pre));
                    }
                },pageable));
        return msgInnerRecordPageResult;
    }

    /**
     * 获得信息
     * @param id
     * @return
     */
    public MsgInnerRecord getById(String id){
        return msgInnerRecordDao.getById(id);
    }

    public void save(MsgInnerRecord msgInnerRecord) {
        msgInnerRecordDao.save(msgInnerRecord);
    }

    public void delete(String id) {
        msgInnerRecordDao.deleteById(id);
    }

    /**
     * 查询消息列表
     * @param usercode
     * @param msgType
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Page<Map<String, Object>> selectMsgListByCondition(String usercode, String msgType, Integer pageNo, Integer pageSize) {
        return msgInnerRecordDao.selectMsgListByCondition(usercode, msgType, pageNo, pageSize);
    }

    /**
     * 更新消息为已读
     * @param usercode
     * @param msgId
     */
    public void updateMsgReadStatus(String usercode, String msgId) {
        msgInnerRecordDao.updateMsgReadStatus(usercode, msgId);
    }

    /**
     * 查询是否有未读消息
     * @param usercode
     * @return
     */
    public int selectIsUnReadMsg(String usercode) {
        Long count = msgInnerRecordDao.selectUnReadMsgCount(usercode);
        return count.intValue() > 0 ? 1 : 0;
    }
}
