package com.pocketco.global.common.code.status;

import com.pocketco.global.common.code.BaseCode;
import com.pocketco.global.common.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    _CREATED(HttpStatus.CREATED, "COMMON201", "요청 성공 및 리소스 생성됨"),

    // user
    USER_REGISTER_LOCAL_SUCCESS(HttpStatus.OK, "USER_300", "로컬 회원가입 성공했습니다."),
    USER_PROFILE_SUCCESS(HttpStatus.OK, "USER_301", "로그인한 유저 조회 성공했습니다."),
    USER_LOGIN_LOCAL_SUCCESS(HttpStatus.OK, "USER_302", "로컬 로그인 성공했습니다."),


    // token
    //TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "TOKEN_200", "토큰이 정상적으로 재발급되었습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}