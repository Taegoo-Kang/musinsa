package com.musinsa.point.api.controller;

import com.musinsa.point.api.model.dto.PointBalanceDto;
import com.musinsa.point.api.model.dto.PointUseParam;
import com.musinsa.point.api.service.PointUseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트")
@RequiredArgsConstructor
@RequestMapping("/point/use")
@RestController
public class PointUseController {

    private final PointUseService pointUseService;

    @Operation(description = "포인트 사용")
    @PostMapping
    public PointBalanceDto usePoint(@RequestBody @Valid PointUseParam param) {
        return pointUseService.usePoint(param);
    }

    @Operation(description = "포인트 사용 취소")
    @PostMapping("/cancel")
    public PointBalanceDto cancelUsedPoint(@RequestBody @Valid PointUseParam param) {
        return pointUseService.cancelUsedPoint(param);
    }
}
