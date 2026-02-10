package com.hyuse98.scheduler.iam.infrastructure.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/auth/client/me")
public class UserProfileController {

    //TODO (Get Profile)

    //TODO (Soft Delete)

    //TODO (Change Password)

    //TODO (Change Login Email)
}
