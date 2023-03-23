package com.musinsa.point.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_point_use_member_id", columnList = "memberId")
})
@Entity
public class PointUse extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long pointUseNo;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long memberId;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long orderNo;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long freePointAt;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long cashPointAt;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long cancelFreePointAt;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long cancelCashPointAt;

    @NotNull
    @Column(columnDefinition = "date default CURRENT_DATE", nullable = false)
    private LocalDate useDate;

    @Builder
    private PointUse(Long pointUseNo, Long memberId, long orderNo, long freePointAt, Long cashPointAt) {
        this.pointUseNo = pointUseNo;
        this.memberId = memberId;
        this.orderNo = orderNo;
        this.freePointAt = freePointAt;
        this.cashPointAt = cashPointAt;
        this.cancelFreePointAt = 0L;
        this.cancelCashPointAt = 0L;
        this.useDate = LocalDate.now();
    }

    public long getTotalUsedAt() {
        return this.freePointAt + this.cashPointAt - this.cancelFreePointAt - this.cancelCashPointAt;
    }

    public void cancelPoint(long freePointAt, long cashPointAt) {
        this.cancelFreePointAt += freePointAt;
        this.cancelCashPointAt += cashPointAt;
    }
}
