package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class PrintNameDelegate implements JavaDelegate {
  
  @Override
  public void execute(DelegateExecution execution) {
    String name = (String) execution.getVariable("name");
    System.out.println("Hello, " + name + "!");
  }
}