package com.springscheduler.scheduler.job;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Scheduled(fixedRate = 60000) // setiap 1 minit
    public void runJob() throws Exception {
        System.out.println("🕒 Starting CSV import batch...");

        JobParameters params = new JobParametersBuilder()
                .addDate("runDate", new Date())
                .toJobParameters();

        jobLauncher.run(importUserJob, params);
    }
}
