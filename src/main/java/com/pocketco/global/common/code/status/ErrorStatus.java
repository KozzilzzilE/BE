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

    // --- AUTH ---
    AUTH_ALREADY_USED(HttpStatus.CONFLICT, "AUTH_409_ALREADY_USED", "이미 가입된 계정입니다."),
    MISSING_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_TOKEN_MISSING", "Access Token이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_TOKEN_INVALID", "유효하지 않은 Access Token입니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_TOKEN_EXPIRED", "Access Token이 만료되었습니다."),

    // --- USER ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404_NOT_FOUND", "유저를 찾을 수 없습니다."),

    // --- Language ---
    LANGUAGE_ALREADY_USED(HttpStatus.CONFLICT, "LANGUAGE_409_ALREADY_USED", "이미 추가한 언어입니다."),
    LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "LANGUAGE_404_NOT_FOUND", "선택할 수 없는 언어입니다."),
    LANGUAGE_NOT_EXISTS(HttpStatus.NOT_FOUND, "LANGUAGE_404_EMPTY", "선택할 수 있는 언어가 없습니다."),

    // --- Topic ---
    TOPIC_ALREADY_USED(HttpStatus.CONFLICT, "TOPIC_409_ALREADY_USED", "이미 추가한 알고리즘 입니다."),
    TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND, "TOPIC_404_NOT_FOUND", "선택할 수 없는 알고리즘 입니다."),

    // --- Learning Notion ---
    LEARNING_NOTION_ALREADY_EXISTS(HttpStatus.CONFLICT, "NOTION_409_ALREADY_EXISTS", "해당 알고리즘의 해당 개념 페이지는 이미 존재 합니다."),
    LEARNING_NOTION_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "NOTION_409_CODE_ALREADY_EXISTS", "해당 페이지에 해당 언어의 예제 코드는 이미 존재 합니다."),
    LEARNING_NOTION_TOPIC_NOT_EXISTS(HttpStatus.NOT_FOUND, "NOTION_404_TOPIC_NOT_FOUND", "해당 알고리즘의 해당 개념 페이지가 없습니다."),
    LEARNING_NOTION_NOT_EXISTS(HttpStatus.NOT_FOUND, "NOTION_404_NOT_FOUND", "해당 개념 페이지가 없습니다."),

    // --- Learning Applied Exercise ---
    LEARNING_APPLIED_ALREADY_EXISTS(HttpStatus.CONFLICT, "APPLIED_409_ALREADY_EXISTS", "해당 알고리즘의 해당 응용 페이지는 이미 존재 합니다."),
    LEARNING_APPLIED_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "APPLIED_409_CODE_ALREADY_EXISTS", "해당 페이지에 해당 언어의 코드는 이미 존재 합니다."),
    LEARNING_APPLIED_EXERCISE_TOPIC_NOT_EXISTS(HttpStatus.NOT_FOUND, "APPLIED_404_TOPIC_NOT_FOUND", "해당 알고리즘의 해당 응용 페이지가 없습니다."),
    LEARNING_APPLIED_EXERCISE_NOT_EXISTS(HttpStatus.NOT_FOUND, "APPLIED_404_NOT_FOUND", "해당 응용 페이지가 없습니다."),
    LEARNING_APPLIED_CODE_ANSWER_DUPLICATE(HttpStatus.BAD_REQUEST, "APPLIED_400_ANSWER_DUPLICATE", "중복된 정답 번호가 존재합니다."),

    // --- problem ---
    PROBLEM_ALREADY_EXISTS(HttpStatus.CONFLICT, "PROBLEM_409_ALREADY_EXISTS", "이미 존재하는 문제 제목입니다."),
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM_404_NOT_FOUND", "문제를 찾을 수 없습니다."),
    SOLUTION_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM_404_SOLUTION_CODE_NOT_FOUND", "해당 언어의 모범 답안이 없습니다."),
    PROBLEM_LANGUAGE_TIME_LIMIT_ALREADY_EXISTS(HttpStatus.CONFLICT, "PROBLEM_409_LANGUAGE_TIME_LIMIT_ALREADY_EXISTS", "해당 문제에 해당 언어의 시간 제한 데이터는 이미 존재 합니다."),
    PROBLEM_LANGUAGE_SOLUTION_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "PROBLEM_409_LANGUAGE_SOLUTION_CODE_ALREADY_EXISTS", "해당 문제에 해당 언어의 모범 코드는 이미 존재 합니다."),
    PROBLEM_INVALID_DIFFICULTY(HttpStatus.BAD_REQUEST, "PROBLEM_400_INVALID_DIFFICULTY", "잘못된 난이도 값입니다. (EASY, NORMAL, HARD, ALL 중 하나를 입력하세요)"),
    TEMP_STORAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM_404_TEMP_STORAGE_NOT_FOUND", "임시 저장된 데이터가 없습니다."),

    // --- history & token ---
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "HISTORY_404_NOT_FOUND", "제출 기록을 찾을 수 없습니다."),
    HISTORY_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "HISTORY_401_UNAUTHORIZED", "제출 기록에 접근할 수 없습니다."),

    // --- Bookmark ---
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "BOOKMARK_409_ALREADY_EXISTS", "이미 찜한 문제입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKMARK_404_NOT_FOUND", "찜한 기록을 찾을 수 없습니다."),

    // --- Public Profile Image ---
    PUBLIC_PROFILE_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PUBLIC_PROFILE_IMAGE_404_NOT_FOUND", "공개된 프로필 사진을 찾을 수 없습니다.")
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