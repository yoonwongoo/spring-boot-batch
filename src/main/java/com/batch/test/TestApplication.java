package com.batch.test;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@SpringBootApplication
@EnableBatchProcessing
public class TestApplication {

	public static void main(String[] args) {


		System.exit(SpringApplication.exit(SpringApplication.run(TestApplication.class, args)));
	}



	@Bean
	@Primary //기본적으로 스프링에서 설정이 되어서있어서 이걸로 기본전략으로 사용하겠다
	public ThreadPoolTaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setThreadNamePrefix("추가되는 thread-");
		taskExecutor.initialize();

		return taskExecutor;
	}
}
