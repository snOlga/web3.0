package web.ozon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import web.ozon.DTO.CommentDTO;
import web.ozon.entity.CommentEntity;
import web.ozon.entity.CommentRequestEntity;
import web.ozon.repository.CommentRepository;
import web.ozon.repository.CommentRequestRepository;

@Service
public class CommentRequestService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentRequestRepository commentRequestRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createRequest(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentDTO.getId()).get();
        CommentRequestEntity commentRequestEntity = new CommentRequestEntity(null, commentEntity, false);
        commentRequestRepository.save(commentRequestEntity);
    }
}
