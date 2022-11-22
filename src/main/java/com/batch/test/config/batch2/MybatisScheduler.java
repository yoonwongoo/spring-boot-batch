package com.batch.test.config.batch2;


import com.batch.test.config.batch3.BatchUserGradeAndInsertPointConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.Loader;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RequiredArgsConstructor
@Slf4j
@Component
public class MybatisScheduler {


    private final JobLauncher jobLauncher;

    private final BatchUserGradeAndInsertPointConfiguration batchUserGradeAndInsertPointConfiguration;

    @Scheduled(cron = "0 24 22 * * *")

    public void jobRun(){
        System.out.println("스케줄러 실행된다");
        long time= System.currentTimeMillis();



        Map<String, JobParameter> parameterMap = new HashMap<>();
        parameterMap.put("time", new JobParameter(time));
        JobParameters jobParameters = new JobParameters(parameterMap);

        try{
            jobLauncher.run(batchUserGradeAndInsertPointConfiguration.userGradeAndInsertPointJob(),jobParameters );
        }catch (JobExecutionAlreadyRunningException|JobInstanceAlreadyCompleteException|
        JobParametersInvalidException|JobRestartException e){
            log.error(e.getMessage());
        }

    }
}
