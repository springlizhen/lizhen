package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName：ResultObject
 * Package:com.bjpowernode.p2p.model.vo
 * Description:
 *
 * @date:2019/2/25 21:28
 * @author:guoxin@bjpowernode.com
 *
 */
    /**
     * 错误码
     *
     *
     * **/
public class ResultObject implements Serializable {
    private  String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
