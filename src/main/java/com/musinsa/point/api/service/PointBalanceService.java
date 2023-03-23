package com.musinsa.point.api.service;

import com.musinsa.point.api.domain.entity.PointBalance;
import com.musinsa.point.api.domain.repository.PointBalanceRepository;
import com.musinsa.point.api.model.dto.PointBalanceDto;
import com.musinsa.point.api.model.dto.mapper.PointBalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointBalanceService {

    private final PointBalanceRepository pointBalanceRepository;

    /**
     * 회원 포인트 잔액 조회
     *
     * @param memberId - 회원 Id
     * @return pointBalanceDto
     */
    public PointBalanceDto getPointBalance(final Long memberId) {
        PointBalance balance = pointBalanceRepository.findByMemberId(memberId);
        if (balance == null) {
            return PointBalanceDto.none(memberId);
        }
        return PointBalanceMapper.toDto(balance);
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public PointBalanceDto plusPointBalance(final Long memberId, final long freePoint, final long cashPoint) {
        PointBalance balance = pointBalanceRepository.findByMemberId(memberId);
        if (balance == null) {
            balance = PointBalance.builder()
                .memberId(memberId)
                .freePoint(freePoint)
                .cashPoint(cashPoint)
                .build();

            pointBalanceRepository.save(balance);

        } else {
            balance.addFreePoint(freePoint);
            balance.addCashPoint(cashPoint);
        }

        return PointBalanceMapper.toDto(balance);
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
    public PointBalanceDto minusPointBalance(final Long memberId, final long freePoint, final long cashPoint) {
        var balance = pointBalanceRepository.findByMemberId(memberId);
        balance.addFreePoint(freePoint * -1);
        balance.addCashPoint(cashPoint * -1);

        return PointBalanceMapper.toDto(balance);
    }
}
