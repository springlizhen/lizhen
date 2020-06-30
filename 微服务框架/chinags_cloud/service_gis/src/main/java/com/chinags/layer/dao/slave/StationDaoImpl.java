package com.chinags.layer.dao.slave;

import com.chinags.common.utils.StringUtils;
import com.chinags.layer.entity.slave.Precipitation;
import com.chinags.layer.entity.slave.Stbprp;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class StationDaoImpl implements StationDaoPlus {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Stbprp> selectStation(Stbprp stbprp, Pageable pageable) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder dataSql = new StringBuilder("SELECT * FROM c##moni.dwd_b_hyd_stbprp_pp where 1=1");
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM c##moni.dwd_b_hyd_stbprp_pp where 1=1");


        if (StringUtils.isNotEmpty(stbprp.getDtmnm())) {
            dataSql.append(" and dtmnm =:checkCenter ");
            countSql.append(" and dtmnm =:checkCenter ");

        }
        if (StringUtils.isNotEmpty(stbprp.getStnm())) {
            dataSql.append(" and stnm=:manageStation");
            countSql.append(" and stnm=:manageStation");

        }
        if (StringUtils.isNotEmpty(stbprp.getRvnm())) {
            dataSql.append(" and rvnm=:manageOffice");
            countSql.append(" and rvnm=:manageOffice");

        }


        //组装sql语句


        //创建本地sql查询实例
        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        dataQuery.setFirstResult((pageable.getPageSize() - 1) * pageable.getPageNumber());
        dataQuery.setMaxResults(pageable.getPageSize());

        dataQuery.unwrap(org.hibernate.Query.class)

                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Query countQuery = entityManager.createNativeQuery(countSql.toString());


        //设置参数
        if (StringUtils.isNotEmpty(stbprp.getDtmnm())) {
            dataQuery.setParameter("checkCenter", stbprp.getDtmnm());
            countQuery.setParameter("checkCenter", stbprp.getDtmnm());
        }
        if (StringUtils.isNotEmpty(stbprp.getStnm())) {
            dataQuery.setParameter("manageStation", stbprp.getStnm());
            countQuery.setParameter("manageStation", stbprp.getStnm());

        }
        if (StringUtils.isNotEmpty(stbprp.getRvnm())) {
            dataQuery.setParameter("manageOffice", stbprp.getRvnm());
            countQuery.setParameter("manageOffice", stbprp.getRvnm());


        }


        //设置分页
        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        Long total = count.longValue();
        List<Stbprp> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<Stbprp>emptyList();
        return new PageImpl<>(content2, pageable, total);
    }

    @Override
    public Page<Precipitation> selectPrecipitation(Precipitation prec, PageRequest pageable) {
        StringBuilder dataSql = new StringBuilder("SELECT\n" +
                "\ts.stnm,\n" +
                "\ts.lgtd,\n" +
                "\ts.lttd,\n" +
                "\tt.*\n" +
                "FROM\n" +
                "\tc##moni.dwd_d_hyd_pptn_r t\n" +
                "\tJOIN ( SELECT stcd, max( tm ) tm FROM c##moni.dwd_d_hyd_pptn_r GROUP BY stcd ) a ON t.stcd = a.stcd\n" +
                "\tLEFT JOIN c##moni.dwd_b_hyd_stbprp_pp s ON t.stcd = s.stcd where 1=1");

        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM (SELECT\n" +
                "\ts.stnm,\n" +
                "\ts.lgtd,\n" +
                "\ts.lttd,\n" +
                "\tt.*\n" +
                "FROM\n" +
                "\tc##moni.dwd_d_hyd_pptn_r t\n" +
                "\tJOIN ( SELECT stcd, max( tm ) tm FROM c##moni.dwd_d_hyd_pptn_r GROUP BY stcd ) a ON t.stcd = a.stcd\n" +
                "\tLEFT JOIN c##moni.dwd_b_hyd_stbprp_pp s ON t.stcd = s.stcd) WHERE 1=1");

        if (StringUtils.isNotEmpty(prec.getStnm())) {
            dataSql.append(" and stnm =:checkCenter ");
            countSql.append(" and stnm =:checkCenter ");
        }

        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
        dataQuery.setFirstResult((pageable.getPageSize() - 1) * pageable.getPageNumber());
        dataQuery.setMaxResults(pageable.getPageSize());

        dataQuery.unwrap(org.hibernate.Query.class)

                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        if (StringUtils.isNotEmpty(prec.getStnm())) {
            dataQuery.setParameter("checkCenter", prec.getStnm());
            countQuery.setParameter("checkCenter", prec.getStnm());
        }

        BigDecimal count = (BigDecimal) countQuery.getSingleResult();
        Long total = count.longValue();
        List<Precipitation> content2 = total > pageable.getOffset() ? dataQuery.getResultList() : Collections.<Precipitation>emptyList();
        return new PageImpl<>(content2, pageable, total);
    }


}
