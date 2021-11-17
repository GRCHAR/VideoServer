package com.forum.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Administrator
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages={"com.forum.video.*"})
@EnableTransactionManagement
@MapperScan("com.forum.video.dao")
@ComponentScan(basePackages = {"com.forum"})
@Configuration
public class VideoApplication {

    public static void main(String[] args) {

        SpringApplication.run(VideoApplication.class, args);


    }

}
