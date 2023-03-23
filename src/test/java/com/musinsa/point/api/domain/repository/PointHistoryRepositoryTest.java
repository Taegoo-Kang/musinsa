package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.model.dto.PointLogParam;
import com.musinsa.point.api.model.dto.PointLogVo;
import com.musinsa.point.api.model.type.PointStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointHistoryRepositoryTest {

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Test
    void test_findPointLogAll() {
        var param = PointLogParam.builder()
            .searchType(List.of(PointStatus.SAVE.getCode(), PointStatus.USE.getCode(), PointStatus.CANCEL.getCode(), PointStatus.EXPIRE.getCode()))
            .memberId(10001L)
            .startDt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
            .endDt(LocalDateTime.of(2023, 5, 2, 23, 59, 59))
            .build();

        var pageable = PageRequest.of(0, 10);
        Slice<PointLogVo> pointLogs = pointHistoryRepository.findPointLogs(param, pageable);

        var content = pointLogs.getContent();
        assertTrue(content.size() <= pageable.getPageSize());
        assertEquals(PointStatus.USE.getCode(), content.get(2).getPointStatus());
        assertEquals(3000L, content.get(2).getPointAt());
        assertEquals(PointStatus.EXPIRE.getCode(), content.get(3).getPointStatus());
        assertEquals(-48L, content.get(3).getPointAt());
        assertEquals(PointStatus.SAVE.getCode(), content.get(8).getPointStatus());
        assertEquals(10000L, content.get(8).getPointAt());
        assertEquals(PointStatus.USE.getCode(), content.get(9).getPointStatus());
        assertEquals(2000L, content.get(9).getPointAt());
        assertTrue(pointLogs.hasNext());
    }

    @Test
    void test_findPointSaveLog() {
        var param = PointLogParam.builder()
            .searchType(List.of(PointStatus.SAVE.getCode(), PointStatus.EXPIRE.getCode()))
            .memberId(10001L)
            .startDt(LocalDateTime.of(2022, 2, 1, 0, 0, 0))
            .endDt(LocalDateTime.of(2023, 3, 1, 23, 59, 59))
            .build();

        var pageable = PageRequest.of(0, 10);
        Slice<PointLogVo> pointLogs = pointHistoryRepository.findPointLogs(param, pageable);

        var content = pointLogs.getContent();
        assertTrue(pointLogs.get()
            .noneMatch(p -> PointStatus.USE.getCode().equals(p.getPointStatus()) || PointStatus.CANCEL.getCode().equals(p.getPointStatus()))
        );
        assertEquals(PointStatus.EXPIRE.getCode(), content.get(0).getPointStatus());
        assertEquals(-48L, content.get(0).getPointAt());
    }
}
