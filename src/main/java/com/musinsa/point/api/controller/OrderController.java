package com.musinsa.point.api.controller;

import com.musinsa.point.api.model.dto.OrderDto;
import com.musinsa.point.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "구매")
@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    @Operation(description = "구매 이력 조회", parameters = {
        @Parameter(name = "member", description = "회원 ID", in = ParameterIn.QUERY)
    }, responses = {
        @ApiResponse(description = "구매 목록", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class))))
    })
    @GetMapping("/log")
    public List<OrderDto> getOrderLog(@RequestParam(value = "member", required = false, defaultValue = "0") Long memberId) {
        return orderService.findOrderLog(memberId);
    }

    @Operation(description = "구매 확정", parameters = {
        @Parameter(name = "member", description = "회원 ID", in = ParameterIn.QUERY)
        , @Parameter(name = "orderNo", description = "주문 번호", in = ParameterIn.PATH)
    }, responses = {
        @ApiResponse(description = "포인트 적립 금액", responseCode = "200", content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @PostMapping("/confirm/{orderNo}")
    public Long confirmOrder(
            @RequestParam(value = "member", required = false, defaultValue = "0") Long memberId
            , @PathVariable(name = "orderNo") Long orderNo) {

        return orderService.orderConfirm(memberId, orderNo);
    }
}
