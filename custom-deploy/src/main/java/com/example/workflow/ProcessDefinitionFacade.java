package com.example.workflow;

import java.util.List;

public interface ProcessDefinitionFacade {
    List<ProcessDefinitionRegistrationEvent> getAllActiveProcessDefinitions();
}
