package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.PointBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointBalanceRepository extends JpaRepository<PointBalance, Long> {

    PointBalance findByMemberId(Long memberId);

    @Query("SELECT pb.freePoint + pb.cashPoint FROM PointBalance pb WHERE pb.memberId = :memberId")
    Long findTotalPointByMemberId(@Param("memberId") Long memberId);
}
