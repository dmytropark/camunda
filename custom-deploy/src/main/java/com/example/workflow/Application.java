package com.example.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class Application {
  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public FilterRegistrationBean<BpmnDeployFilter> bpmnDeployFilterRegistration(BpmnDeployFilter bpmnDeployFilter) {
    FilterRegistrationBean<BpmnDeployFilter> registration = new FilterRegistrationBean<>(bpmnDeployFilter);
    registration.addUrlPatterns("/engine-rest/deployment/create");
    return registration;
  }
}