package com.kt.na_social.api.response;

import java.util.ArrayList;
import java.util.List;

public class BaseResponse<T> {
    private int code;
    private List<String> message = new ArrayList<>();
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
