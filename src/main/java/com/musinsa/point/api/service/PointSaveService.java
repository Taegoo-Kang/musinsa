package com.musinsa.point.api.service;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.PointHistory;
import com.musinsa.point.api.domain.entity.PointType;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.domain.repository.PointTypeRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.dto.PointDto;
import com.musinsa.point.api.model.dto.PointSaveParam;
import com.musinsa.point.api.model.dto.mapper.PointMapper;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.model.type.PointStatus;
import com.musinsa.point.api.util.MathUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
public class PointSaveService {

    private final PointTypeRepository pointTypeRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final PointBalanceService pointBalanceService;

    /**
     * 포인트 적립
     *
     * @param param - 포인트 적립 파라미터
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public PointDto savePoint(final PointSaveParam param) {
        if (param.serviceAt() <= 0) {
            throw new PointApiException(ErrorType.INVALID_SAVE_AMOUNT);
        }

        PointType pointType = pointTypeRepository.findEnabledByPointType(param.pointType());
        if (pointType == null) {
            throw new PointApiException(ErrorType.INVALID_POINT_TYPE);
        }

        // 적립 포인트 설명
        final var description = StringUtils.hasText(param.description())
            ? param.description() : pointType.getPointTypeName();

        // 포인트 적립 금액 계산 (서비스 금액 x 적립률)
        final var pointAt = MathUtils.getPercentage(param.serviceAt(), pointType.getRate())
            .longValue();

        final var memberId = param.memberId();
        // 적립 할 포인트가 0원인 경우
        if (pointAt == 0L) {
            return PointDto.none(memberId);
        }

        // 포인트 저장
        final var point = pointRepository.save(Point.builder()
            .memberId(memberId)
            .pointType(pointType.getPointType())
            .description(description)
            .orderNo(param.orderNo())
            .accumulateAt(pointAt)
            .remainAt(pointAt)
            .isCash(pointType.getIsCash())
            .expireDate(LocalDate.now().plusDays(pointType.getExpirePeriod()))
            .build());

        // 포인트 이력 저장
        pointHistoryRepository.save(PointHistory.builder()
            .memberId(memberId)
            .pointNo(point.getPointNo())
            .pointStatus(PointStatus.SAVE)
            .pointAt(point.getAccumulateAt())
            .build());

        // 잔여 포인트 잔액 증가
        final var freePoint = point.isCash() ? 0L : point.getAccumulateAt();
        final var cashPoint = point.isCash() ? point.getAccumulateAt() : 0L;
        pointBalanceService.plusPointBalance(memberId, freePoint, cashPoint);

        return PointMapper.toDto(point);
    }
}
