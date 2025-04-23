package lib.entity.dto.DTO;

import lombok.*;
import lib.entity.dto.business.ReportReason;

@Setter
@Getter
@AllArgsConstructor
public class CommentReportDTO {

    private Long id;

    private Long commentId;

    private Long reporterId;

    private ReportReason reason;

    private String message;

    private Boolean isAccepted;

    private UserDTO checker;

    private Boolean isDeleted;
}
