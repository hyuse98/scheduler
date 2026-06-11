CREATE TABLE IF NOT EXISTS iam.roles
(
    id   UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS iam.users
(
    id       UUID PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS iam.user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES iam.users (id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES iam.roles (id)
);