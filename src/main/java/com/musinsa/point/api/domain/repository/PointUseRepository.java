package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.PointUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointUseRepository extends JpaRepository<PointUse, Long> {

    PointUse findByMemberIdAndOrderNo(Long memberId, Long orderNo);
}
