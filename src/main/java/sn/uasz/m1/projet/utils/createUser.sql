sudo mariadb -u root


CREATE USER 'm1Informatique'@'%' IDENTIFIED BY 'm1Informatique';


GRANT ALL PRIVILEGES ON *.* TO 'm1Informatique'@'%' WITH GRANT OPTION;


FLUSH PRIVILEGES;


EXIT;

----------------------------------------------------------------------------------------------------------------------
INSERT INTO utilisateur (nom, prenom, dateNaissance, sexe, adresse, email, inscriptionValidee, password, role)
VALUES 
('Ngor', 'Fall', '2000-05-15', 'MASCULIN', 'Dakar', 'e', false, 'e', 'ETUDIANT'),
('Gaye', 'Abdoulaye', '2000-05-15', 'MASCULIN', 'Dakar', 'abdoulaye.gaye@example.com', false, 'password123', 'ETUDIANT'),
('Diop', 'Aminata', '1998-08-22', 'FEMININ', 'Thiès', 'aminata.diop@example.com', false, 'password456', 'ETUDIANT'),
('Faye', 'Mamadou', '1999-12-10', 'MASCULIN', 'Saint-Louis', 'mamadou.faye@example.com', false, 'password789', 'ETUDIANT'),
('Tamba', 'Mbaye', '2004-03-30', 'FEMININ', 'Tamba', 'r', null, 'r', 'ResponsablePedagogique'),
('Sow', 'Fatou', '2001-03-30', 'FEMININ', 'Ziguinchor', 'fatou.sow@example.com', null, 'password321', 'ResponsablePedagogique'),
('Ndoye', 'Cheikh', '1997-07-05', 'MASCULIN', 'Kaolack', 'cheikh.ndoye@example.com', null, 'password654', 'ResponsablePedagogique');



SELECT * FROM utilisateur WHERE email = 'abdoulaye.gaye@example.com' AND password = 'password123';

INSERT INTO utilisateur (nom, prenom, dateNaissance, sexe, adresse, email, password, role)
VALUES 
('Tamba', 'Mbaye', '2004-03-30', 'FEMININ', 'Tamba', 'r', 'r', 'ResponsablePedagogique');