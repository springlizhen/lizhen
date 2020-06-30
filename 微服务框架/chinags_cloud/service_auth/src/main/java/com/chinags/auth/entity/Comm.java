package com.chinags.auth.entity;

import com.chinags.common.entity.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Mr.Zhang
 * @version V1.0
 * @date
 * @since 1.8
 */
@Entity
@Table(name = "T_PUB_SYS_COMM")
public class Comm extends BaseEntity {
    @Id
    @GenericGenerator(name = "comm-uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "comm-uuid")
    private String id;
    /**
     * 系统名称
     */
    private String authName;
    /**
     * 系统编码
     */
    private String authCode;
    /**
     * 系统地址
     */
    private String authUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

}
