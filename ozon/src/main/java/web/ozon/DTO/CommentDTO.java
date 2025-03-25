package web.ozon.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    
    private Long productId;
    
    private Long authorId;
    
    private String content;
    
    private Boolean isAnonymous;
    
    private Boolean isDeleted;

    private Boolean isChecked;

    private Boolean isReported;
}
