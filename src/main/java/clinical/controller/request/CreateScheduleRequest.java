package clinical.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateScheduleRequest {
    private LocalDateTime dateTime;
    private String doctor;
}
