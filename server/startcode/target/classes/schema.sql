DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE,
    name       VARCHAR(255),
    age        INT,
    password   VARCHAR(255)
);