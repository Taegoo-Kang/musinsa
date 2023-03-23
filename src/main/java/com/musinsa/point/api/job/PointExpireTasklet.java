package com.musinsa.point.api.job;

import com.musinsa.point.api.domain.entity.PointHistory;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.model.type.PointStatus;
import com.musinsa.point.api.service.PointBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@StepScope
@RequiredArgsConstructor
@Component
public class PointExpireTasklet implements Tasklet {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final PointBalanceService pointBalanceService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final var expiredPointList = pointRepository.findExpiredPoints();
        for (var point : expiredPointList) {

            // 포인트 소멸 처리
            // 원 포인트 정보가 있으면 원 포인트 정보 업데이트
            long expiredPointNo;
            final var expiredAt = point.getRemainAt();
            if (point.getOriginPointNo() != null && point.getOriginPointNo() > 0) {

                final var originPoint = pointRepository.findById(point.getOriginPointNo())
                    .orElseThrow(() -> new PointApiException(ErrorType.NOT_FOUND_SAVE_POINT));

                originPoint.setExpireAt(originPoint.getExpireAt() + expiredAt);
                expiredPointNo = originPoint.getPointNo();

            } else {
                point.setExpireAt(expiredAt);
                expiredPointNo = point.getPointNo();
            }
            point.setRemainAt(0L);

            // 포인트 이력 저장
            pointHistoryRepository.save(PointHistory.builder()
                .memberId(point.getMemberId())
                .pointNo(expiredPointNo)
                .pointStatus(PointStatus.EXPIRE)
                .pointAt(expiredAt)
                .build());

            // 잔여 포인트 차감
            final var expiredFreeAt = point.isCash() ? 0L : expiredAt;
            final var expiredCashAt = point.isCash() ? expiredAt : 0L;
            pointBalanceService.minusPointBalance(point.getMemberId(), expiredFreeAt, expiredCashAt);
        }

        return RepeatStatus.FINISHED;
    }
}
