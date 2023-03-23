package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "포인트 잔액")
public record PointBalanceDto(
    @Schema(description = "회원 ID") long memberId
    , @Schema(description = "무료 포인트") long freePoint
    , @Schema(description = "유료 포인트") long cashPoint
    , @Schema(description = "총 포인트") long totalPoint
) {
    public static PointBalanceDto none(long memberId) {
        return new PointBalanceDto(memberId, 0L, 0L, 0L);
    }
}
