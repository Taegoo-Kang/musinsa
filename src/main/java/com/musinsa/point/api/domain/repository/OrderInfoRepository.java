package com.musinsa.point.api.domain.repository;

import com.musinsa.point.api.domain.entity.OrderInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    List<OrderInfo> findByMemberIdOrderByOrderNoDesc(Long memberId);

    Optional<OrderInfo> findByOrderNoAndMemberId(Long orderNo, Long memberId);
}
