package web.ozon.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private UserDTO owner;

    private String content;
}
