package web.ozon.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentRequestDTO;
import web.ozon.entity.CommentRequestEntity;

@Service
public class CommentRequestConverter {

    @Autowired
    private CommentConverter commentConverter;

    public CommentRequestEntity fromDTO(CommentRequestDTO dto) {
        if (dto == null)
            return null;

        return new CommentRequestEntity(dto.getId(), commentConverter.fromDTO(dto.getComment()), dto.getIsDeleted());
    }

    public CommentRequestDTO fromEntity(CommentRequestEntity entity) {
        if (entity == null)
            return null;

        return new CommentRequestDTO(entity.getId(), commentConverter.fromEntity(entity.getComment()),
                entity.getIsDeleted());
    }
}
