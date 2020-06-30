package com.chinags.file.service;

import com.chinags.file.dao.HzQzDao;
import com.chinags.file.entity.HzQz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 慧正签章接口
 * @Author : Mark_wang
 * @Date : 2020-2-17
 **/
@Service
public class HzQzService {

    @Autowired
    private HzQzDao hzQzDao;

    /**
     * 保存
     * @param hzQz
     * @return
     */
    public HzQz save(HzQz hzQz) {
        return hzQzDao.save(hzQz);
    }

    /**
     * 根据慧正id查询慧正签章数据
     * @param hzId
     * @return
     */
    public HzQz selectByHzId(String hzId) {
        return hzQzDao.selectByHzId(hzId);
    }
}
