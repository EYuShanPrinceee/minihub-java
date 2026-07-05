package com.minihub.exception;

import com.minihub.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException e) {
        log.warn("业务异常：code={}, message={}", e.getCode(), e.getMessage());

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("参数错误");

        log.warn("参数校验失败：{}", message);

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), message));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求体格式错误：{}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), "请求体格式错误"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e
    ) {
        String message = "缺少必要参数：" + e.getParameterName();

        log.warn("缺少请求参数：{}", e.getParameterName());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e
    ) {
        String message = "参数类型错误：" + e.getName();

        log.warn("参数类型错误：name={}, value={}", e.getName(), e.getValue());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), message));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("唯一约束冲突", e);

        return ResponseEntity
                .status(ErrorCode.CONFLICT.getHttpStatus())
                .body(ApiResponse.fail(ErrorCode.CONFLICT.getCode(), "数据已存在"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.warn("数据完整性异常", e);

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.fail(ErrorCode.PARAM_ERROR.getCode(), "数据不符合约束"));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccessException(DataAccessException e) {
        log.error("数据库异常", e);

        return ResponseEntity
                .status(ErrorCode.DATABASE_ERROR.getHttpStatus())
                .body(ApiResponse.fail(ErrorCode.DATABASE_ERROR.getCode(), "数据库异常"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("系统异常", e);

        return ResponseEntity
                .status(ErrorCode.SYSTEM_ERROR.getHttpStatus())
                .body(ApiResponse.fail(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常"));
    }
}