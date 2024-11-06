package clinical.controller.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchedulesResponse {

    private LocalDateTime dateTime;
    private String situation;

}
