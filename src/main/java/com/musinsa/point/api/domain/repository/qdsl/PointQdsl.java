package com.musinsa.point.api.domain.repository.qdsl;

import com.musinsa.point.api.domain.entity.Point;
import java.util.List;

public interface PointQdsl {

    List<Point> findUsablePoints(final Long memberId);

    List<Point> findExpiredPoints();
}
