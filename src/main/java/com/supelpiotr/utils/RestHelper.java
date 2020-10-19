package com.supelpiotr.utils;

import com.supelpiotr.rate.data.Rate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestHelper {

    private static final Logger log = LoggerFactory.getLogger(RestHelper.class);

    public static void main(String[] args) {
        SpringApplication.run(RestHelper.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public Rate get(RestTemplate restTemplate) {
        Rate rate = restTemplate.getForObject(
                "http://api.nbp.pl/api/exchangerates/rates/A/USD?HTTP Accept=JSON", Rate.class);
        return rate;
    }
}