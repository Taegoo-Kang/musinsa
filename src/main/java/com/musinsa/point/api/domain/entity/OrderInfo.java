package com.musinsa.point.api.domain.entity;

import com.musinsa.point.api.converter.OrderStatusConverter;
import com.musinsa.point.api.model.type.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table
@Entity
public class OrderInfo extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long orderNo;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long memberId;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long paymentAt;

    @Setter
    @Convert(converter = OrderStatusConverter.class)
    @Column(columnDefinition = "char(2) default 'P1'", nullable = false)
    private OrderStatus status; // 주문 상태 (P1: 구매 완료, P2: 구매 확정, P9: 구매 취소)

    @NotNull
    @Column(columnDefinition = "date default CURRENT_DATE", nullable = false)
    private LocalDate orderDate;
}
