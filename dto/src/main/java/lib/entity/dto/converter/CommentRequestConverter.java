package lib.entity.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.CommentRequestDTO;
import lib.entity.dto.entity.CommentRequestEntity;

@Service
public class CommentRequestConverter {

    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private UserConverter userConverter;

    public CommentRequestEntity fromDTO(CommentRequestDTO dto) {
        if (dto == null)
            return null;

        return new CommentRequestEntity(
                dto.getId(),
                commentConverter.fromDTO(dto.getComment()),
                dto.getIsChecked(),
                userConverter.fromDTO(dto.getChecker()),
                dto.getIsDeleted());
    }

    public CommentRequestDTO fromEntity(CommentRequestEntity entity) {
        if (entity == null)
            return null;

        return new CommentRequestDTO(
                entity.getId(),
                commentConverter.fromEntity(entity.getComment()),
                entity.getIsChecked(),
                userConverter.fromEntity(entity.getChecker()),
                entity.getIsDeleted());
    }
}
