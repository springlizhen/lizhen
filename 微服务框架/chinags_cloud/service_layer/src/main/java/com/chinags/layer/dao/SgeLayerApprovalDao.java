package com.chinags.layer.dao;

import com.chinags.layer.entity.SgeLayer;
import com.chinags.layer.entity.SgeLayerApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SgeLayerApprovalDao extends JpaRepository<SgeLayerApproval, String>, JpaSpecificationExecutor<SgeLayerApproval> {
    public SgeLayerApproval getSgeLayerApprovalById(String id);


}
