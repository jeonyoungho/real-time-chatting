package org.example.realtimechatting;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class RealTimeChattingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealTimeChattingApplication.class, args);
    }

}
