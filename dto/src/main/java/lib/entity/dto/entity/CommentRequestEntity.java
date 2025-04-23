package lib.entity.dto.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_requests")
@Where(clause = "is_deleted = false")
public class CommentRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Null
    @Column(name = "is_checked")
    private Boolean isChecked;

    @Null
    @ManyToOne
    @JoinColumn(name = "checker_id")
    private UserEntity checker;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
