package clinical.controller.request;

import lombok.*;


@Data
public class UserRequest {
    private String fullName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String username;
    private String password;
}
