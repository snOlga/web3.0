package web.ozon.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentDTO;
import web.ozon.entity.CommentEntity;

@Service
public class CommentConverter {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private ProductConverter productConverter;

    public CommentEntity fromDTO(CommentDTO dto) {
        return new CommentEntity(
            null, 
            productConverter.fromDTO(dto.getProduct()), 
            userConverter.fromDTO(dto.getAuthor()), 
            dto.getContent(), 
            dto.getIsAnonymous(), 
            null
        );
    }

    public CommentDTO fromEntity(CommentEntity entity) {
        return new CommentDTO(
            entity.getId(),
            productConverter.fromEntity(entity.getProduct()), 
            userConverter.fromEntity(entity.getAuthor()), 
            entity.getContent(), 
            entity.getIsAnonymous(), 
            entity.getIsDeleted());
    }
}
