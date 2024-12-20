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
            .antMatchers("/events/**").permitAll()
            .antMatchers("/portal/dashboard").authenticated()
            .antMatchers("/portal/events/**/airports/**", "/portal/events/**/positions/**", "/portal/events/**/slots/**").hasAnyRole("SYS_ADMIN", "STAFF_DIRECTOR", "STAFF_EVENTS")
            .antMatchers("/portal/events/**").authenticated()
            .antMatchers("/portal/atc/**").hasAnyRole("ATC_S1", "ATC_S2", "ATC_S3", "ATC_C1", "ATC_C3", "ATC_I1", "ATC_I3")
            .antMatchers("/portal/trainings/**").hasAnyRole("SYS_ADMIN", "STAFF_DIRECTOR", "STAFF_TRAINING")
            .antMatchers("/portal/users/**").hasAnyRole("SYS_ADMIN", "STAFF_DIRECTOR", "STAFF_TRAINING")
            .antMatchers("/portal/blog/**").hasAnyRole("SYS_ADMIN", "STAFF_DIRECTOR", "BLOG_MANAGER")
            .antMatchers("/portal/**").authenticated()
            .antMatchers("/profile").authenticated()
            .antMatchers("/users/**").hasAnyRole("SYS_ADMIN", "STAFF_DIRECTOR", "STAFF_EVENTS")
            .antMatchers("/atc/status/**").permitAll()
            .antMatchers("/atc-reservations/**").hasAnyRole("ATC_S1", "ATC_S2", "ATC_S3", "ATC_C1", "ATC_S3", "ATC_I1", "ATC_I3")
            .antMatchers("/test/**").permitAll()
            .antMatchers("/resources/**").permitAll()
            .antMatchers("/", "/home", "/index").permitAll()
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

    http.headers()
            .frameOptions().sameOrigin();

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
