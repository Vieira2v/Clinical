package clinical.domain;

import clinical.resource.repositories.model.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompletedConsultationsDomain {

    private Long id;
    private UserEntity doctor;
    private UserEntity patientId;
    private LocalDateTime dateTime;
    private String reason;
    private String commentFinal;

}
