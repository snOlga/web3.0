package web.ozon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import web.ozon.DTO.CommentDTO;
import web.ozon.converter.CommentConverter;
import web.ozon.entity.CommentEntity;
import web.ozon.exception.*;
import web.ozon.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private CommentRequestService commentRequestService;

    private int PAGINATION_STEP = 10;

    public List<CommentDTO> getAllByProductId(Long productId, int from) {
        return commentRepository.findAllByProductId(productId, PageRequest.of(from, PAGINATION_STEP)).stream()
                .filter(comment -> comment.getIsChecked()).map(commentConverter::fromEntity).toList();
    }

    @Transactional
    public CommentDTO save(CommentDTO commentDTO) throws CommentNotNewException, NonNullNewIdException {
        isCommentNew(commentDTO);
        CommentEntity commentEntity = commentConverter.fromDTO(commentDTO);
        commentRepository.save(commentEntity);
        CommentDTO result = commentConverter.fromEntity(commentEntity);
        commentRequestService.createRequest(result);
        return result;
    }

    private void isCommentNew(CommentDTO commentDTO) throws CommentNotNewException, NonNullNewIdException {
        if (commentDTO.getId() != null)
            throw new NonNullNewIdException();
        CommentEntity existedComment = commentRepository.findByProductIdAndAuthorId(commentDTO.getProductId(),
                commentDTO.getAuthorId());
        if (existedComment != null)
            throw new CommentNotNewException();
    }

    @Transactional
    public CommentDTO update(CommentDTO commentDTO) throws CommentNotExistException, NotSameAuthorException {
        isAbleToUpdateOrDelete(commentDTO.getId());
        CommentEntity existing = commentRepository.findById(commentDTO.getId()).get();
        if (!existing.getContent().equals(commentDTO.getContent())) {
            existing.setContent(commentDTO.getContent());
            existing.setIsChecked(false);
            commentRepository.save(existing);
            commentRequestService.createRequest(commentDTO);
        }
        return commentConverter.fromEntity(existing);
    }

    @Transactional
    public boolean delete(Long id) throws CommentNotExistException, NotSameAuthorException {
        isAbleToUpdateOrDelete(id);
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        comment.setIsDeleted(true);
        commentRepository.save(comment);
        return true;
    }

    private void isAbleToUpdateOrDelete(Long id) throws CommentNotExistException, NotSameAuthorException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            throw new CommentNotExistException();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ADMIN"));
        boolean isOwner = comment.getAuthor().getLogin().equals(auth.getName());

        if (!isOwner && !isAdmin)
            throw new NotSameAuthorException();
    }
}
