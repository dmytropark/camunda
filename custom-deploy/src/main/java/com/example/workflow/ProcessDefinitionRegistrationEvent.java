package com.example.workflow;

import lombok.Data;

@Data
public class ProcessDefinitionRegistrationEvent {
    private String name;
    private String description;
    private int version;
}
