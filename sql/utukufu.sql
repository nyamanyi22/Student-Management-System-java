-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Nov 17, 2024 at 08:18 AM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `utukufutechnical`
--

-- --------------------------------------------------------

--
-- Table structure for table `class_records`
--

DROP TABLE IF EXISTS `class_records`;
CREATE TABLE IF NOT EXISTS `class_records` (
  `class_id` varchar(10) NOT NULL,
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `teacher_in_charge` varchar(100) NOT NULL,
  PRIMARY KEY (`teacher_in_charge`),
  KEY `class_name` (`class_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `class_records`
--

INSERT INTO `class_records` (`class_id`, `class_name`, `teacher_in_charge`) VALUES
('Eb15/eng', 'Engineering', 'Cafy'),
('Eb15/cs', 'Computer Science', 'Caro'),
('Eb14/22', 'Computer Science', 'faruk'),
('Bb14/tc', 'Teaching', 'Felix'),
('Eb15/cs', 'Teaching', 'Kelly'),
('Eb14/eng', 'Engineering', 'Mbati'),
('Eb14/cs', 'Computer Science', 'Vik');

-- --------------------------------------------------------

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
CREATE TABLE IF NOT EXISTS `staff` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `staff_id` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `staff_id` (`staff_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `staff`
--

INSERT INTO `staff` (`id`, `first_name`, `last_name`, `staff_id`, `email`, `phone`, `password`) VALUES
(2, 'Dan', 'Omari', '1234', 'dan@gmai.com', '0719568577', '123');

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `student_id` int NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `class` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  KEY `class` (`class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `first_name`, `last_name`, `age`, `class`, `email`, `phone_number`) VALUES
(12390, 'Felly', 'Mery', 18, 'Engineering', 'merry@gmail.com', '0123986478'),
(23098, 'Felicity', 'Kembo', 23, 'Engineering', 'kembo@gmail.com', '01127865'),
(208764, 'Reci', 'Felop', 19, 'Teaching', 'reci@gail.com', '012793589'),
(209813, 'Alia', 'Weri', 23, 'Computer Science', 'weri@gamil.com', '012348973'),
(326790, 'Sofi', 'Dabs', 21, 'Teaching', 'safo@gmail.com', '01287658'),
(412356, 'Anita', 'Wangare', 23, 'Computer Science', 'Wanga@gmail.com', '0127854290');

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
  `id` int NOT NULL AUTO_INCREMENT,
  `First_name` varchar(50) NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `Id_Number` varchar(20) NOT NULL,
  `Course` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Age` int NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Course` (`Course`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`id`, `First_name`, `Last_Name`, `Id_Number`, `Course`, `Age`, `Email`, `Password`) VALUES
(20, 'Liz', 'Oma', '239856', 'Engineering', 23, 'liz@gmail.com', '12'),
(21, 'Moh', 'Raf', '237890', 'Computer Science', 43, 'moh@gmail.com', '23'),
(22, 'Zew', 'Fero', '234590', 'Teaching', 29, '2233978', '12'),
(23, 'Sofi', 'Eriuy', '209876', 'Engineering', 24, 'sofi@gmail.com', '45'),
(24, 'Kiz', 'Sael', '327890', 'Teaching', 19, 'kiz@gmail.com', '34'),
(25, 'Eddy', 'Sam', '230879', 'Computer Science', 39, 'sam@gmail.com', '23');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `students_ibfk_1` FOREIGN KEY (`class`) REFERENCES `class_records` (`class_name`) ON DELETE CASCADE ON UPDATE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
