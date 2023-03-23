package com.musinsa.point.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table
@Entity
public class PointBalance extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long seq;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false, unique = true)
    private Long memberId; // 회원 ID

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long freePoint; // 무료 포인트

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long cashPoint; // 유료 포인트


    public long getFreePoint() {
        return this.freePoint;
    }

    public long getCashPoint() {
        return this.cashPoint;
    }

    public long getTotalPoint() {
        return this.freePoint + this.cashPoint;
    }

    public void addFreePoint(long freePoint) {
        this.freePoint += freePoint;
    }

    public void addCashPoint(long cashPoint) {
        this.cashPoint += cashPoint;
    }
}
