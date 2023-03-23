package com.musinsa.point.api.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class PointJobConfigure {

    @Bean
    public Job pointExpireJob(JobRepository jobRepository, Step pointExpireStep) {
        return new JobBuilder("pointExpireJob", jobRepository)
            .flow(pointExpireStep)
            .end()
            .build();
    }

    @Bean
    public Step pointExpireStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, Tasklet pointExpireTasklet) {
        return new StepBuilder("pointExpireStep", jobRepository)
            .tasklet(pointExpireTasklet, transactionManager)
            .build();
    }
}
