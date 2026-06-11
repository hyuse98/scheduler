CREATE TABLE IF NOT EXISTS core.client
(
    id           UUID NOT NULL,
    email        VARCHAR(255),
    name         VARCHAR(255),
    phone_number VARCHAR(255),
    birthday     TIMESTAMP(6) WITHOUT TIME ZONE,
    address      VARCHAR(255),
    cns          VARCHAR(255),
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE,
    is_active    BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS core.service_provider
(
    id           UUID NOT NULL,
    name         VARCHAR(255),
    email        VARCHAR(255),
    phone_number VARCHAR(255),
    birthday     TIMESTAMP(6) WITHOUT TIME ZONE,
    address      VARCHAR(255),
    expertise    VARCHAR(255),
    registry     VARCHAR(255),
    created_at   TIMESTAMP(6) WITHOUT TIME ZONE,
    is_active    BOOLEAN,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS core.schedule
(
    id                  UUID NOT NULL,
    client_id           UUID,
    service_provider_id UUID,
    service_type        VARCHAR(255),
    description         VARCHAR(255),
    scheduled_at        TIMESTAMP(6) WITHOUT TIME ZONE,
    status              VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_publication
(
    id                     UUID                        NOT NULL,
    event_type             VARCHAR(255)                NOT NULL,
    listener_id            VARCHAR(255)                NOT NULL,
    serialized_event       VARCHAR(255)                NOT NULL,
    publication_date       TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    completion_date        TIMESTAMP(6) WITH TIME ZONE,
    last_resubmission_date TIMESTAMP(6) WITH TIME ZONE,
    completion_attempts    INTEGER                     NOT NULL,
    status                 VARCHAR(255) CHECK ( status IN
                                                ('PUBLISHED', 'PROCESSING', 'COMPLETED', 'FAILED', 'RESUBMITTED')),
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS core.schedule
    ADD CONSTRAINT FK_schedule_client FOREIGN KEY (client_id) REFERENCES core.client (id);
ALTER TABLE IF EXISTS core.schedule
    ADD CONSTRAINT FK_schedule_provider FOREIGN KEY (service_provider_id) REFERENCES core.service_provider (id);