-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 23, 2024 at 06:55 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `librarydb`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `BookId` int(11) NOT NULL,
  `Title` varchar(100) NOT NULL,
  `Author` varchar(100) NOT NULL,
  `Publisher` varchar(100) NOT NULL,
  `Year` int(11) NOT NULL,
  `ISBN` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`BookId`, `Title`, `Author`, `Publisher`, `Year`, `ISBN`) VALUES
(1, 'Harry Porter', 'Yogesh', 'Vraj Publications', 2001, '14'),
(2, 'Before Death', 'Chavda', 'Brothers Publications', 2015, '26'),
(3, 'Zombie', 'jio', 'sony', 2019, '32'),
(5, 'Black Pearls Curse', 'Barbosa', 'Pirates', 2003, '125'),
(6, 'Salazars Revenge', 'Salazar', 'Pirates', 2013, '156'),
(7, 'The Perfect Murder', 'Ruskin Bond', 'Rupa Publications', 2010, '36');

--
-- Triggers `books`
--
DELIMITER $$
CREATE TRIGGER `deletedBooks` BEFORE DELETE ON `books` FOR EACH ROW INSERT INTO logbooks VALUES(old.BookId,old.Title,old.Author,old.Publisher,old.Year,old.ISBN)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `BorrowId` int(11) NOT NULL,
  `BookId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `BorrowDate` date NOT NULL,
  `ReturnDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `borrow`
--
DELIMITER $$
CREATE TRIGGER `deleted_bookHistory` BEFORE DELETE ON `borrow` FOR EACH ROW INSERT into log_user_book_borrow_details VALUES(old.BorrowId,old.BookId,old.UserId,old.BorrowDate,old.ReturnDate)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `logbooks`
--

CREATE TABLE `logbooks` (
  `BookID` int(11) NOT NULL,
  `Title` varchar(100) NOT NULL,
  `Author` varchar(100) NOT NULL,
  `Publisher` varchar(100) NOT NULL,
  `Year` int(11) NOT NULL,
  `ISBN` varchar(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `logbooks`
--

INSERT INTO `logbooks` (`BookID`, `Title`, `Author`, `Publisher`, `Year`, `ISBN`) VALUES
(4, 'At The Worlds End', 'Jack', 'Pirates', 2007, '145');

-- --------------------------------------------------------

--
-- Table structure for table `log_user_book_borrow_details`
--

CREATE TABLE `log_user_book_borrow_details` (
  `BorrowId` int(11) NOT NULL,
  `BookId` int(11) NOT NULL,
  `UserId` int(11) NOT NULL,
  `BorrowDate` date NOT NULL,
  `ReturnDate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `log_user_book_borrow_details`
--

INSERT INTO `log_user_book_borrow_details` (`BorrowId`, `BookId`, `UserId`, `BorrowDate`, `ReturnDate`) VALUES
(1, 4, 2, '2024-08-23', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `readed_bookhistory`
--

CREATE TABLE `readed_bookhistory` (
  `UserId` int(11) NOT NULL,
  `Book_Name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `readed_bookhistory`
--

INSERT INTO `readed_bookhistory` (`UserId`, `Book_Name`) VALUES
(3, 'Salazars Revenge'),
(3, 'Harry Porter'),
(3, 'Zombie'),
(4, 'Zombie'),
(4, 'Black Pearls Curse'),
(4, 'The Perfect Murder'),
(4, 'Salazars Revenge');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `Name`, `Email`, `Role`) VALUES
(1, 'Yogesh', 'Yogesh@gmail.com', 'Admin'),
(2, 'nirbhay', 'nirbhay@gmail.com', 'User'),
(3, 'milan vekariya', 'milan@gmail.com', 'User'),
(4, 'prince', 'prince@gmail.com', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`BookId`),
  ADD UNIQUE KEY `isbn_unique` (`ISBN`),
  ADD UNIQUE KEY `BookId` (`BookId`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`BorrowId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `BookId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `BorrowId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
