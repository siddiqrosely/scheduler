package com.springscheduler.scheduler;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
@Component
public class MyScheduler {

    @PostConstruct
    public void init() {
        System.out.println("MyScheduler initialized 🧠");
    }



    @Scheduled(fixedRate = 5000)
    public void printMessage() {
        Date d = new Date();
        System.out.println("Hello world!"+d.toString());
    }

    @PreDestroy
    public void onShutdown() {
        System.out.println("💀 [SHUTDOWN] MyScheduler sedang mati...");
    }

}
