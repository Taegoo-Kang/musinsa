package com.musinsa.point.api.model.dto.mapper;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.model.dto.PointDto;
import com.musinsa.point.api.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PointMapper {

    public static PointDto toDto(final Point entity) {
        if (entity == null) {
            return null;
        }

        return PointDto.builder()
            .memberId(entity.getMemberId())
            .description(entity.getDescription())
            .pointAt(entity.getAccumulateAt())
            .expireDate(entity.getExpireDate().format(DateTimeUtils.POINT_DATE_FORMAT))
            .build();
    }
}
