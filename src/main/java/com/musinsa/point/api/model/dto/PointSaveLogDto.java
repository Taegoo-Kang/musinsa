package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "포인트 적립 정보")
public record PointSaveLogDto(
    @Schema(description = "적립 금액") long pointAt
    , @Schema(description = "포인트 정보") String description
    , @Schema(description = "포인트 부가 정보1") String detailInfo1
    , @Schema(description = "포인트 부가 정보2") String detailInfo2
    , @Schema(description = "적립 일") String saveDate
    , @Schema(description = "만료 일자") String expireDate
) {}
