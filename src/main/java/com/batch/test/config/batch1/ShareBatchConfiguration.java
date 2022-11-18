package com.batch.test.config.batch1;


import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/*tasklet과 chunk 실행해보기 예제*/
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ShareBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /*tesklet 사용 Job*/
    @Bean
    public Job taskletProcessJob(){

        return jobBuilderFactory.get("taskletProcessJob")
                .incrementer(new RunIdIncrementer())
                .start(this.taskletProcessStep())
                .build();
    }

    @Bean
    public Step taskletProcessStep(){

        return stepBuilderFactory.get("taskletProcessStep")
               // .tasklet(this.tasklet())
                .tasklet(this.tasklet2(null))
                .build();

    }


    /*tasklet*/
    public Tasklet tasklet(){
        return (contribution,chunkContext)->{
                List<String> itemList=getItemList();
                log.info("itemList : size{}", itemList.size());

            return RepeatStatus.FINISHED;
        };
    }

    /*chunk처럼 만든 tasklet*/
    @Bean
    @StepScope
    public Tasklet tasklet2(@Value("#{jobParameter[chunkSize]}")String parameter){

        List<String> itemList = getItemList();

        return (contribution,chunkContext)->{
//      스코프를 사용하지않을 경우
          StepExecution stepExecution = contribution.getStepExecution(); //읽은 아이템을 조회할 수 있다,
//        JobParameters jobParameters = stepExecution.getJobParameters(); //step의 job으로 들어온 파라미터를 받는다.
//        String jobParameterValue=jobParameters.getString("chunkSize","10");
        int chunkSize = StringUtils.isNotEmpty(parameter)? Integer.parseInt(parameter):10;
        //int chunkSize =10; parameter안 받을 경우 강제로 주입
        int readIndex = stepExecution.getReadCount(); //현재까지 읽은 인덱스// 예를 들면 현재 10까지 읽었으면
        int unreadIndex = readIndex+chunkSize;  //이제 읽어야하는 인덱스// 여기서 +10 범위를 하면 20이다

            if(readIndex>=itemList.size()){

                return RepeatStatus.FINISHED;
            }

           List<String> subList = itemList.subList(readIndex,unreadIndex);
            log.info("subList:size {}",subList.size());

            stepExecution.setReadCount(unreadIndex);
            return RepeatStatus.CONTINUABLE;
        };

    }

    /*chunk 사용 Job*/
    @Bean
    public Job chunkProcessJob(){

        return jobBuilderFactory.get("chunkProcessJob")
                .incrementer(new RunIdIncrementer())
                .start(this.chunkProcessStep(null))
                .build();

    }


    @Bean
    @JobScope
    public Step chunkProcessStep(@Value("#{jobParameters[chunkSize]}")String jobParameter){

        return  stepBuilderFactory.get("chunkProcessStep")
                .<String,String>chunk(StringUtils.isNotEmpty(jobParameter)?Integer.parseInt(jobParameter):10)/*<input,output>*/
                .reader(this.itemReader())
                .processor(this.itemProcessor())
                .writer(this.itemWriter())
                .build();

    }
    public ListItemReader<String> itemReader(){

        return new ListItemReader(getItemList());
    }

    public ItemProcessor<String,String> itemProcessor(){

         return (item) -> item + "hello";
        //null이면 writer로 안넘어간다.
    }

    public ItemWriter itemWriter(){
        return (item)-> log.info("chunkSize: {}",item.size());
       // return (item) -> item.forEach(i->{ log.info(i.toString()); });


    }






    /*임의의 listData*/

    public List<String> getItemList(){
        List itemList = new ArrayList();
        for(int i =0; i<100; i++){

            itemList.add(i+"아이템 생성중");
        }
        return itemList;
    }
}
