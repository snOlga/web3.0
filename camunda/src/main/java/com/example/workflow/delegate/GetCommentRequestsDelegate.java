package com.example.workflow.delegate;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import lib.entity.dto.entity.CommentRequestEntity;
import lib.entity.dto.repository.CommentRequestRepository;

@Component
public class GetCommentRequestsDelegate implements JavaDelegate {

    @Autowired
    private CommentRequestRepository commentRequestRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<CommentRequestEntity> commentRequests = commentRequestRepository.findAll(PageRequest.of(0, 10)).getContent().stream()
                .filter(req -> req.getIsDeleted() == null || !req.getIsDeleted()).filter(req -> req.getIsChecked() == null || !req.getIsChecked()).toList();
        execution.setVariable("commentRequestsIsEmpty", commentRequests.isEmpty());
    }
}
