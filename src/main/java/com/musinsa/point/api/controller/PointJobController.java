package com.musinsa.point.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "포인트")
@RequiredArgsConstructor
@RequestMapping("/point/job")
@RestController
public class PointJobController {

    private final JobOperator jobOperator;
    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;

    @PostMapping("/expire")
    public Long launchPointExpireJob() {
        final var jobName = "pointExpireJob";

        var jobRunId = 0L;
        try {
            Set<Long> jobRunningExecutions = jobOperator.getRunningExecutions(jobName);
            if (!jobRunningExecutions.isEmpty()) {
                return 0L;
            }

            final var job = jobRegistry.getJob(jobName);
            jobRunId = jobLauncher.run(job, new JobParameters()).getId();

        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
        return jobRunId;
    }
}
