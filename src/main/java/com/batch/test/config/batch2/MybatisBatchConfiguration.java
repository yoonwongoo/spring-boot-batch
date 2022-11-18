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
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class MybatisBatchConfiguration {



    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job mybatisJob() throws Exception {

        return this.jobBuilderFactory.get("mybatisJob")
                .incrementer(new RunIdIncrementer())
                .start(this.mybatisStep())
                .build();
    }

    @Bean
    public Step mybatisStep(){

        return stepBuilderFactory.get("mybatisStep")
                .<Shoes,Shoes>chunk(10)
                .reader(this.myBatisPagingItemReader())
                .writer(this.myBatisBatchItemWriter())
                .build();
    }



    /*mybatisPagingReader*/
    @Bean
    public MyBatisPagingItemReader<Shoes> myBatisPagingItemReader(){
        MyBatisPagingItemReader<Shoes> myBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<Shoes>()
                .queryId("com.batch.test.mapper.ShoesMapper.selectShoesList")
                .sqlSessionFactory(sqlSessionFactory)
                .build();
        return myBatisPagingItemReader;
    }

    /*myBatisBatchItemWriter*/
   @Bean
   public MyBatisBatchItemWriter<Shoes> myBatisBatchItemWriter(){
       MyBatisBatchItemWriter<Shoes> myBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<Shoes>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.batch.test.mapper.ShoesMapper.insertShoes")
                .build();
       return myBatisBatchItemWriter;
   }




}
