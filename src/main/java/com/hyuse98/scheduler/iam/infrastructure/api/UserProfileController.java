package com.hyuse98.scheduler.iam.infrastructure.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/auth/client/me")
public class UserProfileController {

    @GetMapping
    public ResponseEntity<String> getProfile(Principal principal) {
        return ResponseEntity.ok("Autenticado como: " + principal.getName());
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody String newPassword) {
        return ResponseEntity.ok("Solicitação de troca de senha recebida para: " + principal.getName());
    }
}