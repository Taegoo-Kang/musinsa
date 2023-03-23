package com.musinsa.point.api.domain.entity;

import com.musinsa.point.api.converter.BooleanToYnConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
    @Index(name = "idx_point_member_id", columnList = "memberId")
    , @Index(name = "idx_point_remain_at", columnList = "remainAt")
    , @Index(name = "idx_point_expire_date", columnList = "expireDate")
})
@Entity
public class Point extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long pointNo;

    @Column(columnDefinition = "bigint")
    private Long originPointNo;

    @NotNull
    @Column(columnDefinition = "bigint", nullable = false)
    private Long memberId;

    @NotBlank
    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String pointType;

    @NotBlank
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String description;

    @Column(columnDefinition = "bigint")
    private Long orderNo;

    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long accumulateAt;

    @Setter
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long remainAt;

    @Setter
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private long expireAt;

    @Convert(converter = BooleanToYnConverter.class)
    @Column(columnDefinition = "char(1)", nullable = false)
    private boolean isCash;

    @NotNull
    @Column(columnDefinition = "date default CURRENT_DATE", nullable = false)
    private LocalDate saveDate;

    @NotNull
    @Column(columnDefinition = "date", nullable = false)
    private LocalDate expireDate;

    @Builder
    private Point(Long pointNo, Long originPointNo, Long memberId, String pointType, String description, Long orderNo, long accumulateAt, long remainAt, Boolean isCash, LocalDate expireDate) {
        this.pointNo = pointNo;
        this.originPointNo = originPointNo;
        this.memberId = memberId;
        this.pointType = pointType;
        this.description = description;
        this.orderNo = orderNo;
        this.accumulateAt = accumulateAt;
        this.remainAt = remainAt;
        this.expireAt = 0L;
        this.isCash = Boolean.TRUE.equals(isCash);
        this.saveDate = LocalDate.now();
        this.expireDate = expireDate;
    }

    public void usePoint(final long useAt) {
        this.remainAt -= useAt;
    }
}
