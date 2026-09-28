package re.edu.md3ss15.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.md3ss15.dto.AuthResponse;
import re.edu.md3ss15.dto.LoginRequest;
import re.edu.md3ss15.dto.RegisterRequest;
import re.edu.md3ss15.entity.User;
import re.edu.md3ss15.repository.UserRepository;
import re.edu.md3ss15.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long ACCESS_EXPIRATION = 15 * 60 * 1000;
    private static final long REFRESH_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = User.builder()
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_CUSTOMER")
                .build();
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String email = authentication.getName();
        String accessToken = jwtTokenProvider.generateToken(email, ACCESS_EXPIRATION);
        String refreshToken = jwtTokenProvider.generateToken(email, REFRESH_EXPIRATION);

        return new AuthResponse(accessToken, refreshToken, "Bearer");
    }
}
