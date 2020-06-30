package com.chinags.device.check.service;

import com.chinags.common.entity.PageResult;
import com.chinags.common.service.BaseService;

import com.chinags.device.check.dao.CheckDao;
import com.chinags.device.check.dao.CheckNoteDao;
import com.chinags.device.check.dao.plus.CheckNoteDaoPlus;
import com.chinags.device.check.entity.Check;
import com.chinags.device.check.entity.CheckNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class CheckNoteService extends BaseService<CheckNote>{

    @Autowired
    private CheckNoteDao checkNoteDao;
    @Autowired
    private CheckDao checkDao;
    @Autowired
    private CheckNoteDaoPlus checkNoteDaoPlus;

    public List<Map<String,Object>> selectCheckNote(String checkPerson,String createDate){
        return checkNoteDaoPlus.getCheckNote(checkPerson,createDate);
    }
   // public Page<CheckNote> selectCheckNote(String checkCenter, String manageStation, String manageOffice){
       // return checkNoteDaoPlus.getCheckNote(checkCenter,manageStation,manageOffice);

   // }
    public List<Map<String,Object>> selectCheckNoteByOffice(String checkCenter,String manageStation,String manageOffice){
        return checkNoteDaoPlus.getCheckNoteByOffice(checkCenter,manageStation,manageOffice);

    }
    public List<Map<String,Object>> selectCheckByOffice(String checkCenter,String manageStation,String manageOffice){
        return checkNoteDaoPlus.getCheckByOffice(checkCenter,manageStation,manageOffice);

    }
    public List<Map<String,Object>> selectCheckLongitudeById(String id){
        return checkNoteDao.selectCheckLongitudeById(id);

    }
    public List<Map<String,Object>> selectCheckName(){
        return checkNoteDao.getCheckName();

    }
    public void save(CheckNote checkNote){
        checkNoteDao.save(checkNote);

    }
    public PageResult selectCheckNote(CheckNote checkNote){
        PageRequest pageable;
        if(checkNote.getOrderBy()==null|| "".equals(checkNote.getOrderBy())) {
            pageable = PageRequest.of(checkNote.getPageNo(), checkNote.getPageSize());
        } else {
            pageable = PageRequest.of(checkNote.getPageNo(), checkNote.getPageSize(), checkNote.getDesc(), checkNote.getOrderBy());
        }
        Page<CheckNote> list  = checkNoteDaoPlus.selectCheckNote(checkNote,pageable);
        List<CheckNote> list1 = list.getContent();
        List<CheckNote> list3 = new ArrayList<>();
        for(int i=0;i<list1.size();i++){
            Integer u=0;
            if(list3.size()>0) {
                for (CheckNote ti : list3) {
                    if (ti.getCheckPerson().equals(list1.get(i).getCheckPerson())) {
                        u = 1;
                    }
                }
                if(u != 1){

                    List<CheckNote> list2 = checkNoteDao.selectcheckList(list1.get(i).getCheckPerson());
                   for(CheckNote ti:list2){
                        for(CheckNote tu:list1){
                            if(ti.getId().equals(tu.getId())){
                                list3.add(ti);
                            }
                        }
                   }

                }
            }else {
                List<CheckNote> list2 = checkNoteDao.selectcheckList(list1.get(i).getCheckPerson());
                for(CheckNote ti:list2){
                    for(CheckNote tu:list1){
                        if(ti.getId().equals(tu.getId())){
                            list3.add(ti);
                        }
                    }
                }
            }



        }
        return  PageResult.getPageResult(checkNoteDaoPlus.selectCheckNoteTo(list3,pageable,checkNote));

    }

    public CheckNote selectCheck(String id){
        return checkNoteDao.getCheckNoteById(id);

    }



    public void delete(CheckNote checkNote ){
        checkNoteDao.delete(checkNote);
    }

    public PageResult<CheckNote> find(CheckNote checkNote) {
        PageRequest pageable;
        if(checkNote.getOrderBy()==null||checkNote.getOrderBy().equals("")){
            pageable = PageRequest.of(checkNote.getPageNo(), checkNote.getPageSize(), Sort.by(Sort.Direction.DESC,"createDate"));
        }else{
            pageable = PageRequest.of(checkNote.getPageNo(), checkNote.getPageSize(), checkNote.getDesc(), checkNote.getOrderBy());

        }
        return PageResult.getPageResult(checkNoteDao.findAll(
                (Specification<CheckNote>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (checkNote.getId() !=null && !"".equals(checkNote.getId())) {
                        predicates.add(cb.like(root.get("id").as(String.class), "%" + checkNote.getId() + "%"));
                    }
                    if (checkNote.getCheckId() !=null && !"".equals(checkNote.getCheckId())) {
                        predicates.add(cb.like(root.get("checkId").as(String.class), "%" + checkNote.getCheckId() + "%"));
                    }
                    if (checkNote.getCheckName() !=null && !"".equals(checkNote.getCheckName())) {
                        predicates.add(cb.like(root.get("checkName").as(String.class), "%" + checkNote.getCheckName() + "%"));
                    }

                    if (checkNote.getId() !=null && !"".equals(checkNote.getId())) {
                        predicates.add(cb.like(root.get("id").as(String.class), "%" + checkNote.getId() + "%"));
                    }
                    if (checkNote.getCheckCenter() !=null && !"".equals(checkNote.getCheckCenter())) {
                        predicates.add(cb.like(root.get("checkCenter").as(String.class), "%" + checkNote.getCheckCenter() + "%"));
                    }
                    if (checkNote.getManageStation() !=null && !"".equals(checkNote.getManageStation())) {
                        predicates.add(cb.like(root.get("manageStation").as(String.class), "%" + checkNote.getManageOffice() + "%"));
                    }
                    if (checkNote.getManageOffice() !=null && !"".equals(checkNote.getId())) {
                        predicates.add(cb.like(root.get("manageOffice").as(String.class), "%" + checkNote.getManageOffice() + "%"));
                    }
                    if (checkNote.getLongitude() !=null && !"".equals(checkNote.getLongitude())) {
                        predicates.add(cb.like(root.get("longitude").as(String.class), "%" + checkNote.getLongitude() + "%"));
                    }
                    if (checkNote.getLatitude() !=null && !"".equals(checkNote.getLatitude())) {
                        predicates.add(cb.like(root.get("latitude").as(String.class), "%" + checkNote.getLatitude() + "%"));
                    }
                    if (checkNote.getCreateDate() !=null && !"".equals(checkNote.getCreateDate())) {
                        predicates.add(cb.equal(root.get("createDate").as(Date.class),checkNote.getCreateDate()));
                    }
                    if (checkNote.getPartoal() !=null && !"".equals(checkNote.getPartoal())) {
                        predicates.add(cb.equal(root.get("partoal").as(Date.class),checkNote.getPartoal()));
                    }

                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                },pageable));
    }

//    public List<CheckNote> selectcheckTO(String id, String checkPerson) {
////        String i="1";
////        List<CheckNote> maps = checkNoteDao.selectchekKb(id,checkPerson,i);
////        return maps;
////        List<CheckNote> list = new ArrayList<>();
////        List<Map<String,Object>> maps =new ArrayList<>();
////        return  maps;
//    }
}



