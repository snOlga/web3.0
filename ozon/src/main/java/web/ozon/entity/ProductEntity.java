package web.ozon.entity;

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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Nonnull
    @Column(name = "content")
    private String content;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
