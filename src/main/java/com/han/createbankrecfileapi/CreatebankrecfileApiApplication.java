package com.han.createbankrecfileapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.han.createbankrecfileapi.mapper")
public class CreatebankrecfileApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreatebankrecfileApiApplication.class, args);
    }

}
