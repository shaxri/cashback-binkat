package com.binkat.cashback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CashbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashbackApplication.class, args);
    }
}