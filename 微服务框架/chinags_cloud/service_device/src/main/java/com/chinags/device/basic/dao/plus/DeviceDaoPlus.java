package com.chinags.device.basic.dao.plus;

import com.chinags.device.basic.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceDaoPlus{

    Page<Device> findAllPage(@Param("device") Device device, Pageable pageable);

    List<Device> findListByCondition(@Param("device") Device device);
}
