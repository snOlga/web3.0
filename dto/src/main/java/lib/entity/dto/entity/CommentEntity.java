package lib.entity.dto.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
@Where(clause = "is_deleted = false")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @Nonnull
    @Column(name = "content")
    private String content;

    @Nonnull
    @Column(name = "is_anonymous")
    @ColumnDefault("false")
    private Boolean isAnonymous;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;

    @Nonnull
    @Column(name = "is_checked")
    private Boolean isChecked;
    
    @Column(name = "is_reported")
    @ColumnDefault("false")
    private Boolean isReported;
}
