package lib.entity.dto.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private UserDTO owner;

    private String content;
}
