package com.musinsa.point.api.service;

import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.model.dto.PointLogDto;
import com.musinsa.point.api.model.dto.PointLogParam;
import com.musinsa.point.api.model.dto.PointLogResponse;
import com.musinsa.point.api.model.type.PointStatus;
import com.musinsa.point.api.model.type.SearchType;
import com.musinsa.point.api.util.DateTimeUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointLogService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointLogResponse findPointLogs(final long memberId, final SearchType searchType, final LocalDate startDate, final LocalDate endDate, final Pageable pageable) {
        if (memberId == 0) {
            return PointLogResponse.builder()
                .pointLogList(Collections.emptyList())
                .hasNext(false)
                .pageSize(pageable.getPageSize())
                .pageNumber(pageable.getPageNumber())
                .build();
        }

        // 파라미터 세팅
        final var statusList = switch (searchType) {
            case ALL -> List.of(PointStatus.SAVE.getCode(), PointStatus.USE.getCode(), PointStatus.CANCEL.getCode(), PointStatus.EXPIRE.getCode());
            case SAVE -> List.of(PointStatus.SAVE.getCode(), PointStatus.USE.getCode());
            case USE -> List.of(PointStatus.CANCEL.getCode(), PointStatus.EXPIRE.getCode());
        };

        final var param = PointLogParam.builder()
            .searchType(statusList)
            .memberId(memberId)
            .startDt(LocalDateTime.of(startDate, LocalTime.MIN))
            .endDt(LocalDateTime.of(endDate, LocalTime.MAX))
            .build();

        // 포인트 이력 조회
        final var pointLog = pointHistoryRepository.findPointLogs(param, pageable);

        // DTO 형식에 맞게 변환
        final var index = new AtomicInteger(1);
        final var pointLogList = pointLog.stream()
            .map(p -> PointLogDto.builder()
                    .index(index.getAndIncrement())
                    .pointStatus(PointStatus.of(p.getPointStatus()).getDescription())
                    .pointAt(p.getPointAt())
                    .date(p.getCreatedDt().format(DateTimeUtils.POINT_DATE_FORMAT))
                    .build()
            )
            .toList();

        return PointLogResponse.builder()
            .pointLogList(pointLogList)
            .hasNext(pointLog.hasNext())
            .pageSize(pageable.getPageSize())
            .pageNumber(pageable.getPageNumber())
            .build();
    }
}
