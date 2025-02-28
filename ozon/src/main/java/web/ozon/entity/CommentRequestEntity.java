package web.ozon.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
