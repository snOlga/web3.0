package lib.entity.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lib.entity.dto.DTO.CommentReportDTO;
import lib.entity.dto.entity.CommentReportEntity;

@Service
public class CommentReportConverter {

    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private UserConverter userConverter;
    
    public CommentReportEntity fromDTO(CommentReportDTO dto) {
        if (dto == null)
            return null;

        return new CommentReportEntity(
                dto.getId(),
                commentConverter.fromId(dto.getCommentId()),
                userConverter.fromId(dto.getReporterId()),
                dto.getReason(),
                dto.getMessage(),
                dto.getIsAccepted(),
                userConverter.fromDTO(dto.getChecker()),
                dto.getIsDeleted() == null ? false : dto.getIsDeleted());
    }

    public CommentReportDTO fromEntity(CommentReportEntity entity) {
        if (entity == null)
            return null;

        return new CommentReportDTO(
                entity.getId(),
                entity.getComment().getId(),
                entity.getReporter().getId(),
                entity.getReason(),
                entity.getMessage(),
                entity.getIsAccepted(),
                userConverter.fromEntity(entity.getChecker()),
                entity.getIsDeleted());
    }
}
