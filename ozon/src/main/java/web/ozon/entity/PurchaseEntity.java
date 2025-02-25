package web.ozon.entity;

import org.hibernate.annotations.ColumnDefault;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchases")
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isCommented;
}
