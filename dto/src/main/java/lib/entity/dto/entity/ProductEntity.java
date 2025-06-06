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
@Table(name = "products")
@Where(clause = "is_deleted = false")
public class ProductEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @Nonnull
    @Column(name = "content")
    private String content;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
