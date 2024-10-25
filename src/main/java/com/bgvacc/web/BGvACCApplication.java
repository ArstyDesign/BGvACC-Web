package com.bgvacc.web;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.api.MetarApi;
import com.bgvacc.web.api.discord.DiscordNotifyApi;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.beans.Version;
import com.bgvacc.web.utils.*;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  public AirportUtils airportUtils() {
    return new AirportUtils();
  }

  @Bean
  public URLUtils uRLUtils() {
    return new URLUtils();
  }

  @Bean
  public Version version() {
    return new Version();
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

  @Bean
  public VatEudCoreApi vatEudCoreApi() {
    return new VatEudCoreApi();
  }

  @Bean
  public DiscordNotifyApi discordNotifyApi() {
    return new DiscordNotifyApi();
  }

  @Bean
  public Utils utils() {
    return new Utils();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
