package com.chinags.common.entity;

public class Result {
    private boolean status;
    private Integer code;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(boolean status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Result(boolean status, Integer code, String message, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
