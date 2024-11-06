package clinical.controller.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompletedHistoryResponse {
    private String doctorName;
    private String patientName;
    private LocalDateTime dateTime;
    private String reason;
    private String commentFinal;
}
