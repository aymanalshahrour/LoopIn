DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE,
    username  VARCHAR(255) UNIQUE ,
    password   VARCHAR(255)
);