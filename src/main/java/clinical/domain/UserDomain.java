package clinical.domain;

import lombok.Data;

@Data
public class UserDomain {
    private int id;
    private String fullName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String username;
    private String password;
}
