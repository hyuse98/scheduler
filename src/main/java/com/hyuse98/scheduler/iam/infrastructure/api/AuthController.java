package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.application.usecase.LoginUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterServiceProviderUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints para registro e login de usuários")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final RegisterServiceProviderUseCase registerServiceProviderUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(
            RegisterUseCase registerUseCase,
            RegisterServiceProviderUseCase registerServiceProviderUseCase,
            LoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.registerServiceProviderUseCase = registerServiceProviderUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Operation(summary = "Registrar novo prestador de serviços", description = "Cria um novo registro de prestador na plataforma com role SERVICE_PROVIDER e despacha o evento de registro.")
    @PostMapping("/register/provider")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerProvider(@RequestBody RegistrationRequest request) {
        registerServiceProviderUseCase.execute(request);
    }

    @Operation(summary = "Registrar novo usuário", description = "Cria um novo registro de usuário na plataforma e despacha o evento de registro.")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegistrationRequest request) {
        registerUseCase.execute(request);
    }

    @Operation(summary = "Fazer login", description = "Autentica o usuário utilizando e-mail e senha, retornando um token JWT.")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
