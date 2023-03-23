package com.musinsa.point.api.domain.repository.qdsl;

import com.musinsa.point.api.domain.entity.PointUseDetail;
import com.musinsa.point.api.domain.entity.QPoint;
import com.musinsa.point.api.domain.entity.QPointUseDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PointUseDetailQdslImpl implements PointUseDetailQdsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PointUseDetail> findCancelableDetails(final Long pointUseNo) {

        final var pointUseDetail = QPointUseDetail.pointUseDetail;
        final var point = QPoint.point;

        // 사용과 반대 - 유효기간이 긴 순, 유료포인트 먼저 취소
        return jpaQueryFactory
            .selectFrom(pointUseDetail)
            .join(pointUseDetail.point, point)
            .where(pointUseDetail.pointUseNo.eq(pointUseNo)
                .and(pointUseDetail.pointAt.gt(pointUseDetail.cancelPointAt))
            )
            .orderBy(point.expireDate.desc(), point.isCash.desc())
            .fetch();
    }
}
