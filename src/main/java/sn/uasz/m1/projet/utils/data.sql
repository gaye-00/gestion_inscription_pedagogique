
INSERT INTO enseignant (matricule, nom, prenom, email, telephone)
VALUES 
    ('ENS001', 'Diallo', 'Amadou', 'amadou.diallo@uasz.sn', '771234567'),
    ('ENS002', 'Diop', 'Fatou', 'fatou.diop@uasz.sn', '778765432'),
    ('ENS003', 'Ndiaye', 'Moussa', 'moussa.ndiaye@uasz.sn', '773456789');


INSERT INTO utilisateur (nom, prenom, dateNaissance, ine, sexe, adresse, email, inscriptionValidee, password, role)
VALUES 
('Ngor', 'Fall', '2000-05-15', "INE001", 'MASCULIN', 'Dakar', 'e', false, 'e', 'ETUDIANT'),
('Gaye', 'Abdoulaye', '2000-05-15', "INE002", 'MASCULIN', 'Dakar', 'abdoulaye.gaye@example.com', false, 'password123', 'ETUDIANT'),
('Diop', 'Aminata', '1998-08-22', "INE003", 'FEMININ', 'Thiès', 'aminata.diop@example.com', false, 'password456', 'ETUDIANT'),
('Faye', 'Mamadou', '1999-12-10', "INE004", 'MASCULIN', 'Saint-Louis', 'mamadou.faye@example.com', false, 'password789', 'ETUDIANT'),
('Tamba', 'Mbaye', '2004-03-30', null, 'FEMININ', 'Tamba', 'r', null, 'r', 'ResponsablePedagogique'),
('Sow', 'Fatou', '2001-03-30', null, 'FEMININ', 'Ziguinchor', 'fatou.sow@example.com', null, 'password321', 'ResponsablePedagogique'),
('Ndoye', 'Cheikh', '1997-07-05', null, 'MASCULIN', 'Kaolack', 'cheikh.ndoye@example.com', null, 'password654', 'ResponsablePedagogique');

INSERT INTO formation (nom, code, nombreOptionsRequis, niveau, responsable_id)
VALUES 
    ('Licence 3 Informatique', 'L3INFO', 2, 'L3', 5),
    ('Master 1 Informatique', 'M1INFO', 3, 'M1', 6);

INSERT INTO enseignant (matricule, nom, prenom, email, telephone)
VALUES 
    ('ENS001', 'Diallo', 'Amadou', 'amadou.diallo@uasz.sn', '771234567'),
    ('ENS002', 'Diop', 'Fatou', 'fatou.diop@uasz.sn', '778765432'),
    ('ENS003', 'Ndiaye', 'Moussa', 'moussa.ndiaye@uasz.sn', '773456789'),
    ('ENS004', 'Sarr', 'Awa', 'awa.sarr@uasz.sn', '776543210'),
    ('ENS005', 'Faye', 'Oumar', 'oumar.faye@uasz.sn', '770987654');


INSERT INTO ue (code, nom, volumeHoraire, coefficient, credits, nomResponsable, formation_id, enseignant_id, obligatoire)
VALUES 
    ('INFO101', 'Algorithmique Avancée', 60, 1.5, 6, 'Amadou Diallo', 1, 1, true),
    ('INFO102', 'Bases de Données', 45, 1.0, 4, 'Fatou Diop', 1, 2, true),
    ('INFO103', 'Programmation Web', 30, 1.0, 4, 'Moussa Ndiaye', 1, 3, false),
    ('INFO201', 'Intelligence Artificielle', 60, 1.5, 6, 'Amadou Diallo', 2, 1, true),
    ('INFO202', 'Big Data', 45, 1.0, 4, 'Fatou Diop', 2, 2, false);

INSERT INTO ue (code, nom, volumeHoraire, coefficient, credits, nomResponsable, formation_id, enseignant_id, obligatoire)
VALUES 
    ('INFO104', 'Anglais', 60, 1.5, 6, 'Mme Dabo', 1, 1, false),
    ('INFO105', 'Optimatisation', 45, 1.0, 4, 'Youssou Dieng', 1, 2, false),
    ('INFO106', 'Theorie des graphes', 30, 1.0, 4, 'Youssou Dieng', 1, 3, false);


INSERT INTO groupe (numero, typeGroupe)
VALUES 
    (1, 'TD'),
    (2, 'TD'),
    (1, 'TP'),
    (2, 'TP');


-- INSERT INTO utilisateur (nom, prenom, dateNaissance, sexe, adresse, email, password, role)
-- VALUES 
--     ('Fall', 'Awa', '2001-05-12', 'FEMININ', 'Dakar', 'awa.fall@etu.uasz.sn', '123456', 'ETUDIANT'),
--     ('Sow', 'Ibrahima', '2000-11-22', 'MASCULIN', 'Ziguinchor', 'ibrahima.sow@etu.uasz.sn', '654321', 'ETUDIANT'),
--     ('Ba', 'Mariama', '1999-02-10', 'FEMININ', 'Thiès', 'mariama.ba@etu.uasz.sn', '111222', 'ETUDIANT'),
--     ('Seck', 'Ousmane', '2001-08-30', 'MASCULIN', 'Kaolack', 'ousmane.seck@etu.uasz.sn', '333444', 'ETUDIANT');

-- INSERT INTO etudiant (ine, groupe_id, formation_id, inscriptionValidee, id)
-- VALUES 
--     ('INE001', 1, 1, true, 1),
--     ('INE002', 2, 1, false, 2),
--     ('INE003', 3, 2, true, 3),
--     ('INE004', 4, 2, false, 4);


INSERT INTO etudiant_ue (etudiant_id, ue_id)
VALUES 
    (1, 1), (1, 2),
    (2, 1), (2, 3),
    (3, 4), (3, 5),
    (4, 4);


-- INSERT INTO utilisateur (nom, prenom, dateNaissance, sexe, adresse, email, password, role)
-- VALUES 
--     ('Ndour', 'Abdou', '1980-07-15', 'MASCULIN', 'Saint-Louis', 'abdou.ndour@uasz.sn', 'admin123', 'ResponsablePedagogique');

-- INSERT INTO responsable_pedagogique (id)
-- VALUES (5);


SELECT * FROM formation;
SELECT * FROM enseignant;
SELECT * FROM ue;
SELECT * FROM groupe;
SELECT * FROM utilisateur;
SELECT * FROM etudiant;
SELECT * FROM etudiant_ue;
-- SELECT * FROM responsable_pedagogique;
