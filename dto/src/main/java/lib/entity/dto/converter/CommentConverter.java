package lib.entity.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.CommentDTO;
import lib.entity.dto.entity.CommentEntity;
import lib.entity.dto.repository.CommentRepository;

@Service
public class CommentConverter {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private ProductConverter productConverter;
    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity fromDTO(CommentDTO dto) {
        if (dto == null)
            return null;

        return new CommentEntity(
                dto.getId(),
                productConverter.fromId(dto.getProductId()),
                userConverter.fromId(dto.getAuthorId()),
                dto.getContent(),
                dto.getIsAnonymous(),
                dto.getIsDeleted() == null ? false : dto.getIsDeleted(),
                dto.getIsChecked() == null ? false : dto.getIsChecked(),
                dto.getIsReported() == null ? false : dto.getIsReported());
    }

    public CommentDTO fromEntity(CommentEntity entity) {
        if (entity == null || entity.getAuthor() == null || entity.getProduct() == null)
            return null;

        return new CommentDTO(
                entity.getId(),
                entity.getProduct().getId(),
                entity.getAuthor().getId(),
                entity.getContent(),
                entity.getIsAnonymous(),
                entity.getIsDeleted(),
                entity.getIsChecked(),
                entity.getIsReported());
    }

    public CommentEntity fromId(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
}
