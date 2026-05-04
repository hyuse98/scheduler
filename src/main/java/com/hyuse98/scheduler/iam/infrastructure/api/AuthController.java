package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.*;
import com.hyuse98.scheduler.iam.application.usecase.LoginUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterServiceProviderUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterUseCase;
import com.hyuse98.scheduler.iam.infrastructure.api.advice.ErrorResponse;
import com.hyuse98.scheduler.iam.infrastructure.config.RefreshTokenService;
import com.hyuse98.scheduler.iam.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Authentication", description = "Endpoints for Login and Registration")
@SecurityRequirements()
@RestController
@RequestMapping("/api/v1/iam/auth")
public class AuthController {

    private final RegisterUseCase registerUseCase;
    private final RegisterServiceProviderUseCase registerServiceProviderUseCase;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public AuthController(
            RegisterUseCase registerUseCase,
            RegisterServiceProviderUseCase registerServiceProviderUseCase,
            LoginUseCase loginUseCase, RefreshTokenService refreshTokenService, TokenService tokenService) {
        this.registerUseCase = registerUseCase;
        this.registerServiceProviderUseCase = registerServiceProviderUseCase;
        this.loginUseCase = loginUseCase;
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshTokenService::getUserFromToken)
                .map(domainUser -> {

                    if (!domainUser.isEnabled()) {
                        throw new RuntimeException("Usuário desativado!");
                    }

                    String token = tokenService.generateToken(domainUser);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));

                })
                .orElseThrow(() -> new RuntimeException("Refresh token não encontrado!"));
    }

    @Operation(summary = "Log in", description = "Authenticates the user using email and password, returning a JWT token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials - incorrect email or password",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the submitted payload",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Register new User", description = "Creates a new user record on the platform and dispatches the registration event")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already in use",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the submitted payload",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register/user")
    public ResponseEntity<UserProfileResponse> register(@Valid @RequestBody RegistrationRequest request) {
        UserProfileResponse response = registerUseCase.execute(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Register new service provider", description = "Creates a new provider record on the platform and dispatches the registration event")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Service provider created",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already in use",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the submitted payload",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register/provider")
    public ResponseEntity<UserProfileResponse> registerProvider(@Valid @RequestBody RegistrationRequest request) {
        UserProfileResponse response = registerServiceProviderUseCase.execute(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
