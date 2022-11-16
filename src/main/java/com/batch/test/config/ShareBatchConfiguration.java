package com.batch.test.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ShareBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;



    @Bean
    public Job chuckProcessJob(){

        return jobBuilderFactory.get("chunkProcessJob")
                .incrementer(new RunIdIncrementer())
                .start(this.chuckProcessStep())
                .build();
    }


    @Bean
    public Step chuckProcessStep(){

        return stepBuilderFactory.get("chunkProcessStep")
                .tasklet(this.tasklet())
                .build();

    }


    public Tasklet tasklet(){
        return (contribution,chunkContext)->{
                List<String> itemList=getItemList();
                log.info("itemList : size{}", itemList.size());

            return RepeatStatus.FINISHED;
        };
    }


    public List<String> getItemList(){
        List itemList = new ArrayList();
        for(int i =0; i<100; i++){

            itemList.add(i+"아이템 생성중");
        }
        return itemList;
    }
}
