package clinical.controller.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CancelledHistoryResponse {

    private String doctor;
    private String patientName;
    private LocalDateTime dateTime;
    private String commentFinal;
}
