package com.chinags.common.redis;

import com.chinags.common.utils.Global;
import com.chinags.common.utils.RegularUtils;

import java.util.List;

/**
 * 角色缓存
 */
public class RoleDataRedisUtils extends RedisUtils{

    /**
     * 权限名称
     */
    private static final String ROLE_NAME = "role";
    /**
     * 权限名称
     */
    private static final String ROLE_NAME_PERS = "role_permission";

    private RoleDataRedisUtils(){
        String time = Global.getConfig("redis.timeout.menu");
        if(RegularUtils.stringParseInt(time)){
            timeout = Integer.parseInt(time);
        }
    }

    public static RoleDataRedisUtils getRoleDataRedisUtils(){
        return new RoleDataRedisUtils();
    }

    /**
     * 设置用户角色部门数据权限
     * @param userCode 用户code
     * @param officeCode 部门code集合
     */
    public static void setUserOffice(String userCode, List<String> officeCode, String systemname) throws Exception {
        setMenu(userCode, officeCode, ROLE_NAME + JedisUtil.NAME_LINK + systemname);
    }

    /**
     * 获取用户角色部门数据权限
     * @param userCode 用户code
     * @return 部门code数组
     */
    public static String[] getUserOffice(String userCode, String systemname) throws Exception {
        return getMenu(userCode, ROLE_NAME + JedisUtil.NAME_LINK + systemname);
    }

    /**
     * 设置用户角色部门人员数据权限
     * @param userCode 用户code
     * @param userCodes 优用户code集合
     */
    public static void setUserOfficePermission(String userCode, List<String> userCodes) throws Exception {
        setMenu(userCode, userCodes, ROLE_NAME_PERS);
    }

    /**
     * 获取用户角色部门人员数据权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    public static String[] getUserOfficePermission(String userCode) throws Exception {
        return getMenu(userCode, ROLE_NAME_PERS);
    }

    /**
     * 设置用户角色权限
     * @param userCode 用户code
     * @param code 数据code集合
     */
    public static void setMenu(String userCode, List<String> code, String key) throws Exception {
        set(userCode, code, key);
    }

    /**
     * 获取用户角色权限
     * @param userCode 用户code
     * @return 数据code数组
     */
    public static String[] getMenu(String userCode, String key) throws Exception {
        return get(userCode, key);
    }

    /**
     * 删除用户菜单权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    public static void deleteUserMenu(String userCode, String systemname) throws Exception {
        delete(userCode, ROLE_NAME + JedisUtil.NAME_LINK + systemname, ROLE_NAME_PERS + JedisUtil.NAME_LINK + systemname);
    }
}
