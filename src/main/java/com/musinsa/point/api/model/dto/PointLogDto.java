package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "포인트 이력")
public record PointLogDto(
    @Schema(description = "No.") int index
    , @Schema(description = "상태") String pointStatus
    , @Schema(description = "금액") long pointAt
    , @Schema(description = "날짜", format = "yyyy.MM.dd") String date
) {}
