package com.musinsa.point.api.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record PointLogParam(
    List<String> searchType
    , long memberId
    , LocalDateTime startDt
    , LocalDateTime endDt
) {}
