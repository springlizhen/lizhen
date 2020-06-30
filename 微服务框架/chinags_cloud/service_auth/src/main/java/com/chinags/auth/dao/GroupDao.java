package com.chinags.auth.dao;

import com.chinags.auth.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface GroupDao extends JpaRepository<Group, String>, JpaSpecificationExecutor<Group> {

    Group getFirstById(String id);
}
