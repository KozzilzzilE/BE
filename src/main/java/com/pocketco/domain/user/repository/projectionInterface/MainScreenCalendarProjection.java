package com.pocketco.domain.user.repository.projectionInterface;

import java.time.LocalDate;

// DB 결과를 필요한 필드만 가볍게 가져오기 위해 Projection Interface 사용
// Projection Interface = DB 조회 후 불필요한 값들도 있는 엔티티를 반환 받지 않고 빠르게 필요한 값만 DTO에 바로 매핑
public interface MainScreenCalendarProjection {
    LocalDate getDate();
    Integer getCount();
}