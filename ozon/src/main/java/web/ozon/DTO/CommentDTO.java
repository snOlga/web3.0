package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Null;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CommentDTO {

    @Null
    private Long id;

    @Nonnull
    private ProductDTO product;

    @Nonnull
    private UserDTO author;

    @Nonnull
    private String content;

    @Nonnull
    private Boolean isAnonymous;

    @Nonnull
    private Boolean isDeleted;
}
