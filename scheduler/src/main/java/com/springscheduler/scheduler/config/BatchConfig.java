package com.springscheduler.scheduler.config;

import com.springscheduler.scheduler.job.UserItemProcessor;
import com.springscheduler.scheduler.model.Users;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.*;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.*;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Users> reader() {
        return new FlatFileItemReaderBuilder<Users>()
                .name("userItemReader")
                .resource(new ClassPathResource("input/users.csv"))
                .delimited()
                .names("id", "name", "email")
                .linesToSkip(1)
                .targetType(Users.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Users> writer(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcBatchItemWriterBuilder<Users>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO users (id, name, email) VALUES (:id, :name, :email)")
                .namedParametersJdbcTemplate(jdbcTemplate)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Users> reader, JdbcBatchItemWriter<Users> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Users, Users>chunk(10, transactionManager)
                .reader(reader)
                .processor(new UserItemProcessor())
                .writer(writer)
                .build();
    }

    @Bean
    public JobCompletionNotificationListener listener() {
        return new JobCompletionNotificationListener();
    }
}
