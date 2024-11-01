package clinical.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String fullName;
    private String email;
    private String gender;
    private String phoneNumber;
    private String username;
    private String password;
}
