CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255),
    `nickname` VARCHAR(255) NOT NULL UNIQUE,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `role` VARCHAR(50) NOT NULL,
    `created_date` DATETIME NOT NULL,
    `modified_date` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `post` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(500) NOT NULL,
    `content` TEXT NOT NULL,
    `writer` VARCHAR(255) NOT NULL,
    `view` INT NOT NULL DEFAULT 0,
    `file_url` VARCHAR(255),
    `user_id` BIGINT,
    `created_date` DATETIME NOT NULL,
    `modified_date` DATETIME NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_posts_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE Comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment TEXT NOT NULL,
    created_date VARCHAR(255) NOT NULL,
    modified_date VARCHAR(255) NOT NULL,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
