package com.example.workflow;

import java.util.function.Consumer;

public interface NotificationFacade {
    Consumer<ProcessDefinitionRegistrationEvent> receiveProcessDefinitionRegistrationEvent();
    void sendProcessDefinitionRegistrationEvent(ProcessDefinitionRegistrationEvent event);
}
