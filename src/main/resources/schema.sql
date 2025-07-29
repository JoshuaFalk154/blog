DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS likes;

CREATE TABLE users(
    id uuid NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    sub varchar(255) UNIQUE NOT NULL,
    created_at DATE,
    updated_at DATE,
    PRIMARY KEY(id)
);

CREATE TABLE posts(
    id uuid NOT NULL,
    title VARCHAR(255) NOT NULL,
    body VARCHAR(1024) NOT NULL,
    created_at DATE,
    updated_at DATE,
    author_id uuid,
    PRIMARY KEY(id)
);

CREATE TABLE likes(
    id uuid NOT NULL,
    created_at DATE,
    author_id uuid,
    post_id uuid,
    PRIMARY KEY(id)
);

ALTER TABLE posts
ADD CONSTRAINT fk_posts_author
FOREIGN KEY (author_id) REFERENCES users(id);

ALTER TABLE likes
ADD CONSTRAINT fk_likes_author
FOREIGN KEY (author_id) REFERENCES users(id);

ALTER TABLE likes
ADD CONSTRAINT fk_likes_post
FOREIGN KEY (post_id) REFERENCES posts(id);
