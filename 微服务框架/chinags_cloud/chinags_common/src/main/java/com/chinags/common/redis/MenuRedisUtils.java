package com.chinags.common.redis;

import com.chinags.common.utils.Global;
import com.chinags.common.utils.RegularUtils;
import com.chinags.common.utils.StringUtils;

import java.util.List;

/**
 * 菜单缓存
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
public class MenuRedisUtils extends RedisUtils{

    /**
     * 权限名称
     */
    private static final String MENU_NAME = "menu";
    /**
     * 权限名称
     */
    private static final String MENU_NAME_PERS = "menu_permission";

    private MenuRedisUtils(){
        String time = Global.getConfig("redis.timeout.menu");
        if(RegularUtils.stringParseInt(time)){
            timeout = Integer.parseInt(time);
        }
    }

    private static MenuRedisUtils getMenuRedisUtils(){
        return new MenuRedisUtils();
    }

    /**
     * 设置用户菜单权限
     * @param userCode 用户code
     * @param menuCode 菜单code集合
     */
    public static void setUserMenu(String userCode, List<String> menuCode) throws Exception {
        setMenu(userCode, menuCode, MENU_NAME);
    }

    /**
     * 获取用户菜单权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    public static String[] getUserMenu(String userCode) throws Exception {
        return getMenu(userCode, MENU_NAME);
    }

    /**
     * 设置用户菜单数据权限
     * @param userCode 用户code
     * @param menuCode 菜单code集合
     */
    public static void setUserMenuPermission(String userCode, List<String> menuCode) throws Exception {
        setMenu(userCode, menuCode, MENU_NAME_PERS);
    }

    /**
     * 获取用户菜单数据权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    public static String[] getUserMenuPermission(String userCode) throws Exception {
        return getMenu(userCode, MENU_NAME_PERS);
    }

    /**
     * 设置用户菜单权限
     * @param userCode 用户code
     * @param menuCode 菜单code集合
     */
    private static void setMenu(String userCode, List<String> menuCode, String key) throws Exception {
        set(userCode, menuCode, key);
    }

    /**
     * 获取用户菜单权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    private static String[] getMenu(String userCode, String key) throws Exception {
        return get(userCode, key);
    }

    /**
     * 删除用户菜单权限
     * @param userCode 用户code
     * @return 菜单code数组
     */
    public static void deleteUserMenu(String userCode) throws Exception {
        delete(userCode, MENU_NAME, MENU_NAME_PERS);
    }

}
