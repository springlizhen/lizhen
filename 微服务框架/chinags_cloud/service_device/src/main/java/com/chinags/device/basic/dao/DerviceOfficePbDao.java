package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.DerviceOfficePb;
import com.chinags.device.basic.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DerviceOfficePbDao extends JpaRepository<DerviceOfficePb, String>, JpaSpecificationExecutor<DerviceOfficePb> {
    public DerviceOfficePb getDerviceOfficePbById(String id);

}
