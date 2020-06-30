package com.chinags.layer.dao.slave;

import com.chinags.layer.entity.slave.Precipitation;
import com.chinags.layer.entity.slave.Stbprp;
import oracle.jdbc.proxy.annotation.Pre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StationDaoPlus {
    public Page<Stbprp> selectStation(@Param("stbprp") Stbprp stbprp, Pageable pageable);


    public Page<Precipitation> selectPrecipitation(@Param("prec") Precipitation prec, PageRequest pageable);
}
