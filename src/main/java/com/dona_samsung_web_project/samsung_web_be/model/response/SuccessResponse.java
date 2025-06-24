package com.dona_samsung_web_project.samsung_web_be.model.response;

import org.springframework.http.HttpStatus;

public class SuccessResponse<T> extends CommonResponse {
    T data;

    public SuccessResponse(T data, String message) {
        super.setCode("00");
        super.setMessage(message);
        super.setStatus(HttpStatus.OK.name());
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
