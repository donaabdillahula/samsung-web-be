package com.dona_samsung_web_project.samsung_web_be.model.response;

public class ErrorResponse extends CommonResponse {
    public ErrorResponse(String code, String message) {
        super.setCode(code);
        super.setMessage(message);
        super.setStatus("FAILED");
    }
}
