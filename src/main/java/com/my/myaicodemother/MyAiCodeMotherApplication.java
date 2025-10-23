package com.my.myaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.my.myaicodemother.mapper")
public class MyAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyAiCodeMotherApplication.class, args);
    }

}
