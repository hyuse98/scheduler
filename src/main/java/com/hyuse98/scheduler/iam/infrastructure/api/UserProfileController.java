package com.hyuse98.scheduler.iam.infrastructure.api;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Obter perfil", description = "Retorna os dados do perfil do usuário autenticado")
    @GetMapping
    public ResponseEntity<String> getProfile(Principal principal) {
        return ResponseEntity.ok("Autenticado como: " + principal.getName());
    }

    @Operation(summary = "Mudar senha", description = "Altera a senha do usuário logado")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody String newPassword) {
        return ResponseEntity.ok("Solicitação de troca de senha recebida para: " + principal.getName());
    }
}