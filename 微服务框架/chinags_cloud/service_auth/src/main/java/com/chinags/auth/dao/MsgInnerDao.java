package com.chinags.auth.dao;

import com.chinags.auth.entity.Comm;
import com.chinags.auth.entity.MsgInner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface MsgInnerDao extends JpaRepository<MsgInner, String>, JpaSpecificationExecutor<MsgInner> {

    public MsgInner getById(String id);

    public List<MsgInner> getMsgInnersByIdIn(List<String> id);
}
