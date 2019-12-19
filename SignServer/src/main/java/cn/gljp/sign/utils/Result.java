package cn.gljp.sign.utils;

import java.io.Serializable;

public class Result implements Serializable {
    private Integer code;
    private boolean success;//是否成功
    private String message;//返回信息
    private Object data;//返回数据

    public Result() {
    }

    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(Integer code, boolean success, String message, Object data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public Result( boolean success,  Object data) {
        this.message = message;
        this.data = data;
    }
    public Result(Integer code,  String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public Result success(boolean success, String message, Object data){
        return new Result(20000, success, message, data);
    }
    public Result success(boolean success, Object data){
        return new Result(20000, message, data);
    }
}
