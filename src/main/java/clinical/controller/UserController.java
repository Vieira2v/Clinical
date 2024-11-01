package clinical.controller;

import clinical.controller.mapper.DozerMapper;
import clinical.controller.request.UserRequest;
import clinical.domain.User;
import clinical.domain.service.AuthenticationService;
import clinical.domain.service.ScheduleService;
import clinical.domain.service.security.JwtTokenProvider;
import clinical.domain.service.security.dto.CredentialsLogin;
import clinical.resource.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/api/clinical")
public class UserController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private ScheduleService consultationsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/register")
    public ResponseEntity registerUser(@RequestBody UserRequest userRequest) {
        var request = DozerMapper.parseObject(userRequest, User.class);
        var create = authService.createUser(request);
        return ResponseEntity.ok(create);
    }

    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody CredentialsLogin credentialsLogin) {
        return ResponseEntity.ok(authService.loginUser(credentialsLogin));
    }
}
