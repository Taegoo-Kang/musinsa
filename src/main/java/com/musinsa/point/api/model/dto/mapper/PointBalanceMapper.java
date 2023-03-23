package com.musinsa.point.api.model.dto.mapper;

import com.musinsa.point.api.domain.entity.PointBalance;
import com.musinsa.point.api.model.dto.PointBalanceDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PointBalanceMapper {

    public static PointBalanceDto toDto(final PointBalance entity) {
        if (entity == null) {
            return null;
        }

        return PointBalanceDto.builder()
            .memberId(entity.getMemberId())
            .freePoint(entity.getFreePoint())
            .cashPoint(entity.getCashPoint())
            .totalPoint(entity.getTotalPoint())
            .build();
    }
}
