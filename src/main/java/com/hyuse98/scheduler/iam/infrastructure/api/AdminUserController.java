package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.usecase.DisableUserUsecase;
import com.hyuse98.scheduler.iam.application.usecase.EnableUserUsecase;
import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/admin/client")
public class AdminUserController {

    private final GetUsersUsecase getUsersUsecase;
    private final DisableUserUsecase disableUserUsecase;
    private final EnableUserUsecase enableUserUsecase;

    public AdminUserController(GetUsersUsecase getUsersUsecase, DisableUserUsecase disableUserUsecase, EnableUserUsecase enableUserUsecase) {
        this.getUsersUsecase = getUsersUsecase;
        this.disableUserUsecase = disableUserUsecase;
        this.enableUserUsecase = enableUserUsecase;
    }

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getUsers() {
        List<UserProfileResponse> response = getUsersUsecase.execute().stream()
                .map(user -> new UserProfileResponse(
                        user.getId(),
                        user.getEmail().getValue(),
                        user.getRoles().iterator().next().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/disable")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void disableUser(@RequestParam UUID id) {
        disableUserUsecase.execute(id);
    }

    @PostMapping("/enable")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void enableUser(@RequestParam UUID id) {
        enableUserUsecase.execute(id);
    }
}