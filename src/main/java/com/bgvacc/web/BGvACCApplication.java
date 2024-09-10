package com.bgvacc.web;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.api.MetarApi;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class BGvACCApplication {

  public static void main(String[] args) {
    SpringApplication.run(BGvACCApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    return restTemplate;
  }

  @Bean
  public EventApi eventApi() {
    return new EventApi();
  }

  @Bean
  public MetarApi metarApi() {
    return new MetarApi();
  }

  @Bean
  public CoreApi coreApi() {
    return new CoreApi();
  }
}
