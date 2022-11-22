package com.batch.test.config.batch2;


import com.batch.test.dto.UpdateUserGrade;
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
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*월별 회원의 주문금액에 맞게 회원등급 하향,상향 조절
 * reader,processor,writer 네이밍은 해당 job(행위)의 name을 사용 ex) updateUserGrade
 * */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class MybatisBatchUserGradeConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public Job updateUserGradeJob() {

        return this.jobBuilderFactory.get("updateUserGradeJob")
                .start(this.updateUserGradeStep())
                .build();
    }


    @Bean
    public Step updateUserGradeStep() {
        log.info("step실행");

        return stepBuilderFactory.get("updateUserGradeStep")
                .<UserTotalPrice, UpdateUserGrade>chunk(10)
                .reader(this.updateUserGradeItemReader())
                .processor(this.updateUserGradeItemProcessor())
                .writer(this.updateUserGradeItemWriter())
                .build();
    }


    /*회원의 아이디와 이번달 총 가격*/
    @Bean
    public MyBatisPagingItemReader<UserTotalPrice> updateUserGradeItemReader() {
        log.info("reader실행");


        MyBatisPagingItemReader myBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<UserTotalPrice>()
                .pageSize(10)
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.batch.test.mapper.UserMapper.selectUserTotalPrice")
                .build();
        return myBatisPagingItemReader;
    }

    @Bean
    public ItemProcessor<UserTotalPrice, UpdateUserGrade> updateUserGradeItemProcessor() {

        return up -> up.updateGrade(up.getUserId(), up.getTotalPrice());

    }

    @Bean
    public MyBatisBatchItemWriter<UpdateUserGrade> updateUserGradeItemWriter() {
        log.info("writer실행");

        MyBatisBatchItemWriter<UpdateUserGrade> userGradeMyBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<UpdateUserGrade>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.batch.test.mapper.UserMapper.updateUserGrade")
                .build();
        return userGradeMyBatisBatchItemWriter;

    }


}
