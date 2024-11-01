package clinical.controller.request;

import lombok.*;

import java.time.LocalDate;

@Data
public class UserRequest {
    private String fullName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String username;
    private String password;
}
