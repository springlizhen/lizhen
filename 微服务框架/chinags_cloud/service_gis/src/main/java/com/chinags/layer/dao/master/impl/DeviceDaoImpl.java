package com.chinags.layer.dao.master.impl;

import com.chinags.common.utils.StringUtils;
import com.chinags.layer.dao.master.plus.DeviceDaoPlus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeviceDaoImpl implements DeviceDaoPlus {
    @PersistenceContext
    private EntityManager entityManager;




}
