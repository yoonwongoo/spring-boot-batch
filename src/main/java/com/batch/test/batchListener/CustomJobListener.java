package com.batch.test.batchListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;


@Slf4j
public class CustomJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("job 시작 전 받은 파라메터:{},",jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long time = jobExecution.getEndTime().getTime()-jobExecution.getStartTime().getTime();
        log.info("job 시작 후 받은 파라메터:{}, 소요시간:{}",jobExecution.getJobParameters(),time);

    }
}
