USE `ecommerce`;

INSERT INTO `member`(`name`, `email`, `password`, `role`) 
VALUES
("Manders", "manders@test.com", "$2a$12$d6aqfLhwZIHy662HLS0TPev.rplM.OXzCp8FSkOWxio20UwqecLN.", "ROLE_ADMIN"),
("Hua", "hua@test.com", "$2a$10$wlpxyV.Xsdq/2qW95s0s5efRT2oR3JslGDnT8JaR6HslTidOc3xca", "ROLE_USER");