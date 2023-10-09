package com.example.workflow;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * https://refactoring.guru/design-patterns/facade
 */
@Component
@RequiredArgsConstructor
public class ProcessDefinitionFacadeImpl implements ProcessDefinitionFacade {
    private final RepositoryService repositoryService;

    @Override
    public List<ProcessDefinitionRegistrationEvent> getAllActiveProcessDefinitions() {
        return getAllActiveCamundaProcessDefinitions().stream()
                .map(processDefinition -> {
                    var target = new ProcessDefinitionRegistrationEvent();
                    BeanUtils.copyProperties(processDefinition, target);
                    return target;
                })
                .toList();
    }

    private List<ProcessDefinition> getAllActiveCamundaProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentTime()
                .desc()
                .list();
    }
}
