package com.pocketco.global.exception;

import com.pocketco.global.common.code.ErrorReasonDTO;
import com.pocketco.global.common.code.status.ErrorStatus;
import com.pocketco.global.common.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    /** 커스텀 GeneralException 처리 **/
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<BaseResponse<?>> handleGeneralException(GeneralException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        ErrorReasonDTO errorReason = ex.getErrorCode().getReasonHttpStatus();
        log.warn("[handleGeneralException] {}: {} | path : {}", errorReason.getCode(), errorReason.getMessage(), path);

        BaseResponse<?> errorResponse = BaseResponse.onFailure(ex.getErrorCode(), path);
        return new ResponseEntity<>(errorResponse, errorReason.getHttpStatus());
    }

    /** @Valid 검증 실패 처리 **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                 HttpServletRequest request) {
        String path = request.getRequestURI();
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("입력값이 올바르지 않습니다.");

        log.warn("[handleMethodArgumentNotValidException] {} | path : {}", errorMessage, path);

        BaseResponse<?> errorResponse = BaseResponse.onFailure(
                ErrorStatus._INVALID_INPUT_VALUE,
                errorMessage,
                path
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /** 커스텀 annotation 에러 처리 **/
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<?>> handleConstraintViolationException(ConstraintViolationException ex,
                                                                              HttpServletRequest request){
        String path = request.getRequestURI();
        String errorMessage = ex.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "ConstraintViolationException 추출 도중 에러 발생")
                );
        log.warn("[handleConstraintViolationException] {} | path : {}", errorMessage, path);

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                ErrorStatus._BAD_REQUEST,
                errorMessage,
                path
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleDataIntegrity(DataIntegrityViolationException e,
                                                                  HttpServletRequest request) {

        if (isUniqueConstraintViolation(e, "uk_notion_topic_page")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 또는 BAD_REQUEST
                    .body(BaseResponse.onFailure(ErrorStatus.LEARNING_NOTION_ALREADY_EXISTS, request.getRequestURI()));
        }
        if (isUniqueConstraintViolation(e, "uk_applied_topic_order")) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 또는 BAD_REQUEST
                    .body(BaseResponse.onFailure(ErrorStatus.LEARNING_APPLIED_ALREADY_EXISTS, request.getRequestURI()));
        }

        // 그 외는 일반 무결성 에러로 처리
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(BaseResponse.onFailure(ErrorStatus._INVALID_REQUEST, request.getRequestURI()));
    }

    // DB 제약조건 이름 확인하는 메서드
    private boolean isUniqueConstraintViolation(DataIntegrityViolationException e, String constraintName) {
        Throwable cause = e.getCause();
        while (cause != null) {
            // Hibernate ConstraintViolationException에 제약 이름이 들어있는 경우가 많음
            if (cause instanceof org.hibernate.exception.ConstraintViolationException cve) {
                String name = cve.getConstraintName();
                return name != null && name.equalsIgnoreCase(constraintName);
            }
            cause = cause.getCause();
        }
        return false;
    }


    /** 그 외 모든 예상 못한 예외 처리 **/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        log.error("[handleUnexpectedException] 알 수 없는 예외 발생 | path : {}", path, ex);

        BaseResponse<?> errorResponse = BaseResponse.onFailure(
                ErrorStatus._INTERNAL_SERVER_ERROR,
                path
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}