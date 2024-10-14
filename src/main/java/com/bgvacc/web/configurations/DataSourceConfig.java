package com.bgvacc.web.configurations;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  public AuthenticationProperties authProps;

  @Bean
  public DataSource getDataSource() {
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName("org.postgresql.Driver");
    dataSourceBuilder.url("jdbc:postgresql://" + authProps.getDbHost() + ":" + authProps.getDbPort() + "/" + authProps.getDbSchema());
    dataSourceBuilder.username(authProps.getDbUsername());
    dataSourceBuilder.password(authProps.getDbPassword());
    return dataSourceBuilder.build();
  }
}
