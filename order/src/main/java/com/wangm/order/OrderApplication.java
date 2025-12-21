package com.wangm.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wangmeng
 * @since 2024/9/4
 */
@EnableTransactionManagement
@MapperScan("com.wangm.order.mapper")
@SpringBootApplication
@MapperScan("com.wangm.order.mapper")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
