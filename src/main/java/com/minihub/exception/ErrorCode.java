package com.minihub.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    PARAM_ERROR(400, HttpStatus.BAD_REQUEST, "参数错误"),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "未登录或登录已失效"),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "无权限"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "资源不存在"),
    CONFLICT(409, HttpStatus.CONFLICT, "数据冲突"),
    SYSTEM_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "系统异常"),
    DATABASE_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "数据库异常");

    private final int code;
    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(int code, HttpStatus httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}