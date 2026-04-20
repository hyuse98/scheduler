package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.JwtResponse;
import com.hyuse98.scheduler.iam.application.dto.LoginRequest;
import com.hyuse98.scheduler.iam.application.dto.RegistrationRequest;
import com.hyuse98.scheduler.iam.application.usecase.LoginUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterServiceProviderUseCase;
import com.hyuse98.scheduler.iam.application.usecase.RegisterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hyuse98.scheduler.iam.infrastructure.api.advice.ErrorResponse;

@Tag(name = "Authentication", description = "Endpoints for Login and Registration")
@SecurityRequirements()
@RestController
@RequestMapping("/api/v1/iam/auth")
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
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
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
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegistrationRequest request) {
        registerUseCase.execute(request);
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
    @ResponseStatus(HttpStatus.CREATED)
    public void registerProvider(@RequestBody RegistrationRequest request) {
        registerServiceProviderUseCase.execute(request);
    }
}
