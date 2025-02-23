package web.ozon.DTO;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;

import org.hibernate.annotations.Where;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "comments")
@Where(clause = "is_deleted = false")
public class CommentDTO {

    @Null
    private Long id;

    @Nonnull
    private UserDTO product;

    @Nonnull
    private UserDTO author;

    @Nonnull
    private String content;

    @Nonnull
    private Boolean isAnonymous;

    @Nonnull
    private Boolean isDeleted;
}
