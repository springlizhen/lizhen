package com.chinags.layer.dao.slave;

import com.chinags.layer.entity.master.Device;
import com.chinags.layer.entity.slave.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface StationDao extends JpaRepository<Station, String>, JpaSpecificationExecutor<Station> {
    @Query(value = "select a.*,t.device_name,t.ORG_ID from t_enm_device t left join (select a.mn,a.value,name from msg_point_value a  join (select max(qn) qn,mn from msg_point_value group by mn) t on t.mn=a.mn and t.qn=a.qn  left join msg_point_code d on a.code=d.code where type='Rtd') pivot (max(value) for name in('pH传感器' pH传感器,'氨氮' 氨氮,'苯系物' 苯系物,'电导率传感器' 电导率传感器,'氟化物（以Fˉ计）' 氟化物,'高锰酸盐指数' 高锰酸盐指数,'化学需氧量(COD)' 化学需氧量,'硫化物' 硫化物,'硫酸盐分析仪' 硫酸盐分析仪,'氯化物' 氯化物,'溶解氧传感器' 溶解氧传感器,'生物毒性检测仪' 生物毒性检测仪,'温度测量' 温度测量,'硝氮' 硝氮,'叶绿素' 叶绿素,'浊度传感器' 浊度传感器,'总硬度' 总硬度,'总有机碳(TOC)' 总有机碳)) a  on t.device_number=a.mn where device_class='SZJCZ' and t.ORG_ID =:orgId", nativeQuery = true)
    public List<Map<String,Object>> selectAppMn(String orgId);
    @Query(value = "select rain,count(distinct stcd),tm stcd from (select case when t.drp<10 then '小雨'  when drp>10 and drp<24.9 then '中雨' when drp>25 and drp<49.9 then '大雨' when drp>50 then '暴雨' when drp>250 then '特大暴雨' end rain,t.stcd,s.lgtd,s.lttd,a.tm from dwd_d_hyd_pptn_r t join (select stcd,max(tm) tm from dwd_d_hyd_pptn_r group by stcd) a on t.stcd=a.stcd left join dwd_b_hyd_stbprp_pp s on t.stcd=s.stcd) group by rain,tm ", nativeQuery = true)
    public List<Map<String,Object>> selectStcd ();
    @Query(value = "select * from (select a.mn,a.value,name,a.qn,a.code from msg_point_value a  join (select max(qn) qn,mn from msg_point_value group by mn) t on t.mn=a.mn and t.qn=a.qn  left join msg_point_code d on a.code=d.code) where mn=:mn", nativeQuery = true)
    public List<Map<String,Object>> selectAppStation(String mn);



/*    @Query(value = "select t.drp,s.stnm,t.stcd,s.lgtd,s.lttd from dwd_d_hyd_pptn_r t join (select stcd,max(tm) tm from dwd_d_hyd_pptn_r group by stcd) a on t.stcd=a.stcd left join dwd_b_hyd_stbprp_pp s on t.stcd=s.stcd", nativeQuery = true)
    public List<Map<String,Object>> selectPrecipitation();

    @Query(value = "select t.drp,s.stnm,t.stcd,s.lgtd,s.lttd from dwd_d_hyd_pptn_r t join (select stcd,max(tm) tm from dwd_d_hyd_pptn_r group by stcd) a on t.stcd=a.stcd left join dwd_b_hyd_stbprp_pp s on t.stcd=s.stcd where s.stnm=:stnm ", nativeQuery = true)
    public List<Map<String,Object>> selectPrecipitationByStnm(String stnm);*/


    @Query(value = "select * from (select a.mn,a.value,name from msg_point_value a  join (select max(qn) qn,mn from msg_point_value group by mn) t on t.mn=a.mn and t.qn=a.qn  left join msg_point_code d on a.code=d.code where a.mn=:mn and type='Rtd') pivot (max(value) for name in('pH传感器' pH传感器,'氨氮' 氨氮,'苯系物' 苯系物,'电导率传感器' 电导率传感器,'氟化物（以Fˉ计）' 氟化物,'高锰酸盐指数' 高锰酸盐指数,'化学需氧量(COD)' 化学需氧量,'硫化物' 硫化物,'硫酸盐分析仪' 硫酸盐分析仪,'氯化物' 氯化物,'溶解氧传感器' 溶解氧传感器,'生物毒性检测仪' 生物毒性检测仪,'温度测量' 温度测量,'硝氮' 硝氮,'叶绿素' 叶绿素,'浊度传感器' 浊度传感器,'总硬度' 总硬度,'总有机碳(TOC)' 总有机碳))", nativeQuery = true)
    public List<Map<String,Object>> selectStation(String mn);
}
