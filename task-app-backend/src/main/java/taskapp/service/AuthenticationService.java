package taskapp.service;

import taskapp.dto.AuthenticationRequest;
import taskapp.dto.AuthenticationResponse;
import taskapp.dto.RegisterRequest;
import taskapp.entity.User;
import taskapp.exception.AuthenticationException;
import taskapp.repository.UserRepository;
import taskapp.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // Verifica username - antes de tentar salvar
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("USERNAME_EXISTS");
        }
        
        // Verifica email - antes de tentar salvar
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("EMAIL_EXISTS");
        }
        
        try {
            var user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (Exception e) {
            // Log da exceção para diagnóstico
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            e.printStackTrace();
            throw new AuthenticationException("REGISTRATION_FAILED");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user;
        String credential = request.getUsername(); // pode ser email ou username

        // Tenta encontrar por username primeiro
        Optional<User> userByUsername = userRepository.findByUsername(credential);
        if (userByUsername.isPresent()) {
            user = userByUsername.get();
        } else {
            // Se não encontrou por username, tenta por email
            user = userRepository.findByEmail(credential)
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        }

        // Autentica com as credenciais
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(), // sempre usa username para autenticação
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}