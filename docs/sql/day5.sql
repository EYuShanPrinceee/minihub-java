CREATE DATABASE IF NOT EXISTS minihub
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE minihub;

DROP TABLE IF EXISTS articles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE articles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    author_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_articles_author
        FOREIGN KEY (author_id)
        REFERENCES users(id)
);

INSERT INTO users (nickname, email)
VALUES
    ('Tom', 'tom@example.com'),
    ('Jack', 'jack@example.com');

INSERT INTO articles (title, content, author_id)
VALUES
    ('Java Day 5', 'Today I learned MySQL and SQL.', 1),
    ('Spring Boot Controller', 'Today I practiced Controller and DTO.', 1),
    ('Backend Study Note', 'This article belongs to Jack.', 2);

SELECT * FROM users;

SELECT * FROM articles;

SELECT *
FROM users
WHERE email = 'tom@example.com';

SELECT *
FROM articles
WHERE author_id = 1;

SELECT *
FROM articles
WHERE title LIKE '%Java%'
   OR content LIKE '%Java%';

SELECT *
FROM articles
WHERE author_id = 1
  AND (title LIKE '%Java%' OR content LIKE '%Java%');

SELECT *
FROM articles
ORDER BY created_at DESC
LIMIT 0, 10;

SELECT COUNT(*)
FROM articles;

SELECT
    a.id,
    a.title,
    a.content,
    a.author_id,
    u.nickname AS author_nickname
FROM articles a
JOIN users u ON a.author_id = u.id;