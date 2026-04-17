package com.hyuse98.scheduler.iam.infrastructure.api;

import com.hyuse98.scheduler.iam.application.dto.UserProfileResponse;
import com.hyuse98.scheduler.iam.application.dto.UserStatusRequest;
import com.hyuse98.scheduler.iam.application.usecase.GetUsersUsecase;
import com.hyuse98.scheduler.iam.application.usecase.impl.UpdateUserStatusUseCaseImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "IAM - Admin", description = "Administrative endpoints for user management")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/iam/admin/user")
public class AdminUserController {

    private final GetUsersUsecase getUsersUsecase;
    private final UpdateUserStatusUseCaseImpl updateUserStatusUseCaseImpl;

    public AdminUserController(GetUsersUsecase getUsersUsecase, UpdateUserStatusUseCaseImpl updateUserStatusUseCaseImpl) {
        this.getUsersUsecase = getUsersUsecase;
        this.updateUserStatusUseCaseImpl = updateUserStatusUseCaseImpl;
    }

    @Operation(summary = "List all users", description = "Returns a list containing the profiles of all system users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users Listed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No users found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getUsers() {
        List<UserProfileResponse> response = getUsersUsecase.execute().stream()
                .map(user -> new UserProfileResponse(
                        user.getId(),
                        user.getEmail().getValue(),
                        user.getRoles().iterator().next().name()
                ))
                .toList();

        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user status", description = "Activates or deactivates a user's access by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User status updated successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "State conflict (if using strict state validation)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserStatus(
            @Parameter(description = "UUID of the user to be updated", required = true)
            @PathVariable("id") UUID id, @RequestBody UserStatusRequest request) {
        updateUserStatusUseCaseImpl.execute(id, request.active());
    }
}