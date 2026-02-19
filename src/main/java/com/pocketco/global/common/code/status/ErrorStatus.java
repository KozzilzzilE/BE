package com.pocketco.global.common.code.status;

import com.pocketco.global.common.code.BaseErrorCode;
import com.pocketco.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // --- Common ---
    _INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON400", "입력값이 올바르지 않습니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "권한이 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // --- ADMIN ---
    ADMIN_IS_NOT(HttpStatus.UNAUTHORIZED, "ADMIN_400", "관리자 권한이 없습니다."),

    // --- AUTH ---
    AUTH_ALREADY_USED(HttpStatus.BAD_REQUEST, "AUTH_400", "이미 가입된 계정입니다"),

    // --- USER ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저를 찾을 수 없습니다."),

    // --- Language ---
    LANGUAGE_ALREADY_USED(HttpStatus.BAD_REQUEST, "LANGUAGE_400", "이미 추가한 언어입니다"),
    LANGUAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LANGUAGE_401", "선택할 수 없는 언어입니다"),

    // --- Topic ---
    TOPIC_ALREADY_USED(HttpStatus.BAD_REQUEST, "TOPIC_400", "이미 추가한 알고리즘 입니다"),
    TOPIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOPIC_401", "선택할 수 없는 알고리즘 입니다")


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}