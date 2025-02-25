sudo mariadb -u root


CREATE USER 'm1Informatique'@'%' IDENTIFIED BY 'm1Informatique';


GRANT ALL PRIVILEGES ON *.* TO 'm1Informatique'@'%' WITH GRANT OPTION;


FLUSH PRIVILEGES;


EXIT;


-- INSERT INTO utilisateur (email, password) VALUES 
-- ('a', 'a'),
-- ('gaye@gaye.com', '123'),
-- ('user2@example.com', 'password456');


-- INSERT INTO utilisateur (email, password) VALUES 
-- ('user1@example.com', SHA2('password123', 256)),
-- ('user2@example.com', SHA2('password456', 256));

