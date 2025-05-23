package com.example.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.workflow.filter.CommentFilter;

import lib.entity.dto.DTO.CommentDTO;

@Component
public class FilterCommentDelegate implements JavaDelegate {

    @Autowired
    private CommentFilter commentFilter;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long productId = Long.valueOf((Integer) execution.getVariable("product_id"));
        Long authorId = Long.valueOf((Integer) execution.getVariable("author_id"));
        String content = (String) execution.getVariable("content");
        Boolean isAnonymous = (Boolean) execution.getVariable("is_anonymous");
        CommentDTO commentDTO = new CommentDTO(null, productId, authorId, content, isAnonymous, false, null, null);
        commentFilter.filter(commentDTO);
        execution.setVariable("commentDTO", commentDTO);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!");
    }

}
