package cn.spicybar.frame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * BootApplication
 *
 * @author hackyo
 * @version V1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
public class FrameApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameApplication.class, args);
    }

}
