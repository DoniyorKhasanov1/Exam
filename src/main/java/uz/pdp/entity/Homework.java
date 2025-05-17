package uz.pdp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Homework {
    private UUID id;
    private Long chatId;
    private String description;
    private String zipFileId;
    private Integer grade;
    private String teacherFeedback;
    private LocalDateTime sendTime;
    private LocalDateTime checkedAt;
}
