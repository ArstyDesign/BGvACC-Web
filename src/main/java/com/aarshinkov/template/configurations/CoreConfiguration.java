package com.aarshinkov.template.configurations;

import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.*;
import org.springframework.web.servlet.config.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Configuration
public class CoreConfiguration implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    
    registry.addResourceHandler("/resources/**").addResourceLocations("WEB-INF/resources/");
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//    messageSource.setBasenames("classpath:messages/messages");
    messageSource.setBasenames("classpath:messages/messages", "classpath:env");
    messageSource.setDefaultEncoding("UTF-8");

    return messageSource;
  }

  @Override
  public Validator getValidator() {
    
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource());
    return validator;
  }
}
