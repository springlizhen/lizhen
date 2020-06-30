package com.chinags.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类
 *
 */
public class Global
{
    private static final Logger log = LoggerFactory.getLogger(Global.class);

    private static String NAME = "application.yml";

    /**
     * 当前对象实例
     */
    private static Global global;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<String, String>();

    private Global()
    {
    }

    /**
     * 静态工厂方法
     */
    public static synchronized Global getInstance()
    {
        if (global == null)
        {
            global = new Global();
        }
        return global;
    }

    /**
     * 获取配置
     */
    public static String getConfig(String key)
    {
        String value = map.get(key);
        if (value == null)
        {
            Map<?, ?> yamlMap = null;
            try
            {
                yamlMap = YamlUtil.loadYaml(NAME);
                //修复当v为null时，禁止转成String
                Object v = YamlUtil.getProperty(yamlMap, key);
                if(v!=null) {
                    value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                }
                map.put(key, value != null ? value : StringUtils.EMPTY);
            }
            catch (FileNotFoundException e)
            {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }

    /**
     * 获取项目名称
     */
    public static String getName()
    {
        return StringUtils.nvl(getConfig("chinags.name"), "chinags");
    }

    /**
     * 获取项目版本
     */
    public static String getVersion()
    {
        return StringUtils.nvl(getConfig("chinags.version"), "3.4.0");
    }

    /**
     * 获取版权年份
     */
    public static String getCopyrightYear()
    {
        return StringUtils.nvl(getConfig("chinags.copyrightYear"), "2019");
    }

    /**
     * 实例演示开关
     */
    public static String isDemoEnabled()
    {
        return StringUtils.nvl(getConfig("chinags.demoEnabled"), "true");
    }

    /**
     * 获取ip地址开关
     */
    public static Boolean isAddressEnabled()
    {
        return Boolean.valueOf(getConfig("chinags.addressEnabled"));
    }

    /**
     * 获取文件上传路径
     */
    public static String getProfile()
    {
        return getConfig("chinags.profile");
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getConfig("chinags.profile") + "avatar/";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getConfig("chinags.profile") + "download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getConfig("chinags.profile") + "upload/";
    }

    /**
     * 获取初始密码，默认123456
     */
    public static String getPassWord()
    {
        String password = getConfig("initpassword");
        return StringUtils.isEmpty(password)?"123456":password;
    }

    /**
     * 获取加密文，默认jd-ds-auth
     * @return
     */
    public static String getEncryption(){
        String encryption = Global.getConfig("encryption");
        return StringUtils.isEmpty(encryption)?"jd-ds-auth":encryption;
    }

    /**
     * 获取redis密码
     * @return
     */
    public static String getRedisPassword(){
        String password = Global.getConfig("redis.password");
        return StringUtils.isEmpty(password)?null:password;
    }

    /**
     * 获取redis keyName
     * @return
     */
    public static String getRedisKeyName(){
        String keyName = Global.getConfig("redis.keyName");
        return StringUtils.isEmpty(keyName)?"jd_ds_auth":keyName;
    }

    /**
     * 获取zuul拦截地址
     */
    public static String[] getZullSystem() {
        String config = Global.getConfig("zuul.system");
        return StringUtils.isEmpty(config)?new String[0]:config.split(",");
    }

    /**
     * 获取zuul非拦截地址
     */
    public static String[] getZullNoFilter() {
        String config = Global.getConfig("zuul.no-filter");
        return StringUtils.isEmpty(config)?new String[0]:config.split(",");
    }

    /**
     * 获取redis地址
     * @return
     */
    public static String getRedisURL(){
        String password = Global.getConfig("redis.url");
        return StringUtils.isEmpty(password)?"127.0.0.1":password;
    }
}
