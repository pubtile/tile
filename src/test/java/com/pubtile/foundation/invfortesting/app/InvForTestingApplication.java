package com.pubtile.foundation.invfortesting.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pubtile.foundation","com.pubtile.bc"})
@MapperScan({"com.pubtile.foundation.**.mybatis.mapper","com.pubtile.bc.**.mybatis.mapper"})
public class InvForTestingApplication {
    public static void main(String[] args) {
        SpringApplication.run(InvForTestingApplication.class, args);
    }


}
