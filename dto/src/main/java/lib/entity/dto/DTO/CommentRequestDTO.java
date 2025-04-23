package lib.entity.dto.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CommentRequestDTO {
    
    private Long id;

    private CommentDTO comment;

    private Boolean isChecked;

    private UserDTO checker;

    private Boolean isDeleted;
}
