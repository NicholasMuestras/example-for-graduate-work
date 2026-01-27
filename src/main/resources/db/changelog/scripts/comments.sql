-- liquibase formatted sql

-- changeset nobraztsov:1
CREATE TABLE IF NOT EXISTS comments (
    pk SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at BIGINT NOT NULL,
    author_id INTEGER NOT NULL,
    ad_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads(pk) ON DELETE CASCADE
);
