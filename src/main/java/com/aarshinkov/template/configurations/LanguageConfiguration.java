package com.aarshinkov.template.configurations;

import com.aarshinkov.template.utils.AppConstants;
import java.util.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Configuration
public class LanguageConfiguration implements WebMvcConfigurer {

  @Bean(name = "localeResolver")
  public CookieLocaleResolver cookieLocaleResolver() {
    
    CookieLocaleResolver localeResolver = new CookieLocaleResolver();
    localeResolver.setCookieName(AppConstants.LANG_COOKIE_NAME);
    localeResolver.setDefaultLocale(new Locale(AppConstants.LANG_DEFAULT));

    return localeResolver;
  }

  @Bean(name = "localeInterceptor")
  public LocaleChangeInterceptor localeInterceptor() {
    
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName(AppConstants.LANG_COOKIE_NAME);

    return interceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    
    registry.addInterceptor(localeInterceptor());
  }
}
