package com.chinags.device.check.dao;

import com.chinags.device.check.entity.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StandardDao extends JpaRepository<Standard, String>, JpaSpecificationExecutor<Standard> {
    public Standard getStandardById(String id);


}
