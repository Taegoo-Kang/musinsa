package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.PointType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointTypeRepository extends JpaRepository<PointType, Long> {

    @Query("SELECT pt FROM PointType pt WHERE pt.pointType = :pointType AND pt.useYn = 'Y'")
    PointType findEnabledByPointType(@Param("pointType") String pointType);
}
