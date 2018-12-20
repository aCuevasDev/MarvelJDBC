CREATE DATABASE  IF NOT EXISTS `marvel` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `marvel`;
-- MySQL dump 10.13  Distrib 5.6.41, for Linux (x86_64)
--
-- Host: localhost    Database: marvel
-- ------------------------------------------------------
-- Server version	5.6.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `enemy`
--

DROP TABLE IF EXISTS `enemy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enemy` (
  `name` varchar(50) NOT NULL,
  `debility` varchar(45) NOT NULL,
  `level` int(11) NOT NULL,
  `place` varchar(50) NOT NULL,
  PRIMARY KEY (`name`),
  KEY `fk_enemy_1_idx` (`place`),
  CONSTRAINT `fk_enemy_1` FOREIGN KEY (`place`) REFERENCES `place` (`name`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enemy`
--

LOCK TABLES `enemy` WRITE;
/*!40000 ALTER TABLE `enemy` DISABLE KEYS */;
INSERT INTO `enemy` VALUES ('BadApple','Java',1,'Xandar'),('BlackLotus','Savate',3,'Kyln'),('DarkWindows','Java',1,'Xandar'),('Destructor','Thunderbolt',1,'New York'),('DoctorOctopus','Spider Sense',3,'Atlantis'),('DoctorVisual','Java',1,'Xandar'),('ElektraNatchios','Savate',1,'Attilan'),('Immortus','Flight',3,'Kyln'),('Loki','Thunderbolt',3,'New York'),('Maestro','Force',3,'Knowhere'),('Thanos','Flight',1,'New York'),('Venom','Spider Sense',1,'Xandar'),('Wendigo','Force',1,'New York');
/*!40000 ALTER TABLE `enemy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gem`
--

DROP TABLE IF EXISTS `gem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gem` (
  `name` varchar(20) NOT NULL,
  `user` varchar(10) NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `place` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`,`user`),
  KEY `fk_gem_1_idx` (`user`),
  KEY `fk_gem_2_idx` (`place`),
  CONSTRAINT `fk_gem_1` FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_gem_2` FOREIGN KEY (`place`) REFERENCES `place` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gem`
--

LOCK TABLES `gem` WRITE;
/*!40000 ALTER TABLE `gem` DISABLE KEYS */;
INSERT INTO `gem` VALUES ('Mind Gem','mar','mar','Muir Island'),('Mind Gem','pepe','ElektraNatchios','Attilan'),('Mind Gem','user1','Loki','New York'),('Mind Gem','user2','Destructor','New York'),('Mind Gem','user3','user3','Latveria'),('Mind Gem','user4','user4','Wakanda'),('Power Gem','mar','mar','Muir Island'),('Power Gem','pepe','Thanos','New York'),('Power Gem','user1','user1','New York'),('Power Gem','user2','Immortus','Kyln'),('Power Gem','user3',NULL,'Ryker Island'),('Power Gem','user4','Destructor','New York'),('Reality Gem','mar','mar','Muir Island'),('Reality Gem','pepe','pepe','Attilan'),('Reality Gem','user1','Loki','New York'),('Reality Gem','user2','DarkWindows','Xandar'),('Reality Gem','user3','Maestro','Knowhere'),('Reality Gem','user4','user4','Wakanda'),('Soul Gem','mar','mar','Muir Island'),('Soul Gem','pepe','DarkWindows','Xandar'),('Soul Gem','user1','Loki','New York'),('Soul Gem','user2','Maestro','Knowhere'),('Soul Gem','user3','Wendigo','New York'),('Soul Gem','user4','Thanos','New York'),('Space Gem','mar','mar','Muir Island'),('Space Gem','pepe','Destructor','New York'),('Space Gem','user1','Venom','Xandar'),('Space Gem','user2',NULL,'Ryker Island'),('Space Gem','user3','Destructor','New York'),('Space Gem','user4','ElektraNatchios','Attilan'),('Time Gem','mar','mar','Muir Island'),('Time Gem','pepe','DarkWindows','Xandar'),('Time Gem','user1','DarkWindows','Xandar'),('Time Gem','user2','BadApple','Xandar'),('Time Gem','user3','Destructor','New York'),('Time Gem','user4','Venom','Xandar');
/*!40000 ALTER TABLE `gem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `place`
--

DROP TABLE IF EXISTS `place`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `place` (
  `name` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  `north` varchar(50) DEFAULT NULL,
  `south` varchar(50) DEFAULT NULL,
  `east` varchar(50) DEFAULT NULL,
  `west` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `place`
--

LOCK TABLES `place` WRITE;
/*!40000 ALTER TABLE `place` DISABLE KEYS */;
INSERT INTO `place` VALUES ('Asgard','It is the world of the Asgardians surrounded by an incomplete wall.','Knowhere','New York','Muir Island','Xandar'),('Atlantis','Atlantis is a steep island abundant in resources. There are all kinds of minerals. There are also large forests that provide unlimited timber; numerous animals, domestic and wild, especially elephants.','New York',NULL,'Wakanda','Kyln'),('Attilan','Attilan is a city located on an island in the North Atlantic. It is under a protective dome, protecting it and keeping it hidden.',NULL,'Symkaria',NULL,'Muir Island'),('Genosha','It is an island that boasts of having a high standard of living and an excellent economy. However, Genosha\'s prosperity was built on the slavery of its mutant population.','Wakanda',NULL,NULL,NULL),('Knowhere','Located within what appears to be a head cut from a Celestial floating near the Rasgido, Knowhere acts as an impromptu port of call and observatory of the final destination of the universe for intergalactic travelers of all species and all epochs.',NULL,'Asgard',NULL,NULL),('Kyln','It is conformed by several united spherical containment units located at the edge of the Universe, right at the edge of the expansion (the \"Crunch\"). Its maintenance is in charge of many interstellar races and all kinds of mechanical automatons (flying robots, nanotechnology, etc.).','Ryker Island',NULL,'Atlantis',NULL),('Latveria','Most of the land of Latveria is dedicated to agriculture, the large industries do not have offices in the country, although in its capital, more specifically in the seat of government is housed one of the most advanced robotics laboratories on the planet.','Muir Island','Wakanda','Symkaria','New York'),('Muir Island','Muir Island is an island that was purchased by Dr. Moira MacTaggert when she won the Nobel Prize in genetic research. He created a Mutant Research Center where he could carry out research and treatment of mutants.',NULL,'Latveria','Attilan','Asgard'),('New York','New York City is the largest city in the state of New York and the city with the largest metropolitan area in the United States of America. ','Asgard','Atlantis','Latveria','Ryker Island'),('Ryker Island','Ryker Island is an island converted into a common criminal prison and superheroes.','Xandar','Kyln','New York',NULL),('Symkaria','Symkaria is the original home of mercenary Silver Sable, and borders Latveria, the country ruled by Victor Von Doom (Doctor Death).','Attilan',NULL,NULL,'Latveria'),('Wakanda','It was named after its native inhabitants, the \"Wakandianos\". It is a nation that is recognized by its nature to be very united in all aspects, a society where its culture is based on solidarity and affection towards others.','Latveria','Genosha',NULL,'Atlantis'),('Xandar','The planet is represented in the Tantra system in the Andromeda galaxy. It is best known as the homeworld of the Nova Corps, a working group of intergalactic police.',NULL,'Ryker Island','Asgard',NULL);
/*!40000 ALTER TABLE `place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `superhero`
--

DROP TABLE IF EXISTS `superhero`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `superhero` (
  `name` varchar(50) NOT NULL,
  `superpower` varchar(45) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `superhero`
--

LOCK TABLES `superhero` WRITE;
/*!40000 ALTER TABLE `superhero` DISABLE KEYS */;
INSERT INTO `superhero` VALUES ('BlackWidow','Savate'),('Hulk','Force'),('IronMan','Flight'),('Spider-Man','Spider Sense'),('SuperJava','Java'),('Thor','Thunderbolt');
/*!40000 ALTER TABLE `superhero` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(10) NOT NULL,
  `pass` varchar(10) NOT NULL,
  `level` int(11) NOT NULL,
  `superhero` varchar(50) NOT NULL,
  `place` varchar(50) NOT NULL,
  `points` int(11) NOT NULL,
  PRIMARY KEY (`username`),
  KEY `fk_user_1_idx` (`superhero`),
  KEY `fk_user_2_idx` (`place`),
  CONSTRAINT `fk_user_1` FOREIGN KEY (`superhero`) REFERENCES `superhero` (`name`) ON UPDATE CASCADE,
  CONSTRAINT `fk_user_2` FOREIGN KEY (`place`) REFERENCES `place` (`name`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('mar','mar',2,'SuperJava','Muir Island',5),('pepe','pepe',1,'Hulk','Attilan',11),('user1','1234',1,'BlackWidow','New York',8),('user2','1234',1,'IronMan','New York',0),('user3','1234',2,'Spider-Man','Latveria',7),('user4','1234',1,'Thor','Wakanda',11);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-18 12:53:24
