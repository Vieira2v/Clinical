package clinical.domain.service;

import clinical.controller.config.SecurityConfig;
import clinical.controller.mapper.DozerMapper;
import clinical.controller.response.UserResponse;
import clinical.domain.User;
import clinical.domain.service.security.JwtTokenProvider;
import clinical.domain.service.security.dto.CredentialsLogin;
import clinical.domain.service.security.dto.Token;
import clinical.resource.repositories.PermissionEntityRepository;
import clinical.resource.repositories.UserEntityRepository;
import clinical.resource.repositories.model.Permissions;
import clinical.resource.repositories.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private UserEntityRepository repository;

    @Autowired
    private PermissionEntityRepository permissionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SecurityConfig securityConfig;

    public UserResponse createUser(User user) {
        if (user == null) throw  new IllegalArgumentException();

        securityConfig.passwordEncoder().encode(user.getPassword());
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));

        var entity = DozerMapper.parseObject(user, UserEntity.class);

        Permissions permission = permissionRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Permissions not found"));
        List<Permissions> permissions = new ArrayList<>();
        permissions.add(permission);
        entity.setPermissions(permissions);

        return DozerMapper.parseObject(repository.save(entity), UserResponse.class);
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity loginUser(CredentialsLogin login) {
        try {
            var username = login.getUsername();
            var password = login.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            var user = repository.findByUsername(username);

            var tokenResponse = new Token();
            if (user != null) {
                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username or password is incorrect");
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
