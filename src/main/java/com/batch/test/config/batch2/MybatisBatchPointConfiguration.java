//package com.batch.test.config.batch2;
//
//
//import com.batch.test.dto.InsertUserPoint;
//import com.batch.test.dto.UserGrade;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.User;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.batch.MyBatisBatchItemWriter;
//import org.mybatis.spring.batch.MyBatisPagingItemReader;
//import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
//import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///*월별 회원등급에 맞게 포인트 적립
// * reader,processor,writer 네이밍은 해당 job(행위)의 name을 ex) insertPoint사용
// * */
//@RequiredArgsConstructor
//@Slf4j
//@Configuration
//public class MybatisBatchPointConfiguration {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final SqlSessionFactory sqlSessionFactory;
//
//    private static final String JOB_NAME ="insertPointJob";
//    @Bean(JOB_NAME)
//    public Job insertPointJob() {
//
//        return this.jobBuilderFactory.get("insertPointJob")
//                .start(this.insertPointStep())
//                .build();
//    }
//
//
//    @Bean(JOB_NAME+"_STEP")
//    @JobScope
//    public Step insertPointStep() {
//        return stepBuilderFactory.get("insertPointStep")
//                .<UserGrade, InsertUserPoint>chunk(10)
//                .reader(this.insertPointItemReader())
//                .processor(this.insertPointProcessor())
//                .writer(this.insertPointItemWriter())
//                .build();
//
//    }
//
//
//    @Bean
//    @StepScope
//    public MyBatisPagingItemReader<UserGrade> insertPointItemReader() {
//
//        MyBatisPagingItemReader<UserGrade> userGradeMyBatisPagingItemReader = new MyBatisPagingItemReaderBuilder<UserGrade>()
//                .pageSize(10)
//                .sqlSessionFactory(sqlSessionFactory)
//                .queryId("com.batch.test.mapper.UserMapper.selectUserGrade")
//                .build();
//
//        return userGradeMyBatisPagingItemReader;
//    }
//
//    @Bean
//    @StepScope
//    public ItemProcessor<UserGrade, InsertUserPoint> insertPointProcessor() {
//
//        return userGrade -> userGrade.insertUserPoint(userGrade.getUserId(), userGrade.getGrade());
//    }
//
//
//    @Bean
//    @StepScope
//    public MyBatisBatchItemWriter<InsertUserPoint> insertPointItemWriter() {
//
//        MyBatisBatchItemWriter<InsertUserPoint> pointMyBatisBatchItemWriter = new MyBatisBatchItemWriterBuilder<InsertUserPoint>()
//                .sqlSessionFactory(sqlSessionFactory)
//                .statementId("com.batch.test.mapper.PointMapper.insertPoint")
//                .build();
//
//        return pointMyBatisBatchItemWriter;
//    }
//
//
//}
