package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.PointUseDetail;
import com.musinsa.point.api.domain.repository.qdsl.PointUseDetailQdsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointUseDetailRepository extends JpaRepository<PointUseDetail, Long>, PointUseDetailQdsl {

}
