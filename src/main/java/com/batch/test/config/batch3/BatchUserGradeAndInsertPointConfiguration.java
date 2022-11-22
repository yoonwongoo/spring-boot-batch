package com.batch.test.config.batch3;


import com.batch.test.batchListener.CustomStepListener;
import com.batch.test.dto.InsertUserPoint;
import com.batch.test.dto.UpdateUserGrade;
import com.batch.test.dto.UserGrade;
import com.batch.test.dto.UserTotalPrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;

import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class BatchUserGradeAndInsertPointConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final SqlSessionFactory sqlSessionFactory;

    private static final String JOB_NAME = "userGradeAndInsertPointJob";

    @Bean(JOB_NAME)
    public Job userGradeAndInsertPointJob() {

        return this.jobBuilderFactory.get("userGradeAndInsertPointJob")
                .incrementer(new RunIdIncrementer())
                .start(this.updateUserGradeStep())
                .next(stepDecider())
                .from(stepDecider())
                .on("CONTINUE")
                .to(this.insertPointStep())
                .end()
                .build();
    }


    @Bean
    public JobExecutionDecider stepDecider() {

        return new StepDecider();
    }

    @Bean(JOB_NAME + "_STEP")
    @JobScope
    public Step updateUserGradeStep() {
        log.info("step실행");

        return stepBuilderFactory.get("updateUserGradeStep")
                .listener(new CustomStepListener())
                .<UserTotalPrice, UpdateUserGrade>chunk(10)
                .reader(this.updateUserGradeItemReader())
                .processor(this.updateUserGradeItemProcessor())
                .writer(this.updateUserGradeItemWriter())
                .faultTolerant()
                .retry(DeadlockLoserDataAccessException.class)
                .retryLimit(3)
                .build();
    }


    /*회원의 아이디와 이번달 총 가격*/
    @Bean
    @StepScope
    public MyBatisPagingItemReader<UserTotalPrice> updateUserGradeItemReader() {


        MyBatisPagingItemReader myBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<UserTotalPrice>()
                .pageSize(10)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.batch.test.mapper.UserMapper.selectUserTotalPrice")
                .build();
        return myBatisPagingItemReader;
    }

    @Bean
    @StepScope
    public ItemProcessor<UserTotalPrice, UpdateUserGrade> updateUserGradeItemProcessor() {

        return up -> up.updateGrade(up.getUserId(), up.getTotalPrice());

    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter<UpdateUserGrade> updateUserGradeItemWriter() {

        MyBatisBatchItemWriter<UpdateUserGrade> userGradeMyBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<UpdateUserGrade>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.batch.test.mapper.UserMapper.updateUserGrade")
                .build();
        return userGradeMyBatisBatchItemWriter;

    }

    /*------------------------------------------------------------------------------------------------------------------*/
    @Bean
    @JobScope
    public Step insertPointStep() {
        return stepBuilderFactory.get("insertPointStep")
                .listener(new CustomStepListener())
                .<UserGrade, InsertUserPoint>chunk(10)
                .reader(this.insertPointItemReader())
                .processor(this.insertPointProcessor())
                .writer(this.insertPointItemWriter())
                .faultTolerant()
                .retry(DeadlockLoserDataAccessException.class)
                .retryLimit(3)
                .build();

    }


    @Bean
    @StepScope
    public MyBatisPagingItemReader<UserGrade> insertPointItemReader() {

        MyBatisPagingItemReader<UserGrade> userGradeMyBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<UserGrade>()
                .pageSize(10)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.batch.test.mapper.UserMapper.selectUserGrade")
                .build();

        return userGradeMyBatisPagingItemReader;
    }

    @Bean
    @StepScope
    public ItemProcessor<UserGrade, InsertUserPoint> insertPointProcessor() {

        return userGrade -> userGrade.insertUserPoint(userGrade.getUserId(), userGrade.getGrade());
    }


    @Bean
    @StepScope
    public MyBatisBatchItemWriter<InsertUserPoint> insertPointItemWriter() {

        MyBatisBatchItemWriter<InsertUserPoint> pointMyBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<InsertUserPoint>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.batch.test.mapper.PointMapper.insertPoint")
                .build();

        return pointMyBatisBatchItemWriter;
    }


}
