DROP TABLE IF EXISTS Messages;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) UNIQUE,
    username   VARCHAR(255) UNIQUE,
    password   VARCHAR(255)
);

CREATE TABLE Messages
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_username   VARCHAR(255),
    receiver_username VARCHAR(255),
    content           VARCHAR(255),
    timestamp         DATETIME
);