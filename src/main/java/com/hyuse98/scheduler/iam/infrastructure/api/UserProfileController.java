package com.hyuse98.scheduler.iam.infrastructure.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "User Profile", description = "Endpoints para o perfil do usuário")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/v1/auth/client/me")
public class UserProfileController {

    @Operation(summary = "Get Profile", description = "Returns the authenticated user's profile data.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the submitted payload",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<String> getProfile(Principal principal) {
        return ResponseEntity.ok("Autenticado como: " + principal.getName());
    }

    @Operation(summary = "Change Password", description = "Changes the logged-in user's password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Password changed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in the submitted payload",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody String newPassword) {
        return ResponseEntity.ok("Solicitação de troca de senha recebida para: " + principal.getName());
    }
}