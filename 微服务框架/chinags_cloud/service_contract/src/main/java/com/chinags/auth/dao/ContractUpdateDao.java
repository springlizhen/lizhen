package com.chinags.auth.dao;

import com.chinags.auth.entity.Contract;
import com.chinags.auth.entity.ContractUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface ContractUpdateDao extends JpaRepository<ContractUpdate, String>, JpaSpecificationExecutor<ContractUpdate> {
    @Query(value = "select * from t_coa_contract_update where id=:id", nativeQuery = true)
    public  ContractUpdate getUpdateContractById(String id);
    @Query(value = "select t.* from (select contract_id,max(t.update_date) createdate from T_COA_CONTRACT_UPDATE t \n" +
            "group by contract_id) a left join  T_COA_CONTRACT_UPDATE t  on t.contract_id= a.contract_id and t.update_date=a.createdate where t.work_id=:workId", nativeQuery = true)
    public  ContractUpdate getMaxUpdateContractById(String workId);
    @Query(value = "select t.* from (select contract_id,max(t.update_date) createdate from T_COA_CONTRACT_UPDATE t \n" +
            "group by contract_id) a left join  T_COA_CONTRACT_UPDATE t  on t.contract_id= a.contract_id and t.update_date=a.createdate where a.contract_id=:id", nativeQuery = true)
    public  ContractUpdate getMaxUpdateContract(String id);

}
