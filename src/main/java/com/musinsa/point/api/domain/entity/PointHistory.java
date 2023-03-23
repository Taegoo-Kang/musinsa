package com.musinsa.point.api.domain.entity;

import com.musinsa.point.api.converter.PointStatusConverter;
import com.musinsa.point.api.model.type.PointStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_point_history_member_id", columnList = "memberId")
    , @Index(name = "idx_point_history_created_dt", columnList = "createdDt")
})
@Entity
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long seq;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long memberId;

    @NotNull
    @Convert(converter = PointStatusConverter.class)
    @Column(columnDefinition = "char(1)", nullable = false)
    private PointStatus pointStatus;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long pointNo;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long pointUseNo;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long pointAt;

    @NotNull
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createdDt;

    @Builder
    private PointHistory(Long memberId, PointStatus pointStatus, Long pointNo, long pointUseNo, Long pointAt) {
        this.memberId = memberId;
        this.pointStatus = pointStatus;
        this.pointNo = pointNo;
        this.pointUseNo = pointUseNo;
        this.pointAt = pointAt;
        this.createdDt = LocalDateTime.now();
    }
}
