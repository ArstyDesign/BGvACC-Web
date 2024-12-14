package com.bgvacc.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // Заменя @EnableGlobalMethodSecurity
public class WebSecurityConfig {

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

  /**
   * Security filter chain configuration instead of
   * WebSecurityConfigurerAdapter.
   * @param http
   * @return 
   * @throws java.lang.Exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            .antMatchers("/").permitAll()
            .antMatchers("/login").anonymous()
            .antMatchers("/users/**").authenticated()
            .antMatchers("/events/**").permitAll()
            .antMatchers("/portal/dashboard").authenticated()
            .antMatchers("/portal/trainings/**").hasAnyRole("SYS_ADMIN", "STAFF", "STAFF_DIRECTOR", "STAFF_TRAINING")
            .antMatchers("/portal/users/**").hasAnyRole("SYS_ADMIN", "STAFF", "STAFF_DIRECTOR", "STAFF_TRAINING")
            .antMatchers("/portal/**").authenticated()
            .antMatchers("/profile").authenticated()
            .antMatchers("/atc/status/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .antMatchers("/**").permitAll()
            )
            .exceptionHandling(exception -> exception
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint)
            )
            .logout(logout -> logout
            .logoutUrl("/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .invalidSessionUrl("/login")
            .enableSessionUrlRewriting(false)
            .maximumSessions(5)
            .sessionRegistry(sessionRegistry)
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

    return http.build();
  }

  /**
   * AuthenticationManager bean configuration.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Password encoder bean for encrypting passwords.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Session event publisher for updating the logged-in users list.
   */
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }
}
