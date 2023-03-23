package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.PointHistory;
import com.musinsa.point.api.model.dto.PointLogParam;
import com.musinsa.point.api.model.dto.PointLogVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    @Query(value =
        """
        SELECT
            T.point_status AS pointStatus
            , T.point_no AS pointNo
            , T.point_use_no AS pointUseNo
            , SUM(T.point_at) AS pointAt
            , MIN(T.created_dt) AS createdDt
        FROM (
            SELECT
                CASE ph.point_status
                    WHEN 'C' THEN 'U'
                    ELSE ph.point_status 
                END AS point_status,
                CASE ph.point_status
                    WHEN 'S' THEN ph.point_no
                    WHEN 'X' THEN ph.point_no
                    ELSE 0
                END AS point_no, 
                CASE ph.point_status
                    WHEN 'U' THEN ph.point_use_no
                    WHEN 'C' THEN ph.point_use_no
                    ELSE 0 
                END AS point_use_no,
                CASE ph.point_status
                    WHEN 'X' THEN (ph.point_at * -1)
                    WHEN 'C' THEN (ph.point_at * -1)
                    ELSE ph.point_at 
                END AS point_at,
                ph.created_dt
            FROM point_history ph
            WHERE ph.member_id = :#{#param.memberId}
            AND ph.point_status IN :#{#param.searchType}
            AND ph.created_dt BETWEEN :#{#param.startDt} AND :#{#param.endDt}
        ) AS T
        GROUP BY T.point_status, T.point_no, T.point_use_no
        ORDER BY MIN(T.created_dt) ASC
        """
    , nativeQuery = true)
    Slice<PointLogVo> findPointLogs(@Param("param") PointLogParam param, Pageable pageable);
}
