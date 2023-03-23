package com.musinsa.point.api.domain.entity;

import com.musinsa.point.api.converter.BooleanToYnConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
public class PointType extends AuditTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint")
    private Long seq;

    @NotBlank
    @Column(columnDefinition = "varchar(10)", nullable = false, unique = true)
    private String pointType; // 포인트 타입

    @NotBlank
    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String pointTypeName; // 포인트 타입명

    @NotNull
    @Column(columnDefinition = "float", nullable = false)
    private Float rate;

    @Column(columnDefinition = "smallint default 365", nullable = false)
    private Integer expirePeriod; // 포인트 유효기간

    @Convert(converter = BooleanToYnConverter.class)
    @Column(columnDefinition = "char(1)", nullable = false)
    private Boolean isCash; // 유/무료 포인트

    @NotBlank
    @Column(columnDefinition = "char(1) default 'Y'", nullable = false)
    private String useYn; // 사용 여부
}
