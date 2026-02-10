package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.usecase.DisableUserUsecase;
import com.hyuse98.scheduler.iam.application.usecase.EnableUserUsecase;
import com.hyuse98.scheduler.iam.application.usecase.GetUserProfileUsecase;
import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import com.hyuse98.scheduler.iam.domain.model.aggregate.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/admin/client")
public class AdminUserController {

    private final GetUsersUsecase getUsersUsecase;
    private final GetUserProfileUsecase getUserProfileUsecase;
    private final DisableUserUsecase  disableUserUsecase;
    private final EnableUserUsecase enableUserUsecase;

    public AdminUserController(GetUsersUsecase getUsersUsecase, GetUserProfileUsecase getUserProfileUsecase, DisableUserUsecase disableUserUsecase, EnableUserUsecase enableUserUsecase) {
        this.getUsersUsecase = getUsersUsecase;
        this.getUserProfileUsecase = getUserProfileUsecase;
        this.disableUserUsecase = disableUserUsecase;
        this.enableUserUsecase = enableUserUsecase;
    }

    //TODO(List Users)
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(getUsersUsecase.execute());
    }

    //TODO(Get User {Email})
    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam String email) {
        return ResponseEntity.ok().body(getUserProfileUsecase.execute());
    }

    //TODO(Get User {ID})
    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam UUID id) {
        return ResponseEntity.ok().body(getUserProfileUsecase.execute());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void disableUser(@RequestParam UUID id) {
        disableUserUsecase.execute(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void enableUser(@RequestParam UUID id) {
        enableUserUsecase.execute(id);
    }
}
