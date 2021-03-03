package com.zdhk.ipc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;


//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
@EnableAsync
public class App {

    public static void main(String[] args) {
        //elasticsearch自身的netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");


        SpringApplication.run(App.class, args);

    }



}
