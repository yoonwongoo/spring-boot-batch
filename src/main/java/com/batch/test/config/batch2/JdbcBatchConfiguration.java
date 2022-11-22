package com.batch.test.config.batch2;


import com.batch.test.dto.Shoes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* JDBC를 이용하여 임의의 SHOES라는 테이블을 생성하여 insert 및 select */
@RequiredArgsConstructor
@Getter
@Slf4j
@Configuration
public class JdbcBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private static final String JOB_NAME = "jdbcJob";



    @Bean(JOB_NAME)
    public Job ItemReaderJob() throws Exception {

        return this.jobBuilderFactory.get("jdbcJob")
                .incrementer(new RunIdIncrementer())
                .start(this.jdbcItemWriterStep())
                .build();
    }


    @Bean(JOB_NAME+"_TASKLET_STEP")
    public Step ItemReaderStep() {
        return stepBuilderFactory.get("step")
                .<Shoes, Shoes>chunk(10)
                .reader(new CustomItemReader<Shoes>(this.getShoesList()))
                .writer(this.itemWriter())
                .build();

    }
    @Bean(JOB_NAME+"_CHUNK_STEP")
    public Step jdbcItemReaderStep() throws Exception {
        return stepBuilderFactory.get("jdbcItemReaderStep")
                .<Shoes, Shoes>chunk(10)
                .reader(this.jdbcCursorItemReader())
                .writer(this.itemWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step jdbcItemWriterStep() throws Exception {
        return stepBuilderFactory.get("jdbcItemWriterStep")
                .<Shoes, Shoes>chunk(10)
                .reader(this.jdbcCursorItemReader())
                .writer(this.jdbcBatchItemWriter())
                .build();

    }

    /*ItemWriter 값 확인로그용 writer*/
    @Bean
    @StepScope
    public ItemWriter<Shoes> itemWriter() {
        return shoesItem -> log.info(shoesItem.stream().map(Shoes::getShoesName)
                .collect(Collectors.joining(", ")));


    }

    /*jdbcItemWriter*/
    @Bean
    @StepScope
    private JdbcBatchItemWriter<Object> jdbcBatchItemWriter() {

        JdbcBatchItemWriter<Object> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into shoes(shoesBrand,shoesName,shoesSize) values(:shoesBrand, :shoesName,:shoesSize)")
                .build();
        jdbcBatchItemWriter.afterPropertiesSet();
        return jdbcBatchItemWriter;
    }


    /*jdbcCursorReader*/
    @Bean
    @StepScope
    public JdbcCursorItemReader<Shoes> jdbcCursorItemReader() throws Exception {

        JdbcCursorItemReader<Shoes> itemReader = new JdbcCursorItemReaderBuilder<Shoes>()
                .name("jdbcCursorItemReader")
                .dataSource(dataSource)
                .sql("select id,shoesBrand,shoesName,shoesSize from shoes")
                .rowMapper(((rs, rowNum) ->
                        new Shoes(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4))
                ))
                .build();
        itemReader.afterPropertiesSet();
        return itemReader;
    }
    @Bean
    @StepScope
    public ItemReader<Shoes> itemReader() {

        return new CustomItemReader(getShoesList());
    }

    /*임의 생성*/
    public List<Shoes> getShoesList() {
        List<Shoes> shoesList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            shoesList.add(new Shoes(i, "나이키", "조던" + i, i));

        }
        return shoesList;
    }


}
