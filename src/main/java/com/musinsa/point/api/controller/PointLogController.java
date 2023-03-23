package com.musinsa.point.api.controller;

import com.musinsa.point.api.model.dto.PointLogResponse;
import com.musinsa.point.api.model.type.SearchType;
import com.musinsa.point.api.service.PointLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트")
@RequiredArgsConstructor
@RequestMapping("/point/log")
@RestController
public class PointLogController {

    private final PointLogService pointLogService;

    @Operation(description = "포인트 적립 이력 조회", parameters = {
        @Parameter(name = "member", description = "회원 ID", in = ParameterIn.QUERY)
        , @Parameter(name = "searchType", description = "조회 타입(전체/적립/사용)", in = ParameterIn.QUERY)
        , @Parameter(name = "startDate", description = "조회 시작 일자", in = ParameterIn.QUERY)
        , @Parameter(name = "endDate", description = "조회 종료 일자", in = ParameterIn.QUERY)
        , @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY)
        , @Parameter(name = "size", description = "페이지 사이즈", in = ParameterIn.QUERY)
    }, responses = {
        @ApiResponse(description = "포인트 적립 정보", responseCode = "200", content = @Content(schema = @Schema(implementation = PointLogResponse.class)))
    })
    @GetMapping
    public PointLogResponse getPointSaveLog(
        @RequestParam(value = "member", required = false, defaultValue = "0") Long memberId
        , @RequestParam(value = "searchType", required = false, defaultValue = "ALL") SearchType searchType
        , @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate
        , @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
        , @ParameterObject @PageableDefault(size = 20) @Nullable Pageable pageable) {

        // 조회 기간은 기본 30일로 세팅
        if (endDate == null) {
            endDate = startDate == null ? LocalDate.now() : startDate.plusDays(30L);
        }
        if (startDate == null) {
            startDate = endDate.minusDays(30L);
        }

        return pointLogService.findPointLogs(memberId, searchType, startDate, endDate, pageable);
    }
}
