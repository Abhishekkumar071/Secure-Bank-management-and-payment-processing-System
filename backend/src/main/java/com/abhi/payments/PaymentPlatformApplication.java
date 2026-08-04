package com.abhi.payments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentPlatformApplication.class, args);
        System.out.println("Payment Platform Backend Started Successfully! 🚀");
    }
}