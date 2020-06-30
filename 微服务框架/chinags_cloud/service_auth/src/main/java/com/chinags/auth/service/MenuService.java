package com.chinags.auth.service;

import com.chinags.auth.dao.MenuDao;
import com.chinags.auth.dao.RoleDao;
import com.chinags.auth.entity.Menu;
import com.chinags.auth.entity.SysUser;
import com.chinags.common.entity.PageResult;
import com.chinags.common.entity.Result;
import com.chinags.common.entity.StatusCode;
import com.chinags.common.lang.StringUtils;
import com.chinags.common.redis.MenuRedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Service
public class MenuService {

    public static Lock lock = new ReentrantLock();

    public Sort sort = new Sort(Sort.Direction.ASC, "sysCode", "treeLevel", "treeSort");

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

    /**
     * 获取左侧菜单栏
     * @param menu 查询条件实体类
     * @return 菜单集合
     */
    public List<Menu> findAll(Menu menu){
        //菜单类型  菜单
        menu.setMenuType("1");
        return findAllS(menu);
    }

    /**
     * 获取左侧菜单栏以及权限
     * @param menu 查询条件实体类
     * @return 菜单集合
     */
    public List<Menu> findAllS(Menu menu){
        //是否显示 显示
        menu.setIsShow("1");
        //菜单状态 正常
        menu.setStatus("0");
        return findData(menu);
    }

    /**
     * 获取全部数据
     * @param menu 查询条件实体类
     * @return 菜单集合
     */
    public List<Menu> findData(Menu menu){
        Example<Menu> ex = Example.of(menu);
        return menuDao.findAll(ex, sort);
    }

    /**
     * 查询全部列表
     * @return 菜单分页
     */
    public PageResult find(Menu menu) {
        PageRequest pageable;
        if(menu.getOrderBy()==null|| "".equals(menu.getOrderBy())) {
            pageable = PageRequest.of(menu.getPageNo(), menu.getPageSize(), Sort.Direction.ASC, "sysCode","treeSort");
        } else {
            pageable = PageRequest.of(menu.getPageNo(), menu.getPageSize(), menu.getDesc(), menu.getOrderBy());
        }
        //数据简单转换，转换为前台框架所需要分页json
        PageResult pageResult;
        pageResult = PageResult.getPageResult(menuDao.findAll(
                (Specification<Menu>) (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (menu.getMenuName() != null && !"".equals(menu.getMenuName())) {
                        predicates.add(cb.like(root.get("menuName").as(String.class), "%" + menu.getMenuName() + "%"));
                    }
                    if (menu.getParentCode() != null && !"".equals(menu.getParentCode())) {
                        predicates.add(cb.equal(root.get("parentCode").as(String.class), menu.getParentCode()));
                    }
                    if (menu.getSysCode() != null && !"".equals(menu.getSysCode())) {
                        predicates.add(cb.equal(root.get("sysCode").as(String.class), menu.getSysCode()));
                    }
                    predicates.add(cb.equal(root.get("status").as(String.class), 0));
                    Predicate[] pre = new Predicate[predicates.size()];
                    query.where(predicates.toArray(pre));
                    return cb.and(predicates.toArray(pre));
                }, pageable));
        return pageResult;
    }

    /**
     * 保存菜单
     * @param menu 保存菜单实体
     */
    @Transactional(rollbackOn = Exception.class)
    public void save(Menu menu){
        Menu menuParent = getMenuByMenuCode(menu.getParentCode());
        Menu m = this.getMenuByMenuCode(menu.getMenuCode());
        if(menuParent!=null) {
            menu.setParentCodes(menuParent.getParentCodes() + menuParent.getMenuCode() + ",");
            menu.setTreeSorts(menuParent.getTreeSorts() + String.format("%10d", menu.getTreeSort()).replace(" ", "0") + ",");
            menu.setTreeNames(menuParent.getTreeNames() + "/" + menu.getMenuName());
            if (StringUtils.isEmpty(menu.getMenuCode())) {
                menu.setTreeLeaf("1");
                menu.setTreeLevel(menuParent.getTreeLevel() + 1);
                menu.setStatus("0");
                menuParent.setTreeLeaf("0");
                menuDao.save(menuParent);
            }
        }else{
            menu.setParentCode("0");
            menu.setParentCodes("0,");
            menu.setTreeSorts(String.format("%10d", menu.getTreeSort()).replace(" ", "0") + ",");
            menu.setTreeNames(menu.getMenuName());
            if (StringUtils.isEmpty(menu.getMenuCode())) {
                menu.setTreeLeaf("1");
                menu.setTreeLevel(0);
                menu.setStatus("0");
            }
        }
        if(menu.getMenuCode()!=null){
            menu.setTreeLeaf(m.getTreeLeaf());
            menu.setTreeLevel(m.getTreeLevel());
            menu.setStatus(m.getStatus());
        }
        //父级菜单是否变动
        if(m!=null&&!menu.getParentCode().equals(m.getParentCode())){
            menuDao.save(menu);
            menuTreelevel(m, menuParent);
        }else{
            menuDao.save(menu);
            menuTreelevel(menu, menuParent);
        }
    }

