package com.musinsa.point.api.model.dto;

import java.time.LocalDateTime;

public interface PointLogVo {

    String getPointStatus();
    long getPointNo();
    long getPointUseNo();
    long getPointAt();
    LocalDateTime getCreatedDt();
}
