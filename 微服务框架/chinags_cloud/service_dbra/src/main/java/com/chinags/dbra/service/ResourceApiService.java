package com.chinags.dbra.service;

import com.alibaba.fastjson.JSONArray;
import com.chinags.dbra.dao.ResourceApiDao;
import com.chinags.dbra.entity.BasicData;
import com.chinags.dbra.entity.ResourceApi;
import com.chinags.dbra.util.FunctionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 资源共享表接口实现
 * @author Mark_Wang
 * @date 2019/7/1
 **/
@Service
public class ResourceApiService {

    @Autowired
    private ResourceApiDao resourceApiDao;

    /**
     * 资源共享信息保存
     * @param resourceApi
     */
    public ResourceApi save(ResourceApi resourceApi, String tableName) {
        boolean result = false;
        try {
            List<BasicData> cols = JSONArray.parseArray(resourceApi.getColumns(), BasicData.class);
            if (StringUtils.isEmpty(resourceApi.getCountSql()) || StringUtils.isEmpty(resourceApi.getAllSql())) {
                // 根据表名生成数据量统计sql语句
                String countSql = FunctionUtil.generatSelectCountSql(tableName);
                resourceApi.setCountSql(countSql);
            }
            // 根据属性和表名生成查询所有数据sql
            String allSql = FunctionUtil.generatSql(cols, tableName);
            resourceApi.setAllSql(allSql);
            if (StringUtils.isEmpty(resourceApi.getCreateDate())) {
                resourceApi.setCreateDate(new Date());
            }
            resourceApi.setUpdateDate(new Date());
            return resourceApiDao.save(resourceApi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据资源id查询资源共享信息
     * @param reId
     * @return
     */
    public ResourceApi findByReId(String reId) {
        return resourceApiDao.findByReId(reId);
    }

    /**
     * 根据id修改api host地址
     * @param apiHost
     * @param apiCountHost
     * @param id
     */
    public void updateHostById(String apiHost, String apiCountHost, String id) {
        resourceApiDao.updateHostById(apiHost, apiCountHost, id);
    }

    /**
     * 根据id增加调用次数
     * @param id
     */
    public void updateCallNumById(String id) {
        resourceApiDao.updateCallNumById(id);
    }

    /**
     * 根据Reid增加应用连接数
     * @param reId
     */
    public void updateConnNumByReId(String reId) {
        resourceApiDao.updateConnNumByReId(reId);
    }

}
