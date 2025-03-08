package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import web.ozon.DTO.CommentDTO;
import web.ozon.DTO.CommentRequestDTO;
import web.ozon.converter.CommentRequestConverter;
import web.ozon.entity.CommentEntity;
import web.ozon.entity.CommentRequestEntity;
import web.ozon.entity.UserEntity;
import web.ozon.repository.CommentRepository;
import web.ozon.repository.CommentRequestRepository;
import web.ozon.repository.UserRepository;

@Service
public class CommentRequestService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentRequestRepository commentRequestRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CommentRequestConverter commentRequestConverter;
    @Autowired
    private UserRepository userRepository;

    private int PAGINATION_STEP = 10;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createRequest(CommentDTO commentDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentDTO.getId()).get();
        CommentRequestEntity commentRequestEntity = new CommentRequestEntity(null, commentEntity, null, null, false);
        commentRequestRepository.save(commentRequestEntity);
        try {
            messagingTemplate.convertAndSend("/topic/comment-request/" + commentRequestEntity.getId());
        } catch (IllegalStateException e) {
            // TODO: handle exception
        }
    }

    public List<CommentRequestDTO> getAll(int from) {
        return commentRequestRepository.findAll(PageRequest.of(from, PAGINATION_STEP)).getContent().stream()
                .map(commentRequestConverter::fromEntity).toList();
    }

    public List<CommentRequestDTO> getAllNotChecked(int from) {
        return getAll(from).stream().filter(req -> !req.getIsChecked()).toList();
    }

    @Transactional
    public CommentRequestDTO update(CommentRequestDTO dto) {
        CommentRequestEntity entity = commentRequestRepository.findById(dto.getId())
                .orElse(null);
        if (entity == null || entity.getIsDeleted())
            return null;

        if (dto.getIsChecked() != null) {
            entity.setIsChecked(dto.getIsChecked());
            UserEntity checker = userRepository.findByLogin(
                    SecurityContextHolder.getContext().getAuthentication().getName());
            entity.setChecker(checker);

            if (dto.getIsChecked()) {
                entity.getComment().setIsChecked(true);
                commentRepository.save(entity.getComment());
            }
        }
        commentRequestRepository.save(entity);
        return commentRequestConverter.fromEntity(entity);
    }

    @Transactional
    public boolean delete(Long id) {
        CommentRequestEntity entity = commentRequestRepository.findById(id)
                .orElse(null);
        if (entity == null || entity.getIsDeleted())
            return false;
        entity.setIsDeleted(true);
        commentRequestRepository.save(entity);
        return true;
    }
}
