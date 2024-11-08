package com.bgvacc.web.configurations;

import java.util.*;
import nz.net.ultraq.thymeleaf.layoutdialect.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.dialect.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.*;
import org.thymeleaf.spring5.templateresolver.*;
import org.thymeleaf.spring5.view.*;
import org.thymeleaf.templateresolver.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Configuration
public class ViewConfig implements WebMvcConfigurer {

  @Bean
  public ClassLoaderTemplateResolver cltr() {
    ClassLoaderTemplateResolver cltr = new ClassLoaderTemplateResolver();
    cltr.setCheckExistence(true);
    cltr.setCacheable(false);
    cltr.setPrefix("/mail/");
    cltr.setTemplateMode("HTML");
    cltr.setCharacterEncoding("UTF-8");
    cltr.setOrder(1);

    return cltr;
  }

  @Bean
  public SpringResourceTemplateResolver srtr() {

    SpringResourceTemplateResolver srtr = new SpringResourceTemplateResolver();
    srtr.setPrefix("/WEB-INF/views/");
    srtr.setSuffix(".html");
    srtr.setTemplateMode("HTML");
    srtr.setCharacterEncoding("UTF-8");
    srtr.setCacheable(false);
    srtr.setOrder(2);

    return srtr;
  }

  @Bean
  public LayoutDialect layoutDialect() {
    return new LayoutDialect();
  }

  @Bean
  public SpringSecurityDialect ssd() {
    return new SpringSecurityDialect();
  }

  @Bean
  public Java8TimeDialect java8TimeDialect() {
    return new Java8TimeDialect();
  }

  @Bean
  public Set<IDialect> additionalDialects() {

    Set<IDialect> additionalDialects = new HashSet<>();
    additionalDialects.add(layoutDialect());
    additionalDialects.add(java8TimeDialect());
    additionalDialects.add(ssd());
    return additionalDialects;
  }

  @Bean
  public Set<ITemplateResolver> templateResolvers() {

    Set<ITemplateResolver> templateResolvers = new HashSet<>();
    templateResolvers.add(cltr());
    templateResolvers.add(srtr());

    return templateResolvers;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {

    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolvers(templateResolvers());
    templateEngine.setAdditionalDialects(additionalDialects());

    return templateEngine;
  }

  @Bean
  public ThymeleafViewResolver viewResolver() {

    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    viewResolver.setCharacterEncoding("UTF-8");

    return viewResolver;
  }
}
