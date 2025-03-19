/*!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.8-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: gestion_inscription_pedagogique
-- ------------------------------------------------------
-- Server version	10.11.8-MariaDB-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `enseignant`
--

DROP TABLE IF EXISTS `enseignant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enseignant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `matricule` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enseignant`
--

/*!40000 ALTER TABLE `enseignant` DISABLE KEYS */;
INSERT INTO `enseignant` VALUES
(1,'amadou.diallo@uasz.sn','ENS001','Diallo','Amadou','771234567'),
(2,'fatou.diop@uasz.sn','ENS002','Diop','Fatou','778765432'),
(3,'moussa.ndiaye@uasz.sn','ENS003','Ndiaye','Moussa','773456789'),
(6,'moussa.ndiaye@uasz.sn','ENS007','Ndiaye','Moussa','773456789'),
(7,'awa.sarr@uasz.sn','ENS004','Sarr','Awa','776543210'),
(8,'oumar.faye@uasz.sn','ENS005','Faye','OumarA','770987654');
/*!40000 ALTER TABLE `enseignant` ENABLE KEYS */;

--
-- Table structure for table `etudiant_ue`
--

DROP TABLE IF EXISTS `etudiant_ue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etudiant_ue` (
  `etudiant_id` bigint(20) NOT NULL,
  `ue_id` bigint(20) NOT NULL,
  PRIMARY KEY (`etudiant_id`,`ue_id`),
  KEY `FKajnc2frbyhptt49nye9wvhgpp` (`ue_id`),
  CONSTRAINT `FK8od4hvb4l6lnhhxwax029xg4m` FOREIGN KEY (`etudiant_id`) REFERENCES `utilisateur` (`id`),
  CONSTRAINT `FKajnc2frbyhptt49nye9wvhgpp` FOREIGN KEY (`ue_id`) REFERENCES `ue` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etudiant_ue`
--

/*!40000 ALTER TABLE `etudiant_ue` DISABLE KEYS */;
INSERT INTO `etudiant_ue` VALUES
(10,50),
(11,50),
(12,50),
(13,50);
/*!40000 ALTER TABLE `etudiant_ue` ENABLE KEYS */;

--
-- Table structure for table `formation`
--

DROP TABLE IF EXISTS `formation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `maxEffectifGroupe` int(11) DEFAULT NULL,
  `niveau` enum('LICENCE','MASTER','DOCTORAT','L1','L2','L3','M1','M2','D1','D2','D3') DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `nombreOptionsRequis` int(11) DEFAULT NULL,
  `responsable_id` bigint(20) DEFAULT NULL,
  `responsableFormation_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe28cx8n7tkhuhuvw1se39p1vu` (`responsable_id`),
  KEY `FK5rv7i7j0htkwc8traylg8ywue` (`responsableFormation_id`),
  CONSTRAINT `FK5rv7i7j0htkwc8traylg8ywue` FOREIGN KEY (`responsableFormation_id`) REFERENCES `enseignant` (`id`),
  CONSTRAINT `FKe28cx8n7tkhuhuvw1se39p1vu` FOREIGN KEY (`responsable_id`) REFERENCES `utilisateur` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formation`
--

/*!40000 ALTER TABLE `formation` DISABLE KEYS */;
INSERT INTO `formation` VALUES
(9,'L3INFO',3,'L3','Licence 3 Informatique',2,1,6),
(10,'MIAGE',4,'M1','Master 1 MIAGE',3,1,1),
(11,'L3-L2I',3,'L3','Licence 3 Informatique',2,1,8),
(12,'M1INFO',4,'M1','Master 1 Informatique',3,1,3);
/*!40000 ALTER TABLE `formation` ENABLE KEYS */;

--
-- Table structure for table `groupe`
--

DROP TABLE IF EXISTS `groupe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupe` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero` int(11) DEFAULT NULL,
  `typeGroupe` enum('TD','TP') DEFAULT NULL,
  `formation_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt0lcksiplcef5bx7dqjnpit15` (`formation_id`),
  CONSTRAINT `FKt0lcksiplcef5bx7dqjnpit15` FOREIGN KEY (`formation_id`) REFERENCES `formation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupe`
--

/*!40000 ALTER TABLE `groupe` DISABLE KEYS */;
INSERT INTO `groupe` VALUES
(1,1,'TD',9),
(2,1,'TP',9),
(3,2,'TP',9),
(4,2,'TD',9);
/*!40000 ALTER TABLE `groupe` ENABLE KEYS */;

--
-- Table structure for table `ue`
--

DROP TABLE IF EXISTS `ue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `coefficient` double DEFAULT NULL,
  `credits` int(11) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `nomResponsable` varchar(255) DEFAULT NULL,
  `obligatoire` bit(1) NOT NULL,
  `volumeHoraire` int(11) DEFAULT NULL,
  `enseignant_id` bigint(20) DEFAULT NULL,
  `formation_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKaice2sj2w4j50omwt223dsl1n` (`code`),
  KEY `FKp1qnahdk4pnaffasfutswv54t` (`enseignant_id`),
  KEY `FKdf4qxqwpiwrc61s546g77e1va` (`formation_id`),
  CONSTRAINT `FKdf4qxqwpiwrc61s546g77e1va` FOREIGN KEY (`formation_id`) REFERENCES `formation` (`id`),
  CONSTRAINT `FKp1qnahdk4pnaffasfutswv54t` FOREIGN KEY (`enseignant_id`) REFERENCES `enseignant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ue`
--

/*!40000 ALTER TABLE `ue` DISABLE KEYS */;
INSERT INTO `ue` VALUES
(50,'INFO101',1.5,6,'Algorithmique Avancée',NULL,0x01,60,1,9),
(51,'INFO102',1,4,'Bases de Données',NULL,0x01,45,2,9),
(52,'INFO103',1,4,'Programmation Web',NULL,0x00,30,2,9),
(53,'INFO201',1.5,6,'Intelligence Artificielle',NULL,0x01,60,1,9),
(54,'INFO202',1,4,'Big Data',NULL,0x00,45,2,9);
/*!40000 ALTER TABLE `ue` ENABLE KEYS */;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `utilisateur` (
  `role` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adresse` varchar(255) DEFAULT NULL,
  `dateNaissance` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `sexe` enum('MASCULIN','FEMININ') DEFAULT NULL,
  `ine` varchar(255) DEFAULT NULL,
  `inscriptionValidee` bit(1) DEFAULT NULL,
  `formation_id` bigint(20) DEFAULT NULL,
  `groupe_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrma38wvnqfaf66vvmi57c71lo` (`email`),
  UNIQUE KEY `UKpp4ggehgdvtp82gtbp42yioho` (`ine`),
  KEY `FKivnrv8jb5qfclw0my2cta6hqq` (`formation_id`),
  KEY `FK1b1dlxawcfeuqi7rvq83siuey` (`groupe_id`),
  CONSTRAINT `FK1b1dlxawcfeuqi7rvq83siuey` FOREIGN KEY (`groupe_id`) REFERENCES `groupe` (`id`),
  CONSTRAINT `FKivnrv8jb5qfclw0my2cta6hqq` FOREIGN KEY (`formation_id`) REFERENCES `formation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--

/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` VALUES
('ResponsablePedagogique',1,'Kaolack','1997-07-05','r','r','r','Cheikh','MASCULIN',NULL,NULL,NULL,NULL),
('ETUDIANT',10,'Dakar','2000-05-15','e','Ngor','e','Fall','MASCULIN','INE001',0x01,9,1),
('ETUDIANT',11,'Dakar','2000-05-15','abdoulaye.gaye@example.com','Gaye','password123','Abdoulaye','MASCULIN','INE002',0x01,9,1),
('ETUDIANT',12,'Thiès','1998-08-22','aminata.diop@example.com','Diop','password456','Aminata','FEMININ','INE003',0x01,9,1),
('ETUDIANT',13,'Saint-Louis','1999-12-10','mamadou.faye@example.com','Faye','password789','Mamadou','MASCULIN','INE004',0x01,9,1),
('ResponsablePedagogique',14,'Ziguinchor','2001-03-30','fatou.sow@example.com','Sow','password321','Fatou','FEMININ',NULL,NULL,NULL,NULL),
('ResponsablePedagogique',15,'Kaolack','1997-07-05','cheikh.ndoye@example.com','Ndoye','password654','Cheikh','MASCULIN',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;

--
-- Dumping routines for database 'gestion_inscription_pedagogique'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-18 22:37:22
