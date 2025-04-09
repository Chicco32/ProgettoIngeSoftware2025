CREATE DATABASE  IF NOT EXISTS `dbingesw` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `dbingesw`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: dbingesw
-- ------------------------------------------------------
-- Server version	5.7.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `archivio storico visite`
--

DROP TABLE IF EXISTS `archivio storico visite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivio storico visite` (
  `Codice Archivio` int(11) NOT NULL,
  `Tipo di Visita` int(11) NOT NULL,
  `Volontario Selezionato` varchar(45) NOT NULL,
  `Data programmata` date NOT NULL,
  PRIMARY KEY (`Codice Archivio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='archivio storico separato solo per le visite effettuate';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `archivio visite attive`
--

DROP TABLE IF EXISTS `archivio visite attive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `archivio visite attive` (
  `Codice Archivio` int(11) NOT NULL,
  `Stato Visita` enum('proponibile','proposta','completa','cancellata','confermata','effettuata') NOT NULL COMMENT 'Stato attuale della specifica visita possibile fra ''proponibile'', ''proposta'', ''completa'', ''annullata'', ''confermata'', ''effettuata',
  `Tipo di Visita` int(11) NOT NULL,
  `Volontario Selezionato` varchar(45) NOT NULL,
  `Data programmata` date DEFAULT NULL,
  PRIMARY KEY (`Codice Archivio`),
  KEY `fk_Archivio delle Visite_Volontari Disponibili1_idx` (`Tipo di Visita`,`Volontario Selezionato`),
  CONSTRAINT `fk_Archivio delle Visite_Volontari Disponibili1` FOREIGN KEY (`Tipo di Visita`, `Volontario Selezionato`) REFERENCES `volontari disponibili` (`Tipo di Visita`, `Volontario Nickname`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='archvio delle visite attive in stato proponibile, prrposto, confermato, cancellato o completo\nuna volta segnate come confermate devono essere spostaTe nell''archivio storico per evitare conflitti di coerenza';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `configuratore`
--

DROP TABLE IF EXISTS `configuratore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuratore` (
  `Nickname` varchar(45) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Salt` varchar(50) NOT NULL,
  PRIMARY KEY (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fruitori`
--

DROP TABLE IF EXISTS `fruitori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fruitori` (
  `Nickname` varchar(45) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Salt` varchar(50) NOT NULL,
  PRIMARY KEY (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fruitori iscritti alle visite`
--

DROP TABLE IF EXISTS `fruitori iscritti alle visite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fruitori iscritti alle visite` (
  `Fruitore` varchar(45) NOT NULL,
  `Visita iscritta` int(11) NOT NULL,
  `Codice prenotazione` varchar(100) NOT NULL,
  `Numero iscritti` int(5) DEFAULT NULL,
  PRIMARY KEY (`Fruitore`,`Visita iscritta`),
  UNIQUE KEY `Codice prenotazione_UNIQUE` (`Codice prenotazione`),
  KEY `fk_Fruitori_has_Archivio delle Visite_Archivio delle Visite_idx` (`Visita iscritta`),
  KEY `fk_Fruitori_has_Archivio delle Visite_Fruitori1_idx` (`Fruitore`),
  CONSTRAINT `fk_Fruitori_has_Archivio delle Visite_Archivio delle Visite1` FOREIGN KEY (`Visita iscritta`) REFERENCES `archivio visite attive` (`Codice Archivio`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_Fruitori_has_Archivio delle Visite_Fruitori1` FOREIGN KEY (`Fruitore`) REFERENCES `fruitori` (`Nickname`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `giorni della settimana`
--

DROP TABLE IF EXISTS `giorni della settimana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giorni della settimana` (
  `Nome` varchar(20) NOT NULL,
  PRIMARY KEY (`Nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `giorni programmabili delle visite`
--

DROP TABLE IF EXISTS `giorni programmabili delle visite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giorni programmabili delle visite` (
  `Tipo di Visita` int(11) NOT NULL,
  `Giorno della Settimana` varchar(10) NOT NULL,
  PRIMARY KEY (`Tipo di Visita`,`Giorno della Settimana`),
  KEY `fk_Visita_has_Giorni della Settimana_Giorni della Settimana_idx` (`Giorno della Settimana`),
  KEY `fk_Visita_has_Giorni della Settimana_Visita_idx` (`Tipo di Visita`),
  CONSTRAINT `fk_Visita_has_Giorni della Settimana_Giorni della Settimana1` FOREIGN KEY (`Giorno della Settimana`) REFERENCES `giorni della settimana` (`Nome`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Visita_has_Giorni della Settimana_Visita` FOREIGN KEY (`Tipo di Visita`) REFERENCES `tipo di visita` (`Codice Tipo di Visita`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `luogo`
--

DROP TABLE IF EXISTS `luogo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `luogo` (
  `Nome` varchar(45) NOT NULL,
  `Descrizione` varchar(200) DEFAULT NULL,
  `Indirizzo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Nome`),
  UNIQUE KEY `Indirizzo_UNIQUE` (`Indirizzo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo di visita`
--

DROP TABLE IF EXISTS `tipo di visita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo di visita` (
  `Codice Tipo di Visita` int(11) NOT NULL,
  `Punto di Incontro` varchar(45) NOT NULL,
  `Titolo` varchar(45) DEFAULT NULL,
  `Descrizione` varchar(200) DEFAULT NULL,
  `Giorno di Inizio (periodo anno)` date DEFAULT NULL,
  `Giorno di Fine (periodo anno)` date DEFAULT NULL,
  `Ora di inizio` time DEFAULT NULL,
  `Durata` int(11) DEFAULT NULL,
  `Necessita Biglietto` tinyint(4) DEFAULT NULL,
  `Min Partecipanti` int(11) DEFAULT NULL,
  `Max Partecipanti` int(11) DEFAULT NULL,
  `Configuratore referente` varchar(45) NOT NULL,
  PRIMARY KEY (`Codice Tipo di Visita`),
  KEY `fk_Visita_Luogo1_idx` (`Punto di Incontro`),
  KEY `fk_Visita_Configuratore1_idx` (`Configuratore referente`),
  CONSTRAINT `fk_Visita_Configuratore1` FOREIGN KEY (`Configuratore referente`) REFERENCES `configuratore` (`Nickname`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Visita_Luogo1` FOREIGN KEY (`Punto di Incontro`) REFERENCES `luogo` (`Nome`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `volontari disponibili`
--

DROP TABLE IF EXISTS `volontari disponibili`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volontari disponibili` (
  `Tipo di Visita` int(11) NOT NULL,
  `Volontario Nickname` varchar(45) NOT NULL,
  PRIMARY KEY (`Tipo di Visita`,`Volontario Nickname`),
  KEY `fk_Visita_has_Volontario_Volontario1_idx` (`Volontario Nickname`),
  KEY `fk_Visita_has_Volontario_Visita1_idx` (`Tipo di Visita`),
  CONSTRAINT `fk_Visita_has_Volontario_Visita1` FOREIGN KEY (`Tipo di Visita`) REFERENCES `tipo di visita` (`Codice Tipo di Visita`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_Visita_has_Volontario_Volontario1` FOREIGN KEY (`Volontario Nickname`) REFERENCES `volontario` (`Nickname`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `volontario`
--

DROP TABLE IF EXISTS `volontario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volontario` (
  `Nickname` varchar(45) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `Salt` varchar(50) NOT NULL,
  PRIMARY KEY (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'dbingesw'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `EliminaVisiteCancellate` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `EliminaVisiteCancellate` ON SCHEDULE EVERY 1 DAY STARTS '2025-03-13 23:59:59' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    DELETE FROM `dbingesw`.`archivio visite attive`
    WHERE `Data programmata` < CURDATE() 
    AND `Stato Visita` = 'cancellata';
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `SpostaVisiteScadute` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `SpostaVisiteScadute` ON SCHEDULE EVERY 1 DAY STARTS '2025-03-13 23:59:59' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
   CALL dbingesw.`ArchiviaVisite`;  
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'dbingesw'
--
/*!50003 DROP FUNCTION IF EXISTS `generaChiaveArchivio` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generaChiaveArchivio`() RETURNS int(11)
    DETERMINISTIC
BEGIN
    DECLARE max_chiave INT;
    
    -- Trova il massimo valore di chiaveArchivio tra entrambe le tabelle
    SELECT GREATEST(
        COALESCE((SELECT MAX(`Codice Archivio`) FROM dbingesw.`archivio storico visite`), 0),
        COALESCE((SELECT MAX(`Codice Archivio`) FROM dbingesw.`archivio visite attive`), 0)
    ) + 1 INTO max_chiave;

    RETURN max_chiave;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generaChiaveTipoVisita` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generaChiaveTipoVisita`() RETURNS int(11)
    DETERMINISTIC
BEGIN
    DECLARE max_val INT;
    
    -- Trova il valore massimo attuale della colonna e aggiunge 1
    SELECT COALESCE(MAX(`Codice Tipo di Visita`), 0) + 1 INTO max_val FROM `tipo di visita`;
    
    RETURN max_val;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ArchiviaVisite` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `ArchiviaVisite`()
BEGIN
    -- Copia le visite non attive in archivio_storico
    INSERT INTO dbingesw.`archivio storico visite` (`Codice Archivio`, `Tipo di Visita`, `Volontario Selezionato`, `Data Programmata`)
    SELECT `Codice Archivio`, `Tipo di Visita`, `Volontario Selezionato`, `Data Programmata`
    FROM dbingesw.`archivio visite attive`
    WHERE `Stato Visita` = 'effettuata';

    -- Elimina le visite non attive dall'archivio attivo
    DELETE FROM dbingesw.`archivio visite attive` WHERE `Stato Visita` = 'effettuata';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `EliminaDatiOrfani` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `EliminaDatiOrfani`()
BEGIN

-- Prima elimino i tipi di visita senza disponibilità che sono quelli con più dipendeze
DELETE FROM dbingesw.`tipo di visita` WHERE `tipo di visita`.`Codice Tipo di Visita` NOT IN 
	(SELECT `Tipo di Visita` FROM dbingesw.`volontari disponibili`);
 
 -- Poi elimino i luoghi senza più visite associate
 DELETE FROM dbingesw.`luogo` WHERE Nome NOT IN 
	(SELECT `Punto di Incontro` FROM dbingesw.`tipo di visita`);
    
-- Infine elimino definitivamente i volontari che non sono più associati in caso di volontari irraggiungibili
DELETE FROM dbingesw.volontario where Nickname not in 
	(SELECT `Volontario Nickname` FROM dbingesw.`volontari disponibili`);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetVisite` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetVisite`(IN stato VARCHAR(50))
BEGIN
    IF stato = 'effettuata' THEN
        -- Query per visite effettuate (solo archivio storico)
        SET @query = 'SELECT archivi.*, tv.`Titolo`, tv.`Punto di Incontro`
                      FROM dbingesw.`archivio storico visite` archivi
                      JOIN dbingesw.`tipo di visita` tv 
                      ON archivi.`Tipo di Visita` = tv.`Codice Tipo di Visita`';
    ELSE
        -- Query per altri stati (solo archivio attivo)
        SET @query = concat('SELECT `Codice Archivio`, `Tipo di Visita`, `Volontario Selezionato`, `Data programmata`,
                             `Titolo`,`Punto di Incontro`
                      FROM dbingesw.`archivio visite attive` archivi
                      JOIN dbingesw.`tipo di visita` tv 
                      ON archivi.`Tipo di Visita` = tv.`Codice Tipo di Visita`
                      WHERE archivi.`Stato Visita` = "',stato,'"');
    END IF;

   -- Preparazione ed esecuzione della query concatenata
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-06 20:13:14
