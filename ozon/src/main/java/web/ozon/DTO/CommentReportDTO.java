package web.ozon.DTO;

import lombok.*;
import web.ozon.business.ReportReason;

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
