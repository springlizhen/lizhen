/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 测试数据DAO接口
 * @author ThinkGem
 * @version 2018-04-22
 */
public interface OAFileDao extends JpaRepository<OAFile, String>, JpaSpecificationExecutor<OAFile> {
	
}