/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试数据Service
 * @author ThinkGem
 * @version 2018-04-22
 */
@Service
public class OAFileService {

    @Autowired
    private OAFileDao oaFileDao;


    public OAFile get(String id) {

        return oaFileDao.getOne(id);
    }
}