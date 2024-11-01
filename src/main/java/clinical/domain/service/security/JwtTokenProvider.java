package clinical.domain.service.security;

import clinical.controller.exceptions.InvalidJwtAuthenticationException;
import clinical.domain.service.security.dto.Token;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWTVerifier;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length}")
    private final long validityInMilliSeconds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        algorithm = Algorithm.HMAC256(secretKey);
    }

    private String generateToken(String username, List<String> roles, Date now, Date expiration) {
        String issueUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .withSubject(username)
                .withIssuer(issueUrl)
                .sign(algorithm)
                .strip();
    }

    private String refreshToken(String username, List<String> roles, Date now) {
        Date validityRefreshToken = new Date(now.getTime() + (validityInMilliSeconds) * 3);
        return generateToken(username, roles, now, validityRefreshToken);
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedToken = decodedToken(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedToken.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        DecodedJWT decodedToken = decodedToken(token);

        try {
            return !decodedToken.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }

    public Token createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliSeconds);

        var accessToken = generateToken(username, roles, now, expiration);
        var refreshToken = refreshToken(username, roles, now);

        return new Token(username, true, now, expiration, accessToken, refreshToken);
    }

    public Token createRefreshToken(String refreshToken) {
        if (refreshToken.contains("Bearer ")) refreshToken = refreshToken.substring(7);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedToken = verifier.verify(refreshToken);

        String username = decodedToken.getSubject();
        List<String> roles = decodedToken.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }
}
