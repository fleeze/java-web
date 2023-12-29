package com.zz.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class ResponseObject<T> {
    protected int code;
    private T data;
    protected String msg;

    public ResponseObject(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> ResponseObject<T> success(T data) {
        return new ResponseObject<>(200, data, "success");
    }

    public static <T> ResponseObject<T> error(int code, String msg) {
        String defaultMessage = "fail";
        String finalMsg = msg == null ? defaultMessage : msg;
        return new ResponseObject<>(code, null, finalMsg);
    }
}