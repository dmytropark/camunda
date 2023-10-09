package com.example.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class BpmnDeployFilter implements Filter {
    private final NotificationFacade notificationFacade;
    private final ProcessDefinitionFacade processDefinitionFacade;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("Do response filtering");
        processDefinitionFacade.getAllActiveProcessDefinitions()
                .forEach(notificationFacade::sendProcessDefinitionRegistrationEvent);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
