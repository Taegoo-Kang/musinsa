package com.musinsa.point.api.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
public class PointUseDetail extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long seq;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long pointUseNo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "point_no")
    private Point point;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long pointAt;

    @Setter
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long cancelPointAt;

    public void cancelPoint(long cancelAt) {
        this.cancelPointAt += cancelAt;
    }
}
