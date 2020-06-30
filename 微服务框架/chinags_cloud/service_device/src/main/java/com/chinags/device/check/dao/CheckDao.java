package com.chinags.device.check.dao;

import com.chinags.device.check.entity.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CheckDao extends JpaRepository<Check, String>, JpaSpecificationExecutor<Check> {


    public Check getCheckById(String id);

}
