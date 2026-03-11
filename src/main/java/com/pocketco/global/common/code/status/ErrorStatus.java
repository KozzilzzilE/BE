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
    _INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "DB 제약 조건 위반입니다."),

    // --- ADMIN ---
    ADMIN_IS_NOT(HttpStatus.UNAUTHORIZED, "ADMIN_400", "관리자 권한이 없습니다."),

    // --- AUTH ---
    AUTH_ALREADY_USED(HttpStatus.BAD_REQUEST, "AUTH_400", "이미 가입된 계정입니다"),

    // --- USER ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저를 찾을 수 없습니다."),

    // --- Language ---
    LANGUAGE_ALREADY_USED(HttpStatus.BAD_REQUEST, "LANGUAGE_400", "이미 추가한 언어입니다"),
    LANGUAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LANGUAGE_401", "선택할 수 없는 언어입니다"),
    LANGUAGE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "LANGUAGE_404", "선택할 수 있는 언어가 없습니다"),

    // --- Topic ---
    TOPIC_ALREADY_USED(HttpStatus.BAD_REQUEST, "TOPIC_400", "이미 추가한 알고리즘 입니다"),
    TOPIC_NOT_FOUND(HttpStatus.BAD_REQUEST, "TOPIC_401", "선택할 수 없는 알고리즘 입니다"),

    // --- Learning Notion ---
    LEARNING_NOTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_400", "해당 알고리즘의 해당 개념 페이지는 이미 존재 합니다"),
    LEARNING_NOTION_CODE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_401", "해당 페이지에 해당 언어의 예제 코드는 이미 존재 합니다"),
    LEARNING_NOTION_TOPIC_NOT_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_402", "해당 알고리즘의 해당 개념 페이지가 없습니다"),
    LEARNING_NOTION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_403", "해당 개념 페이지가 없습니다"),
    LEARNING_APPLIED_EXERCISE_TOPIC_NOT_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_404", "해당 알고리즘의 해당 응용 페이지가 없습니다"),
    LEARNING_APPLIED_EXERCISE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "NOTION_405", "해당 응용 페이지가 없습니다"),

    // --- Learning Applied Exercise ---
    LEARNING_APPLIED_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "APPLIED_400", "해당 알고리즘의 해당 응용 페이지는 이미 존재 합니다"),
    LEARNING_APPLIED_CODE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "APPLIED_401", "해당 페이지에 해당 언어의 코드는 이미 존재 합니다"),

    // --- main screen ---
    USER_MAIN_INFO_FAIL(HttpStatus.BAD_REQUEST, "USER_400", "메인 화면 정보를 불러오는 데 실패했습니다."),

    // --- problem ---
    PROBLEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_401", "이미 존재하는 문제 제목입니다."),
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "문제를 찾을 수 없습니다."),
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "해당 언어의 모범 답안이 없습니다.");

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