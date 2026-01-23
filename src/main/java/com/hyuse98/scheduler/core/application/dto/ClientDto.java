package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record ClientDto(UUID id,
                        String email,
                        String name,
                        String phoneNumber,
                        Date birthday,
                        String address,
                        String cns,
                        Date createdAt,
                        Boolean isActive) implements Serializable {
}