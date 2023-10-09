package com.example.workflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NotificationFacadeImpl implements NotificationFacade {
    private final StreamBridge streamBridge;

    @Override
    @Bean
    public Consumer<ProcessDefinitionRegistrationEvent> receiveProcessDefinitionRegistrationEvent() {
        return event -> System.out.printf("Sending event=%s%n", event);
    }

    @Override
    public void sendProcessDefinitionRegistrationEvent(ProcessDefinitionRegistrationEvent event) {
        streamBridge.send("sendProcessDefinitionRegistrationEvent-out-0", event);
    }
}
