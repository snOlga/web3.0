package lib.entity.dto.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import lib.entity.dto.business.ReportReason;

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
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private UserEntity reporter;

    @Nonnull
    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private ReportReason reason;

    @Column(name = "message")
    private String message;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @ManyToOne
    @JoinColumn(name = "checker_id")
    private UserEntity checker;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
