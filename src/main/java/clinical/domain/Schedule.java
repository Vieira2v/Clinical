package clinical.domain;

import clinical.resource.repositories.model.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {

    private Long id;
    private UserEntity doctor;
    private LocalDateTime dateTime;
    private boolean isAvailable = true;
    private String reason;
    private String situation;
}
