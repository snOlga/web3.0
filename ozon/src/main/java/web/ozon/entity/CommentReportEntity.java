package web.ozon.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import web.ozon.business.ReportReason;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment_reports")
@Where(clause = "is_deleted = false")
public class CommentReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private ReportReason reason;

    @Null
    @Column(name = "message")
    private String message;

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
