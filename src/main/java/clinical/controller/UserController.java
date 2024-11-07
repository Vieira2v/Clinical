package clinical.controller;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.UserRequest;
import clinical.domain.UserDomain;
import clinical.domain.service.AuthenticationService;
import clinical.domain.service.security.dto.CredentialsLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/api/clinical")
public class UserController {

    @Autowired
    private AuthenticationService authService;

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/register")
    public ResponseEntity registerUser(@RequestBody UserRequest userRequest) {
        var request = DozerMapper.parseObject(userRequest, UserDomain.class);
        var create = authService.createUser(request);
        return ResponseEntity.ok(create);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody CredentialsLogin credentialsLogin) {
        return ResponseEntity.ok(authService.loginUser(credentialsLogin));
    }
}
