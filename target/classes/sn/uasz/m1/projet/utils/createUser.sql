sudo mariadb -u root


CREATE USER 'm1Informatique'@'%' IDENTIFIED BY 'm1Informatique';


GRANT ALL PRIVILEGES ON *.* TO 'm1Informatique'@'%' WITH GRANT OPTION;


FLUSH PRIVILEGES;


EXIT;

----------------------------------------------------------------------------------------------------------------------
INSERT INTO utilisateur (nom, prenom, dateNaissance, sexe, adresse, email, password, type_utilisateur)
VALUES 
('Ngor', 'Fall', '2000-05-15', 'MASCULIN', 'Dakar', 'e', 'e', 'ETUDIANT'),
('Gaye', 'Abdoulaye', '2000-05-15', 'MASCULIN', 'Dakar', 'abdoulaye.gaye@example.com', 'password123', 'ETUDIANT'),
('Diop', 'Aminata', '1998-08-22', 'FEMININ', 'Thiès', 'aminata.diop@example.com', 'password456', 'ETUDIANT'),
('Faye', 'Mamadou', '1999-12-10', 'MASCULIN', 'Saint-Louis', 'mamadou.faye@example.com', 'password789', 'ETUDIANT'),
('Sow', 'Fatou', '2001-03-30', 'FEMININ', 'Ziguinchor', 'fatou.sow@example.com', 'password321', 'ResponsablePedagogique'),
('Ndoye', 'Cheikh', '1997-07-05', 'MASCULIN', 'Kaolack', 'cheikh.ndoye@example.com', 'password654', 'ResponsablePedagogique');


