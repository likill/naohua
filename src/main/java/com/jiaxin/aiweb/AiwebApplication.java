package com.jiaxin.aiweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jiaxin.aiweb.mapper")
public class AiwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiwebApplication.class, args);
    }

}
