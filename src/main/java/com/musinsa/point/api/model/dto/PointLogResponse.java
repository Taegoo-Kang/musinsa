package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(name = "포인트 이력 응답")
public record PointLogResponse(
    @Schema(description = "포인트 이력") List<PointLogDto> pointLogList
    , @Schema(description = "더보기 여부") boolean hasNext
    , @Schema(description = "페이징 사이즈") int pageSize
    , @Schema(description = "현재 페이지 넘버") long pageNumber
) {}
