package com.musinsa.point.api.controller;

import com.musinsa.point.api.model.dto.PointDto;
import com.musinsa.point.api.model.dto.PointSaveParam;
import com.musinsa.point.api.service.PointSaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "포인트")
@RequiredArgsConstructor
@RequestMapping("/point/save")
@RestController
public class PointSaveController {

    private final PointSaveService pointSaveService;

    @Operation(description = "포인트 적립", responses = {
        @ApiResponse(description = "포인트 적립 결과", responseCode = "200", content = @Content(schema = @Schema(implementation = PointDto.class)))
    })
    @PostMapping
    public PointDto savePoint(@RequestBody @Valid PointSaveParam param) {
        return pointSaveService.savePoint(param);
    }
}
