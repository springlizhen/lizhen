package com.chinags.device.basic.dao;

import com.chinags.device.basic.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SensorDao extends JpaRepository<Sensor, String>, JpaSpecificationExecutor<Sensor> {
    public Sensor getSensorById(String id);

}
