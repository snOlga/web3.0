package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Null;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CommentRequestDTO {
    
    @Null
    private Long id;

    @Nonnull
    private CommentDTO comment;

    @Nonnull
    private Boolean isDeleted;
}
