package com.hyuse98.scheduler.core;

import java.util.Date;
import java.util.UUID;


public record ClientRegisteredEvent(
        UUID clientId,
        String clientEmail,
        String clientName,
        String clientPhoneNumber,
        Date clientBirthday,
        String clientAddress,
        String clientCns,
        Date clientCreatedAt,
        Boolean clientIsActive
) {
}
