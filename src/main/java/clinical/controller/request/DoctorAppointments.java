package clinical.controller.request;

import clinical.resource.repositories.model.UserEntity;
import lombok.Data;

@Data
public class DoctorAppointments {
    private String commentFinal;
    private PatientId patientId;
}
