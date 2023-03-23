package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.repository.qdsl.PointQdsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long>, PointQdsl {

}
