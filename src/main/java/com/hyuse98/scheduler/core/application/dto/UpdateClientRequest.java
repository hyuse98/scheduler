package com.hyuse98.scheduler.core.application.dto;

import java.io.Serializable;
import java.util.Date;

public record UpdateClientRequest(
        String email,
        String name,
        String phoneNumber,
        Date birthday,
        String address,
        String cns) implements Serializable {
}
