package com.chinags.auth.dao;

import com.chinags.auth.entity.Contract;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface ContractDao extends JpaRepository<Contract, String>, JpaSpecificationExecutor<Contract> {
    @Query(value = "select * from t_coa_contract where id=:id", nativeQuery = true)
    public Contract getContractById(String id);
    public void deleteContractById(String id);
    @Query(value = "select title,id from t_coa_contract ", nativeQuery = true)
    public List<Map<String,Object>> selectSubContract();
    @Query(value = "select a.*,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST where (AUTH_ID=:logincode or AGENT_ID=:logincode) and ISACTIVE='1' and STATUS_NO<200) b on a.WORK_ID = b.WORKID order by b.SENDTIME desc", nativeQuery = true)
    List<Map<String,Object>> selectget(String logincode);
    @Query(value = "select a.*,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST_DONE where (AUTH_ID=:logincode or AGENT_ID=:logincode) and STATUS_NO>200 and STATUS_NO<300) b on a.WORK_ID = b.WORKID order by b.SENDTIME desc", nativeQuery = true)
    List<Map<String,Object>> selectgetTo(String logincode);
    @Query(value = "select a.*,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID FROM T_COA_CONTRACT_MTER a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST where (AUTH_ID=:logincode or AGENT_ID=:logincode) and ISACTIVE='1' and STATUS_NO<200) b on a.WORK_ID = b.WORKID order by b.SENDTIME desc", nativeQuery = true)
    List<Map<String, Object>> selectgetToLb(String logincode);
    @Query(value = "select a.*,b.TITLE,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID FROM T_COA_CONTRACT_MTER a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST_DONE where (AUTH_ID=:logincode or AGENT_ID=:logincode) and STATUS_NO>200 and STATUS_NO<300) b on a.WORK_ID = b.WORKID order by b.SENDTIME desc", nativeQuery = true)
    List<Map<String, Object>> selectToYu(String logincode);
    @Query(value = "select a.Time,a.ID,a.CONTRACTNAME,a.TITLE,a.CONTRACT_CODE,a.MONEY,a.CUSTOMER,a.CUSTOMERPERSON,a.WORK_ID,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID FROM T_COA_CONTRACT a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST) b on a.WORK_ID = b.WORKID order by a.Time desc", nativeQuery = true)
    List<Map<String, Object>> selectZl();
    @Query(value = "select a.Time,a.WORK_ID,b.FLOWNAME,b.SENDUSERNAME,b.SENDTIME,b.TRACKID,b.SUBJECTION_TYPE,b.SENDSUBJECTIONID,d.CONTRACTNAME,d.CONTRACT_CODE,d.MONEY,d.CUSTOMER,d.CUSTOMERPERSON FROM T_COA_CONTRACT_MTER a inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST) b on a.WORK_ID = b.WORKID \n" +
            "inner join T_COA_CONTRACT d on d.ID = a.CONTRACTID ORDER BY a.Time desc\n", nativeQuery = true)
    List<Map<String, Object>> selectZlK();
    @Query(value = "select a.Time,a.PAYTERM,a.PAYAMOUNTNAME,a.INPUTAMOUNT,a.PAYAMOUNT,a.WORK_ID,b.SENDTIME,b.SENDSUBJECTIONID,b.TRACKID,b.SUBJECTION_TYPE,case CAST(d.FLOWSTATUS AS CHAR(10))\n" +
            " WHEN '100' THEN '启动'\n" +
            "   WHEN '110' THEN '正常'\n" +
            "   WHEN '120' THEN '会签'\n" +
            "   WHEN '130' THEN '退回'\n" +
            "   WHEN '190' THEN '拿回'\n" +
            "   WHEN '160' THEN '子流程'\n" +
            "   WHEN '170' THEN '结束'\n" +
            "   WHEN '180' THEN '终止'\n" +
            "   WHEN '140' THEN '暂停'\n" +
            "   WHEN '150' THEN '撤办'\n" +
            "\t end\n" +
            "\t AS status FROM T_COA_CONTRACT_MTER a  inner join(select TITLE,FLOWNAME,SENDUSERNAME,SENDTIME,WORKID,TRACKID,SUBJECTION_TYPE,SUBJECTION_ID,SENDSUBJECTIONID  from HORIZON.TW_HZ_WORKLIST ) b on a.WORK_ID = b.WORKID  inner join  HORIZON.TW_HZ_TRACK d on a.WORK_ID = d.WORKID WHERE a.CONTRACTID=:contractid order by a.Time desc", nativeQuery = true)
    List<Map<String, Object>> selectZlKU(String contractid);

    @Query(value = "select b.WORKID,a.FLOWID,b.sendtime,a.CREATOR from HORIZON.TW_HZ_INSTANCE a left join HORIZON.TW_HZ_WORKLIST b on a.id = b.WORKID where a.CREATOR =:logincode and a.flowid in ('hetongdengji','hetongjiliang') and rownum <= 5   ORDER BY b.sendtime desc \n", nativeQuery = true)
    List<Map<String, Object>> selectUi(String logincode);
    @Query(value = "SELECT a.* from T_COA_CONTRACT a inner join T_COA_CONTRACT_MTER b on a.ID = b.CONTRACTID where b.WORK_ID =:workid", nativeQuery = true)
    List<Contract> selectBy(String workid);
    @Query(value = "SELECT * from T_COA_CONTRACT where WORK_ID =:workid", nativeQuery = true)
    List<Contract> selectByworkId(String workid);
    @Query(value = "select b.WORKID,a.FLOWID,b.sendtime,a.CREATOR from HORIZON.TW_HZ_INSTANCE a left join HORIZON.TW_HZ_WORKLIST b on a.id = b.WORKID where (b.AUTH_ID=:logincode or b.AGENT_ID=:logincode) and b.ISACTIVE='1' and b.STATUS_NO<200 and a.CREATOR =:logincode and a.flowid in ('hetongdengji','hetongjiliang') and rownum <= 5   ORDER BY b.sendtime desc \n", nativeQuery = true)
    List<Map<String, Object>> selectUiTo(String logincode);
    @Query(value = "select b.WORKID,a.FLOWID,b.sendtime,a.CREATOR from HORIZON.TW_HZ_INSTANCE a left join HORIZON.TW_HZ_WORKLIST_DONE b on a.id = b.WORKID where (b.AUTH_ID=:logincode or b.AGENT_ID=:logincode) and STATUS_NO>200 and STATUS_NO<300 and a.CREATOR =:logincode and a.flowid in ('hetongdengji','hetongjiliang') and rownum <= 5   ORDER BY b.sendtime desc \n", nativeQuery = true)
    List<Map<String, Object>> selectUiToK(String logincode);
    @Query(value = "select CUSTOMER from T_COA_CONTRACT  GROUP BY CUSTOMER", nativeQuery = true)
    List<Map<String,Object>> selectwu();
    @Query(value = "select COUNT(1) as count,CUSTOMER\n" +
            "from T_COA_CONTRACT group by  CUSTOMER ORDER by  count(1) desc", nativeQuery = true)
    List<Map<String, Object>> selectUiToKbZ();
    @Query(value = "select * from T_COA_CONTRACT where customer=:customer", nativeQuery = true)
    List<Contract> getmoney(String customer);
    @Query(value = "select * from T_COA_CONTRACT", nativeQuery = true)
    List<Contract> selectAll();
    @Query(value = "select count(1),CONTRACT_TYPE from T_COA_CONTRACT group by CONTRACT_TYPE", nativeQuery = true)
    List<Contract> getType();
}
