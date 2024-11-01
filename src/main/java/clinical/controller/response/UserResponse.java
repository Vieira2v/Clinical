package clinical.controller.response;

import lombok.Data;

@Data
public class UserResponse {
    private String fullName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String username;
}
