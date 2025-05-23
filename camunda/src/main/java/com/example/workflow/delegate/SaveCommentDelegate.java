package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.workflow.service.CommentService;

import lib.entity.dto.DTO.CommentDTO;

@Component
public class SaveCommentDelegate implements JavaDelegate {

    @Autowired
    CommentService commentService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CommentDTO comment = (CommentDTO) execution.getVariable("commentDTO");
        commentService.save(comment);
    }
}
