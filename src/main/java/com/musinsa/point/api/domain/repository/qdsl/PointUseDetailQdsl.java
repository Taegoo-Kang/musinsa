package com.musinsa.point.api.domain.repository.qdsl;

import com.musinsa.point.api.domain.entity.PointUseDetail;
import java.util.List;

public interface PointUseDetailQdsl {

    List<PointUseDetail> findCancelableDetails(final Long pointUseNo);
}
