package com.example.workflow;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Alternative to BpmnDeployFilter filter approach.
 * Filter approach is better because from Camunda Modeler you can't put your custom REST endpoint
 * to deploy your current diagram.
 */
@RestController
@RequestMapping("/custom-engine-rest")
@RequiredArgsConstructor
public class CustomRestController {
    private final RepositoryService repositoryService;

    /**
     * Wrapper for default /engine-rest/deployment/create.
     * Uses default /engine-rest/process-definition via Java API to get all active process definitions.
     * Then sends it to down stream microservice via RabbitMQ.
     * @param file - bpmn xml file
     */
    @PostMapping("/deployment/create")
    public Map<String, String> deploy(
            @RequestPart("deployment-source") String deploymentSource,
            @RequestPart("deployment-name") String deploymentName,
            @RequestPart MultipartFile file) throws IOException {
        var deploymentId = deployBpmn(deploymentSource, deploymentName, file);
        var processDefinitions = getAllActiveProcessDefinitions();
        notifyDownStreamMicroService(processDefinitions);
        return prepareResponse(deploymentId);
    }

    private Map<String, String> prepareResponse(String deploymentId) {
        return Collections.singletonMap("deploymentId", deploymentId);
    }

    private void notifyDownStreamMicroService(List<ProcessDefinition> processDefinitions) {
        var processDefinitionRegistrationEvents = processDefinitions.stream()
                .map(processDefinition -> {
                    var target = new ProcessDefinitionRegistrationEvent();
                    BeanUtils.copyProperties(processDefinition, target);
                    return target;
                })
                .toList();

        //TODO Send a message to your down stream service here with
        // "processDefinitionRegistrationEvents" from above as payload.
    }

    private List<ProcessDefinition> getAllActiveProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentTime()
                .desc()
                .list();
    }

    private String deployBpmn(String deploymentSource, String deploymentName, MultipartFile file) throws IOException {
        var deploymentBuilder =
                repositoryService
                        .createDeployment()
                        .source(deploymentSource)
                        .name(deploymentName)
                        .addInputStream(file.getOriginalFilename(), file.getInputStream());
        return deploymentBuilder.deploy()
                .getId();
    }
}