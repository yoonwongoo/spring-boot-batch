package com.batch.test.config.batch2;


import com.batch.test.dto.Shoes;

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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
/* Mybatis 이용하여 임의의 SHOES라는 테이블을 생성하여 insert 및 select */

@RequiredArgsConstructor
@Slf4j
@Configuration
public class MybatisBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    private static final String JOB_NAME = "mybatisJob";

    @Bean(JOB_NAME)
    public Job mybatisJob() throws Exception {

        return this.jobBuilderFactory.get("mybatisJob")
                .incrementer(new RunIdIncrementer())
                .start(this.mybatisStep())
                .build();
    }


    @Bean(JOB_NAME + "_STEP")
    public Step mybatisStep() {

        return stepBuilderFactory.get("mybatisStep")
                .<Shoes, Shoes>chunk(10)
                .reader(this.myBatisPagingItemReader())
                .writer(this.myBatisBatchItemWriter())
                .build();
    }


    /*mybatisPagingReader test*/
    @Bean
    public MyBatisPagingItemReader<Shoes> myBatisPagingItemReader() {

        MyBatisPagingItemReader<Shoes> myBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<Shoes>()
                .pageSize(10)
                .queryId("com.batch.test.mapper.ShoesMapper.selectShoesList")
                .sqlSessionFactory(sqlSessionFactory)
                .build();
        return myBatisPagingItemReader;
    }

    /*myBatisBatchItemWriter Test*/
    @Bean
    public MyBatisBatchItemWriter<Shoes> myBatisBatchItemWriter() {
        MyBatisBatchItemWriter<Shoes> myBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<Shoes>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.batch.test.mapper.ShoesMapper.insertShoes")
                .build();
        return myBatisBatchItemWriter;
    }


}
