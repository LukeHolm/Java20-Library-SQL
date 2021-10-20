CREATE DATABASE cobalt_reserve;
DROP DATABASE cobalt_reserve;

USE cobalt_reserve;

DROP TABLE books;
CREATE TABLE books (
BookId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
Title VARCHAR(250) NOT NULL,
ISBN VARCHAR(13) NOT NULL, 
Year INT,
Availability BOOLEAN DEFAULT TRUE
);

DROP TABLE authors;
CREATE TABLE authors (
AuthorId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
Author VARCHAR(100)
);

CREATE TABLE bookSeries (
seriesId INT NOT NULL PRIMARY KEY auto_increment,
seriesName VARCHAR(100)
);

DROP TABLE bookAuthor;
CREATE TABLE bookAuthor (
BookId INT NOT NULL PRIMARY KEY auto_increment,
AuthorId INT NOT NULL
);

DROP TABLE members;
CREATE TABLE members (
memberiD INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
FirstName VARCHAR(50) NOT NULL,
LastName VARCHAR(50) NOT NULL
);

DROP TABLE borrowedBooks;
CREATE TABLE borrowedBooks (
BookId INT NOT NULL,
MemberId INT NOT NULL,
LoanDate DATE NOT NULL,
ReturnDate DATE NOT NULL,
RegisteredReturnDate DATE
);

DROP VIEW Library;
CREATE VIEW Library AS 
SELECT 
books.bookId AS Id, 
books.title AS Title, 
authors.author AS Author, 
books.isbn AS ISBN, 
books.Year AS Year, 
IF(books.available, 'Available', 'Borrowed') Availability 
FROM bookauthor
	LEFT JOIN books ON books.bookId = bookauthor.bookId
    LEFT JOIN authors ON authors.authorId = bookauthor.authorId
;

INSERT INTO bookauthor (authorId) VALUES (1);
INSERT INTO bookauthor (authorId) VALUES (1);
INSERT INTO bookauthor (authorId) VALUES (1);
INSERT INTO bookauthor (authorId) VALUES (2);
INSERT INTO bookauthor (authorId) VALUES (2);
INSERT INTO bookauthor (authorId) VALUES (2);

INSERT INTO books (title, isbn, yearOfRelease) VALUES ("Fellowship Of The Ring", "9780008376123", 1954);
INSERT INTO books (title, isbn, yearOfRelease) VALUES ("The Two Towers", "9780008376130", 1954);
INSERT INTO books (title, isbn, yearOfRelease) VALUES ("The Return of the King", "9780008376147", 1955);
INSERT INTO books (title, isbn, yearOfRelease) VALUES ("Homeland", "9781932796407", 1990);
INSERT INTO books (title, isbn, yearOfRelease) VALUES ("Exile", "9781932796469", 1990);
INSERT INTO books (title, isbn, yearOfRelease) VALUES ("Sojourn", "9781932796575", 1991);

INSERT INTO authors	(author) VALUES ("J.R.R. Tolkien");
INSERT INTO authors (author) VALUES ("R.A. Salvatore");