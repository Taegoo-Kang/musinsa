package com.musinsa.point.api.domain.repository.qdsl;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.QPoint;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PointQdslImpl implements PointQdsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Point> findUsablePoints(final Long memberId) {

        final var point = QPoint.point;

        // 유효기간이 짧은 순, 무료포인트 먼저 사용
        return jpaQueryFactory
            .selectFrom(point)
            .where(point.memberId.eq(memberId)
                .and(point.remainAt.gt(0))
                .and(point.expireDate.goe(LocalDate.now()))
            )
            .orderBy(point.expireDate.asc(), point.isCash.asc())
            .fetch();
    }

    @Override
    public List<Point> findExpiredPoints() {

        final var point = QPoint.point;
        final var now = LocalDate.now();

        return jpaQueryFactory
            .selectFrom(point)
            .where(point.remainAt.gt(0L)
                .and(point.expireDate.lt(now))
            )
            .orderBy(point.pointNo.asc())
            .fetch();
    }
}
