package com.daiso.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DaisoShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaisoShopApplication.class, args);
    }

}
