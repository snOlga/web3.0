package web.ozon.DTO;

import jakarta.annotation.Nonnull;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ProductDTO {

    @Nonnull
    private Long id;

    @Nonnull
    private UserDTO owner;

    @Nonnull
    private String content;
}
