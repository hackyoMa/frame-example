package com.github.hackyoma.frameexample.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * BootApplication
 *
 * @author hackyo
 * @date 2018/8/22
 */
@SpringBootApplication
@EnableTransactionManagement
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

}
