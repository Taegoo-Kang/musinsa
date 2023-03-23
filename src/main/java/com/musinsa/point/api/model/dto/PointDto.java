package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "포인트")
public record PointDto(
    @Schema(description = "회원 ID") long memberId
    , @Schema(description = "포인트 정보") String description
    , @Schema(description = "적립 금액") long pointAt
    , @Schema(description = "만료 일자") String expireDate
) {
    public static PointDto none(final long memberId) {
        return new PointDto(memberId, null, 0L, null);
    }
}
