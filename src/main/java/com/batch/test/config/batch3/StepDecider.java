package com.batch.test.config.batch3;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;


public class StepDecider implements JobExecutionDecider{


    private static final FlowExecutionStatus CONTINUE= new FlowExecutionStatus("CONTINUE");

    private static final String FAILED="FAILED";
    private static final String COMPLETED="COMPLETED";


    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        String batchStatus = stepExecution.getStatus().toString();
        System.out.println(stepExecution.getStatus()+"::::getStatus");
        System.out.println(stepExecution.getStepName()+"::::getStepName");
        if(batchStatus.equals(COMPLETED)){

            return CONTINUE; //다음스텝을 진행해라
        }
        return  new FlowExecutionStatus(FAILED); ////다음스텝을 진행하지마라
    }
}

