package web.ozon.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.ozon.DTO.CommentReportDTO;
import web.ozon.entity.CommentReportEntity;

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
                commentConverter.fromDTO(dto.getComment()),
                dto.getReason(),
                dto.getMessage(),
                dto.getIsChecked(),
                userConverter.fromDTO(dto.getChecker()),
                dto.getIsDeleted());
    }

    public CommentReportDTO fromEntity(CommentReportEntity entity) {
        if (entity == null)
            return null;

        return new CommentReportDTO(
                entity.getId(),
                commentConverter.fromEntity(entity.getComment()),
                entity.getReason(),
                entity.getMessage(),
                entity.getIsChecked(),
                userConverter.fromEntity(entity.getChecker()),
                entity.getIsDeleted());
    }
}
