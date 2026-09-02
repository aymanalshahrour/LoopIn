DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE,
    user_name  VARCHAR(255) UNIQUE ,
    password   VARCHAR(255)
);