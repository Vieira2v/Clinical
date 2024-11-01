package clinical.domain.service.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private boolean authenticated;
    private Date created;
    private Date expires;
    private String accessToken;
    private String refreshToken;
}