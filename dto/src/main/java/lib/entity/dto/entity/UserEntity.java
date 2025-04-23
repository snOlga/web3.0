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
@Table(name = "ozon_users")
@Where(clause = "is_deleted = false")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Nonnull
    @Column(name = "login", unique = true)
    private String login;

    @Nonnull
    @Column(name = "password")
    private String password;

    @Nonnull
    @Column(name = "role")
    @ColumnDefault("USER")
    private String role;

    @Nonnull
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private Boolean isDeleted;
}
