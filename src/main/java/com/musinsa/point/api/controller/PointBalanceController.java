package com.musinsa.point.api.controller;

import com.musinsa.point.api.model.dto.PointBalanceDto;
import com.musinsa.point.api.service.PointBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트")
@RequiredArgsConstructor
@RequestMapping("/point/balance")
@RestController
public class PointBalanceController {

    private final PointBalanceService pointBalanceService;

    @Operation(description = "포인트 잔액 조회", parameters = {
        @Parameter(name = "member", description = "회원 ID", in = ParameterIn.QUERY)
    }, responses = {
        @ApiResponse(description = "포인트 잔액 정보", responseCode = "200", content = @Content(schema = @Schema(implementation = PointBalanceDto.class)))
    })
    @GetMapping
    public PointBalanceDto findPointBalance(@RequestParam(value = "member", required = false, defaultValue = "0") Long memberId) {
        return pointBalanceService.getPointBalance(memberId);
    }
}
