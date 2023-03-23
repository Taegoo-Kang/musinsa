package com.musinsa.point.api.service;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.PointHistory;
import com.musinsa.point.api.domain.entity.PointUse;
import com.musinsa.point.api.domain.entity.PointUseDetail;
import com.musinsa.point.api.domain.repository.PointBalanceRepository;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.domain.repository.PointUseDetailRepository;
import com.musinsa.point.api.domain.repository.PointUseRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.dto.PointBalanceDto;
import com.musinsa.point.api.model.dto.PointUseParam;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.model.type.PointStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointUseService {

    private final PointBalanceRepository pointBalanceRepository;
    private final PointRepository pointRepository;
    private final PointUseRepository pointUseRepository;
    private final PointUseDetailRepository pointUseDetailRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final PointBalanceService pointBalanceService;

    /**
     * 포인트 사용
     *
     * @param param - 포인트 사용 파라미터
     */
    @Transactional(rollbackFor = Exception.class)
    public PointBalanceDto usePoint(final PointUseParam param) {
        if (param.useAt() <= 0) {
            return null;
        }

        final var memberId = param.memberId();

        // 가용 포인트 조회
        final Long totalPoint = pointBalanceRepository.findTotalPointByMemberId(memberId);
        if (totalPoint == null || totalPoint < param.useAt()) {
           throw new PointApiException(ErrorType.OVER_ENABLE_POINT);
        }

        // 포인트 사용 처리
        List<PointUseDetail.PointUseDetailBuilder> pointUseDetailList = new ArrayList<>();
        var freeUseAt = 0L;
        var cashUseAt = 0L;
        var useRemainAt = param.useAt();
        var pointList = pointRepository.findUsablePoints(memberId);
        for (var point : pointList) {
            // 사용 가능 금액
            final var remainAt = point.getRemainAt();
            final var useAt = (remainAt < useRemainAt) ? remainAt : useRemainAt;

            // 포인트 사용 처리
            point.usePoint(useAt);
            useRemainAt -= useAt;
            if (point.isCash()) {
                cashUseAt += useAt;
            } else {
                freeUseAt += useAt;
            }

            // 포인트 사용 상세 정보
            pointUseDetailList.add(PointUseDetail.builder()
                .point(point)
                .pointAt(useAt)
            );

            // 사용 완료
            if (useRemainAt == 0) {
                break;
            }
        }

        // 유효기간이 지난 포인트 사용
        if (useRemainAt > 0) {
            throw new PointApiException(ErrorType.USE_EXPIRED_POINT);
        }

        // 포인트 사용 저장
        var pointUse = pointUseRepository.save(PointUse.builder()
            .memberId(memberId)
            .orderNo(param.orderNo())
            .freePointAt(freeUseAt)
            .cashPointAt(cashUseAt)
            .build());

        // 포인트 사용 상세 저장
        for (var detailBuilder : pointUseDetailList) {
            var pointUseDetail = pointUseDetailRepository.save(detailBuilder
                .pointUseNo(pointUse.getPointUseNo())
                .build()
            );

            // 포인트 이력 저장
            pointHistoryRepository.save(PointHistory.builder()
                .memberId(memberId)
                .pointNo(pointUseDetail.getPoint().getPointNo())
                .pointUseNo(pointUse.getPointUseNo())
                .pointStatus(PointStatus.USE)
                .pointAt(pointUseDetail.getPointAt())
                .build());
        }

        // 잔여 포인트 차감
        return pointBalanceService.minusPointBalance(memberId, freeUseAt, cashUseAt);
    }

    /**
     * 포인트 사용 취소
     *
     * @param param - 포인트 사용 취소 파라미터
     */
    @Transactional(rollbackFor = Exception.class)
    public PointBalanceDto cancelUsedPoint(final PointUseParam param) {
        final var memberId = param.memberId();

        PointUse pointUse = pointUseRepository.findByMemberIdAndOrderNo(memberId, param.orderNo());
        if (pointUse == null) {
            throw new PointApiException(ErrorType.NOT_FOUND_USED_POINT);
        }

        // 사용 취소 금액이 사용 금액보다 큰 경우
        if (pointUse.getTotalUsedAt() < param.useAt()) {
            throw new PointApiException(ErrorType.OVER_CANCEL_USED_POINT);
        }

        // 포인트 사용 취소 처리
        var cancelFreeUsedAt = 0L;
        var cancelCashUsedAt = 0L;
        var cancelRemainAt = param.useAt();
        var pointUseDetailList = pointUseDetailRepository.findCancelableDetails(pointUse.getPointUseNo());
        for (var pointUseDetail : pointUseDetailList) {
            // 취소 가능 금액
            final var remainAt = pointUseDetail.getPointAt() - pointUseDetail.getCancelPointAt();
            final var cancelAt = (remainAt < cancelRemainAt) ? remainAt : cancelRemainAt;

            // 포인트 사용 취소
            var point = this.getOriginPoint(pointUseDetail.getPoint());
            cancelRemainAt -= cancelAt;
            if (point.isCash()) {
                cancelCashUsedAt += cancelAt;
            } else {
                cancelFreeUsedAt += cancelAt;
            }

            // 포인트 사용 취소 상세 정보 업데이트
            pointUseDetail.cancelPoint(cancelAt);

            // 취소 포인트 재적립
            var cancelPoint = pointRepository.save(Point.builder()
                    .originPointNo(point.getPointNo())
                    .memberId(memberId)
                    .pointType(point.getPointType())
                    .description(point.getDescription())
                    .orderNo(point.getOrderNo())
                    .remainAt(cancelAt)
                    .isCash(point.isCash())
                    .expireDate(point.getExpireDate())
                .build());

            // 포인트 이력 저장
            pointHistoryRepository.save(PointHistory.builder()
                .memberId(memberId)
                .pointNo(cancelPoint.getPointNo())
                .pointUseNo(pointUse.getPointUseNo())
                .pointStatus(PointStatus.CANCEL)
                .pointAt(cancelAt)
                .build());

            // 취소 완료
            if (cancelRemainAt == 0) {
                break;
            }
        }

        /*
          * Data validation check
         */
        if (cancelRemainAt > 0) {
            throw new PointApiException(ErrorType.FAIL_CANCEL_USED_POINT);
        }

        // 포인트 사용 취소 정보 업데이트
        pointUse.cancelPoint(cancelFreeUsedAt, cancelCashUsedAt);

        // 잔여 포인트 증가
        return pointBalanceService.plusPointBalance(memberId, cancelFreeUsedAt, cancelCashUsedAt);
    }

    /*
     * 최초 적립 정보 조회
     */
    private Point getOriginPoint(Point point) {
        Long originPointNo = point.getOriginPointNo();
        if (originPointNo == null || originPointNo.equals(0L)) {
            return point;
        }

        return pointRepository.findById(originPointNo)
            .orElseThrow(() -> new PointApiException(ErrorType.NOT_FOUND_SAVE_POINT));
    }
}
