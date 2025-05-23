package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lib.entity.dto.entity.CommentRequestEntity;
import lib.entity.dto.repository.CommentRequestRepository;

@Component
public class ApproveCommentDelegate implements JavaDelegate {

    @Autowired
    private CommentRequestRepository commentRequestRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CommentRequestEntity commentRequestEntity = commentRequestRepository
                .findById(Long.valueOf((Integer) execution.getVariable("commentRequestId"))).get();
        commentRequestEntity.setIsChecked((Boolean) execution.getVariable("commentIsOk"));
        commentRequestRepository.save(commentRequestEntity);
    }

}