    /**
     * 删除菜单
     * @param menu 菜单实体
     */
    @Transactional(rollbackOn = Exception.class)
    public Boolean delete(Menu menu){
        int count = parentCodesCount("%"+menu.getMenuCode()+",%");
        if(count > 0) {
            return false;
        }
        String menuParentCode = getMenuByMenuCode(menu.getMenuCode()).getParentCode();
        menuDao.delete(menu);
        count = parentCodesCount("%"+menuParentCode+",%");
        if (count==0){
            Menu menuCodeParent = getMenuByMenuCode(menuParentCode);
            menuCodeParent.setTreeLeaf("1");
            menuDao.save(menuCodeParent);
        }
        return true;
    }

    /**
     * 是否存在子菜单
     * @param menuCode 菜单id
     * @return
     */
    public int parentCodesCount(String menuCode){
        return menuDao.getParentCodesCount(menuCode);
    }

    /**
     * 获得菜单信息
     * @param menuCode 菜单code
     * @return 菜单实体
     */
    public Menu getMenuByMenuCode(String menuCode){
        return menuDao.getMenuByMenuCode(menuCode);
    }

    /**
     * 根据menuCode获取菜单set
     * @param menuCode 菜单code
     * @return 菜单集合
     */
    public Set<Menu> inMenuCode(String menuCode){
        return menuDao.findByMenuCodeIn(menuCode.split(","));
    }

    /**
     * 删除菜单redis
     * @param usercode 用户code
     */
    public void deleteRedis(String usercode){
        try {
            MenuRedisUtils.deleteUserMenu(usercode);
        } catch (Exception e) {
        }
    }

    /**
     * 重置菜单以及子菜单层级
     * @param menuCode 菜单
     */
    public void menuTreelevel(Menu menuCode, Menu menuParent){
        String parentCode = menuCode.getParentCode();
        if(StringUtils.isEmpty(parentCode)||menuParent==null){
            menuCode.setTreeLevel(0);
        }else{
            menuCode.setTreeLevel(menuParent.getTreeLevel() + 1);
        }
        menuDao.save(menuCode);
        List<Menu> menuList = menuDao.getParentCodesMenu(menuCode.getMenuCode(), menuCode.getSysCode());
        for (Menu menu : menuList) {
            menuTreelevel(menu, menuCode);
        }
    }

    /**
     * 菜单redis添加
     * @param usercode 用户code
     * @throws Exception
     */
    public void menuRedis(String usercode) throws Exception {
        //获取权限菜单
        List<String> userCodeRole = roleDao.getUserCodeRole(usercode);
        String[] menutype = new String[userCodeRole.size()];
        List<String> menuCode = menuDao.getRoleMenu(userCodeRole.toArray(menutype));
        MenuRedisUtils.setUserMenu(usercode, menuCode);
    }

    /**
     * 菜单权限redis添加
     * @param usercode 用户code
     * @param systemname 系统
     * @throws Exception
     */
    public void menuPermissionRedis(String usercode, String systemname) throws Exception {
        List<String> roleMenuPermission = menuDao.getRoleMenuPermission(menuRedisList(usercode,systemname));
        MenuRedisUtils.setUserMenuPermission(usercode, roleMenuPermission);
    }

    /**
     * 菜单权限
     * @param usercode 用户code
     * @param systemname 系统
     * @return 菜单集合
     */
    public List<Menu> menuList(String usercode, String systemname) throws Exception {
        return menuDao.getRoleLeftMenu(menuRedisList(usercode,systemname), systemname, sort);
    }


    /**
     * 菜单数据权限
     * @param usercode 用户code
     * @param systemname 系统
     * @return 菜单集合
     */
    public List<Menu> menuListPermission(String usercode, String systemname) throws Exception {
        return menuDao.getRoleLeftMenuPermission(menuRedisList(usercode,systemname), systemname, sort);
    }

    public String[] menuRedisList(String usercode, String systemname) throws Exception {
        String[] menuCodes = MenuRedisUtils.getUserMenu(usercode);
        if(menuCodes==null){
            if(menuCodes==null) {
                if(lock.tryLock()) {
                    menuRedis(usercode);
                    lock.unlock();
                }else {
                    Thread.sleep(100);
                    menuRedis(usercode);
                }
            }
            return menuRedisList(usercode, systemname);
        }
        return menuCodes;
    }

    /**
     * 获取菜单数据权限
     * @param userCode 用户code
     * @return
     * @throws Exception
     */
    public String[] menuDataList(String userCode, String systemname) throws Exception {
        String[] menuCodes = MenuRedisUtils.getUserMenuPermission(userCode);
        if(menuCodes==null){
            if(menuCodes==null) {
                if(lock.tryLock()) {
                    menuPermissionRedis(userCode, systemname);
                    lock.unlock();
                }else {
                    Thread.sleep(100);
                    menuDataList(userCode, systemname);
                }
            }
            return menuDataList(userCode, systemname);
        }
        return menuCodes;
    }

    /**
     * 是否存在权限
     * @param userCode
     * @param permission 权限标识
     * @return
     * @throws Exception
     */
    public boolean menuPresence(String userCode, String permission) throws Exception {
        String[] strings = menuDataList(userCode, null);
        return Arrays.asList(strings).contains(permission);
    }

}
