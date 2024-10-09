package com.bgvacc.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AccessDeniedHandler accessDeniedHandler;

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private SessionRegistry sessionRegistry;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private LogoutHandler logoutHandler;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider);
  }

  @Bean(name = "authenticationManager")
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Order of antmatchers -> specific ones first, then globals

    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/").permitAll()
            //            .antMatchers("/login", "/signup").anonymous()
            .antMatchers("/login").anonymous()
            .antMatchers("/users/**").authenticated()
            .antMatchers("/events/**").permitAll()
            .antMatchers("/portal/dashboard").authenticated()
            .antMatchers("/portal/**").authenticated()
            .antMatchers("/profile").authenticated()
            .antMatchers("/atc/status/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/**").permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .and()
            .httpBasic()
            .authenticationEntryPoint(authenticationEntryPoint);

    http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .invalidSessionUrl("/login")
            .enableSessionUrlRewriting(false)
            .maximumSessions(5).sessionRegistry(sessionRegistry);
  }

  /**
   * When this bean is registered the logged users list is updated.
   * <br><br>
   * You could see documentation for session registry.
   * <br><br>
   * I also updated the http.sessionManagement()... lines.
   *
   * @return new HttpSessionEventPublisher instance
   */
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }
}
