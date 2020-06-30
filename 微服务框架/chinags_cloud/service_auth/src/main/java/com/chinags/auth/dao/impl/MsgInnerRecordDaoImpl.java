package com.chinags.auth.dao.impl;

import com.chinags.auth.dao.plus.MsgInnerRecordDaoPlus;
import com.chinags.auth.entity.system.Message;
import com.chinags.common.utils.StringUtils;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

public class MsgInnerRecordDaoImpl implements MsgInnerRecordDaoPlus {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Map<String, Object>> selectMsgListByCondition(String usercode, String msgType, Integer pageNo, Integer pageSize) {
        StringBuilder dataSql = new StringBuilder("select m.MSG_TITLE title, to_char(m.MSG_CONTENT) content, m.CONTENT_TYPE type, m.SEND_DATE senddate, m.ID msgid, mr.READ_STATUS readstatus, mr.RECEIVE_USER_CODE usercode " +
                "from T_PUB_SYS_MSG_INNER_RECORD mr left join T_PUB_SYS_MSG_INNER m on mr.MSG_INNER_ID = m.ID");
        StringBuilder countSql = new StringBuilder("SELECT count(mr.ID) from T_PUB_SYS_MSG_INNER_RECORD mr left join T_PUB_SYS_MSG_INNER m on mr.MSG_INNER_ID = m.ID");

        //拼接where条件
        StringBuilder whereSql = new StringBuilder(" WHERE 1 = 1");
        if (StringUtils.isNotEmpty(usercode)) {
            whereSql.append(" AND mr.RECEIVE_USER_CODE = :usercode");
        }
        if (StringUtils.isNotEmpty(msgType)) {
            whereSql.append(" AND m.CONTENT_TYPE = :msgType");
        }
        dataSql.append(whereSql);
        countSql.append(whereSql);

        //设置排序
        dataSql.append(" order by m.SEND_DATE desc");

        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        //设置分页
        dataQuery.setFirstResult((pageNo - 1) * pageSize);
        dataQuery.setMaxResults(pageSize);

        //设置参数
        if (StringUtils.isNotEmpty(usercode)) {
            dataQuery.setParameter("usercode", usercode);
            countQuery.setParameter("usercode", usercode);
        }
        if (StringUtils.isNotEmpty(msgType)) {
            dataQuery.setParameter("msgType", msgType);
            countQuery.setParameter("msgType", msgType);
        }

        //返回数据
        List rows = dataQuery.getResultList();
        List<Map<String, Object>> content = new ArrayList<>();
        for (Object row : rows) {
            Object[] cells = (Object[]) row;
            Map<String, Object> data = new HashMap<>(1);
            data.put("title", cells[0]);
            data.put("content", cells[1]);
            data.put("msgType", cells[2]);
            data.put("sendDate", cells[3]);
            data.put("msgId", cells[4]);
            data.put("readStatus", cells[5]);
            data.put("userCode", cells[6]);
            content.add(data);
        }
        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        long total = count.longValue();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return new PageImpl<>(content, pageable, total);
    }
}
