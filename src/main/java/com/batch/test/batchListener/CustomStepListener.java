package com.batch.test.batchListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;


@Slf4j
public class CustomStepListener implements StepExecutionListener {


    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("step 시작 전 step 이름:{}",stepExecution.getStepName());

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("step 시작 후 step 이름:{},step 상태:{} ",stepExecution.getStepName(),stepExecution.getStatus());

        return null;
    }
}
