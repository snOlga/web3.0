package lib.entity.dto.DTO;

import lombok.Data;

@Data
public class BitrixTaskRequest {
    private String title;
    private String description;
    private Integer responsibleId;
    private Long commentId; // Добавим связь с комментарием
}