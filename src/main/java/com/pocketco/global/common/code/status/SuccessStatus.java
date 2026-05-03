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

    // auth
    AUTH_REGISTER_SUCCESS(HttpStatus.OK, "AUTH_300", "회원가입 성공했습니다."),
    AUTH_LOGIN_SUCCESS(HttpStatus.OK, "AUTH_301", "로그인 성공했습니다."),
    AUTH_LOGOUT_SUCCESS(HttpStatus.OK, "AUTH_302", "로그아웃 성공했습니다."),

    // user
    USER_PROFILE_SUCCESS(HttpStatus.OK, "USER_300", "로그인한 유저 조회 성공했습니다."),
    USER_MAIN_SUCCESS(HttpStatus.OK, "USER_200", "메인 화면 정보 조회 성공했습니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "USER_201", "사용자 이름 변경이 완료되었습니다."),
    LANGUAGE_UPDATE_SUCCESS(HttpStatus.OK, "USER_201", "메인 언어가 성공적으로 변경되었습니다."),
    USER_MYPAGE_SUCCESS(HttpStatus.OK, "USER_203", "사용자 마이페이지 조회 성공했습니다."),

    // language
    LANGUAGES_LISTS_SUCCESS(HttpStatus.OK, "LANGUAGE_200", "사용가능한 언어 리스트 조회를 성공했습니다."),

    // admin
    ADMIN_ADD_LANGUAGE_SUCCESS(HttpStatus.CREATED, "ADMIN_200", "언어 추가 성공했습니다."),
    ADMIN_ADD_TOPIC_SUCCESS(HttpStatus.CREATED, "ADMIN_201", "알고리즘 주제 추가 성공했습니다."),
    ADMIN_ADD_NOTION_SUCCESS(HttpStatus.CREATED, "ADMIN_202", "개념 학습 페이지 추가 성공했습니다."),
    ADMIN_ADD_APPLIED_EXERCISE_SUCCESS(HttpStatus.CREATED, "ADMIN_203", "응용 학습 추가 성공했습니다."),
    ADMIN_ADD_PROBLEM_SUCCESS(HttpStatus.CREATED, "ADMIN_204", "코딩 문제 추가 성공했습니다."),
    ADMIN_JUDGE0_LANGUAGE_LIST(HttpStatus.OK, "ADMIN_205", "judge0 언어 목록 조회 성공했습니다."),
    ADMIN_ADD_PROBLEM_LANGUAGE_SETTING_SUCCESS(HttpStatus.CREATED, "ADMIN_206", "코딩 문제 언어 설정 추가 성공했습니다."),
    ADMIN_ADD_CS_PROBLEM_SUCCESS(HttpStatus.CREATED, "ADMIN_209", "CS 문제 추가 성공했습니다."),

    // topic
    TOPIC_LIST_SUCCESS(HttpStatus.OK, "TOPIC_200", "알고리즘 목록 조회 성공"),
    TOPIC_GET_PROBLEMS_SUCCESS(HttpStatus.OK, "TOPIC_202", "문제 목록 조회 성공"),

    // learning
    LEARNING_NOTION_LIST_SUCCESS(HttpStatus.OK, "LEARNING_200", "개념 학습 조회 성공했습니다."),
    LEARNING_NOTION_COMPLETED_SUCCESS(HttpStatus.OK, "LEARNING_201", "개념 학습 완료로 DB 변경 성공했습니다."),
    LEARNING_APPLIED_EXERCISE_LIST_SUCCESS(HttpStatus.OK, "LEARNING_202", "응용 학습 조회 성공했습니다."),
    LEARNING_APPLIED_EXERCISE_COMPLETED_SUCCESS(HttpStatus.OK, "LEARNING_203", "응용 학습 완료로 DB 변경 성공했습니다."),

    // problem
    PROBLEM_DETAIL_GET_SUCCESS(HttpStatus.OK, "PROBLEM_200", "문제 상세 정보 조회 성공"),
    PROBLEM_SOLUTION_GET_SUCCESS(HttpStatus.OK, "PROBLEM_201", "모범 답안 조회 성공"),
    PROBLEM_RUN_SUCCESS(HttpStatus.OK, "PROBLEM_202", "문제 코드 실행 요청 성공"),
    PROBLEM_SUBMIT_SUCCESS(HttpStatus.OK, "PROBLEM_203", "문제 코드 채점 요청 성공"),
    PROBLEM_RUN_RESULT_SUCCESS(HttpStatus.OK, "PROBLEM_204", "문제 코드 실행 결과 조회 성공"),
    PROBLEM_SUBMIT_RESULT_SUCCESS(HttpStatus.OK, "PROBLEM_205", "문제 코드 채점 결과 조회 성공"),
    PROBLEM_SUBMIT_HISTORY_SUCCESS(HttpStatus.OK, "PROBLEM_206", "사용자 문제 제출 기록 조회 성공"),
    PROBLEM_TEMP_SAVE_SUCCESS(HttpStatus.OK, "PROBLEM_207", "임시 저장이 완료되었습니다."),
    PROBLEM_LIST_SUCCESS(HttpStatus.OK, "PROBLEM_209", "전체 문제 목록 조회를 성공했습니다."),
    PROBLEM_DIFFICULTY_LIST_SUCCESS(HttpStatus.OK, "PROBLEM_210", "난이도별 문제 목록 조회를 성공했습니다."),

    // bookmark
    BOOKMARK_ADD_SUCCESS(HttpStatus.OK, "BOOKMARK_200", "찜 추가 성공"),
    BOOKMARK_DELETE_SUCCESS(HttpStatus.OK, "BOOKMARK_201", "찜 삭제 성공"),
    BOOKMARK_LIST_SUCCESS(HttpStatus.OK, "BOOKMARK_202", "찜 목록 조회 성공"),

    // cs problem
    CS_PROBLEM_RANDOM_LIST_SUCCESS(HttpStatus.OK, "CS_PROBLEM_200", "CS 문제 랜덤 조회 성공"),

    // ai code review
    AI_CODE_REVIEW_POST_SUCCESS(HttpStatus.OK, "AI_CODE_REVIEW_200", "AI 코드 리뷰 요청을 성공했습니다."),
    AI_CODE_REVIEW_GET_SUCCESS(HttpStatus.OK, "AI_CODE_REVIEW_201", "AI 코드 리뷰 상태 및 결과 조회를 성공했습니다")

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