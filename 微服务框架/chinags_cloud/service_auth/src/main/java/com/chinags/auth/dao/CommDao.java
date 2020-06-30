package com.chinags.auth.dao;

import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CommDao extends JpaRepository<Comm, String>, JpaSpecificationExecutor<Comm> {

    public Comm getCommById(String id);

    public Comm getCommByAuthCode(String authCode);

}
