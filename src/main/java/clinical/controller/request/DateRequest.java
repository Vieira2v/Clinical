package clinical.controller.request;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class DateRequest {
    private LocalDateTime start;
    private LocalDateTime end;
}
