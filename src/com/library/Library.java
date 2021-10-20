package com.library;

import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Library {
    private static final Menu MENU = new Menu();
    private static Statement sqlStatement;
    private static final int year = Calendar.getInstance().get(Calendar.YEAR) + 1;
    private static final LocalDate dateToday = LocalDate.now();
    private static final LocalDate twoWeeks = dateToday.plusDays(14);
    private static final Date today = java.sql.Date.valueOf(dateToday);
    private static final Date returnDate = java.sql.Date.valueOf(twoWeeks);


    //DONE!
    public void borrowBook () throws SQLException{
        sqlStatement = Run.getSqlStatement();
        String nameOfBook = MENU.enterValue("name of book");
        String query = "SELECT * FROM books WHERE title LIKE '" + nameOfBook + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "bookId");

        if (bookFound) {
            System.out.println("Book(s) found:\n");
            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE title LIKE ? ");
            statement.setString(1, "'" + nameOfBook + "'");
            statement.executeQuery();
            ResultClass.setResult(sqlStatement, "library", "title", "'" + nameOfBook + "'");
            listAllFromTable();

            int bookId = MENU.enterInteger("book id", 1, 999999);
            bookId = ResultClass.idMatches("books", bookId, "BookId");

            boolean available = ResultClass.checkAvailability(sqlStatement,"Availability",nameOfBook,bookId);


            if (available) {


                int memberId = MENU.enterInteger("member id", 1, 999);
                memberId = ResultClass.idMatches("members", memberId, "MemberId");

                PreparedStatement borrowBook = sqlStatement.getConnection().prepareStatement("INSERT INTO borrowedBooks (bookId, MemberId, loanDate,returnDate) VALUES (?,?,?,?);");
                borrowBook.setInt(1, bookId);
                borrowBook.setInt(2, memberId);
                borrowBook.setDate(3, (java.sql.Date) today);
                borrowBook.setDate(4, (java.sql.Date) returnDate);
                borrowBook.executeUpdate();

                PreparedStatement setAvailability = sqlStatement.getConnection().prepareStatement("UPDATE books SET Availability = FALSE WHERE BookId = ?;");
                setAvailability.setInt(1, bookId);
                setAvailability.executeUpdate();

                System.out.println(nameOfBook + " was successfully checked out!");
                System.out.println("Please return by " + returnDate + " at the latest.");
            } else {
                System.out.println("This book is currently not available.");
            }


        } else {
            System.out.println("No book by that name was found!");
        }

    }

    //DONE!
    public void returnBook () throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String nameOfBook = MENU.enterValue("name of book");
        String query = "SELECT * FROM books WHERE title LIKE '" + nameOfBook + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "bookId");

        if (bookFound) {
            System.out.println("Book(s) found:\n");
            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE title LIKE ? ");
            statement.setString(1, "'" + nameOfBook + "'");
            statement.executeQuery();
            ResultClass.setResult(sqlStatement, "library", "title", "'" + nameOfBook + "'");
            listAllFromTable();

            int bookId = MENU.enterInteger("book id", 1, 999999);
            bookId = ResultClass.idMatches("books", bookId, "BookId");
            boolean available = ResultClass.checkAvailability(sqlStatement,"Availability",nameOfBook, bookId);
            boolean returned = ResultClass.checkReturned(sqlStatement,"RegisteredReturnDate",bookId);

            if (!available && !returned) {
                PreparedStatement returnBook = sqlStatement.getConnection().prepareStatement("UPDATE borrowedBooks SET RegisteredReturnDate = ? WHERE bookId = ? AND RegisteredReturnDate IS NULL;");
                returnBook.setDate(1, (java.sql.Date) today);
                returnBook.setInt(2, bookId);
                returnBook.executeUpdate();

                PreparedStatement setAvailability = sqlStatement.getConnection().prepareStatement("UPDATE books SET Availability = TRUE WHERE BookId = ?;");
                setAvailability.setInt(1, bookId);
                setAvailability.executeUpdate();

                System.out.println(nameOfBook + " was successfully returned!");
            } else {
                System.out.println("This book has already been returned!");
            }

        } else {
            System.out.println("No book by that name was found!");
        }

    }

    //DONE!
    public void newAuthor () throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String authorsName = MENU.enterValue("authors name");
        String query = "SELECT * FROM authors WHERE author LIKE '" + authorsName + "';";
        boolean authorFound = ResultClass.ifInputMatches(sqlStatement, query, "authorId");

        if (!authorFound) {
            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO authors (author) VALUES( ? );");
            statement.setString(1, authorsName);
            statement.executeUpdate();
            System.out.println(authorsName + " was successfully registered");
        } else {
            System.out.println(authorsName + " is already registered in the Cobalt Reserve.");
        }
    }

    //DONE!
    public void newBook () throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String authorsName = MENU.enterValue("authors name");
        String query = "SELECT * FROM authors WHERE author LIKE '" + authorsName + "';";
        boolean authorFound = ResultClass.ifInputMatches(sqlStatement, query, "authorId");

        if (authorFound) {
            int authorId = ResultClass.getAuthor(sqlStatement,authorsName);
            String title = MENU.enterValue("title of book");
            String isbn = MENU.enterValue("ISBN");
            int release = MENU.enterInteger("year of release", 0, year);

            PreparedStatement insertToBooks = sqlStatement.getConnection().prepareStatement("INSERT INTO books (title, isbn,year) VALUE (?,?,?);");
            insertToBooks.setString(1, title);
            insertToBooks.setString(2, isbn);
            insertToBooks.setInt(3, release);
            insertToBooks.executeUpdate();

            PreparedStatement insertToBookAuthor = sqlStatement.getConnection().prepareStatement("INSERT INTO bookAuthor (authorId) VALUES (?)");
            insertToBookAuthor.setInt(1, authorId);
            insertToBookAuthor.executeUpdate();

            System.out.println(title + " was successfully registered!");
        } else {
            System.out.println("""
                    There is no author by that name present in The Cobalt Reserve!
                     please register author before registering a new book.
                    """);
        }
    }

    //DONE!
    public void newMember () throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String firstName = MENU.enterValue("first name");
        String lastName = MENU.enterValue("last name");

        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("INSERT INTO members (firstName, lastName) VALUES( ? , ? );");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.executeUpdate();

        System.out.println(firstName + " " + lastName + " was successfully registered");
    }

    //DONE!
    public void searchAuthor() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String authorsName = MENU.enterValue("authors name");
        String query = "SELECT * FROM authors WHERE author LIKE '" + authorsName + "';";
        boolean authorFound = ResultClass.ifInputMatches(sqlStatement, query, "authorId");

        if (authorFound) {
            System.out.println("\nAuthors work:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE author = ? ");
            statement.setString(1, "'" + authorsName + "'");
            statement.executeQuery();

            ResultClass.setResult(sqlStatement, "library", "author", "'" + authorsName + "'");
            listAllFromTable();
        } else {
            System.out.println("No author by that name was found!");
        }
    }

    //DONE!
    public void searchBook() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String nameOfBook = MENU.enterValue("name of book");
        String query = "SELECT * FROM books WHERE title LIKE '" + nameOfBook + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "bookId");

        if (bookFound) {
            System.out.println("\nBook found:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE title = ? ");
            statement.setString(1, "'" + nameOfBook + "'");

            statement.executeQuery();

            ResultClass.setResult(sqlStatement, "library", "title", "'" + nameOfBook + "'");
            listAllFromTable();
        } else {
            System.out.println("No book by that name was found!");
        }
    }

    //DONE!
    public void searchISBN() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String isbn = MENU.enterValue("isbn of book");
        String query = "SELECT * FROM books WHERE isbn LIKE " + isbn + ";";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "bookId");

        if (bookFound) {
            System.out.println("\nBook found:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE isbn = ? ");
            statement.executeQuery();

            ResultClass.setResult(sqlStatement, "library", "isbn", isbn);
            listAllFromTable();
        } else {
            System.out.println("No book with that ISBN was found!");
        }
    }

    //DONE!
    public void searchMember() throws SQLException{
        sqlStatement = Run.getSqlStatement();
        String firstName = MENU.enterValue("first name");
        String lastName = MENU.enterValue("last name");
        String query = "SELECT * FROM members WHERE FirstName = '" + firstName + "' AND LastName = '" + lastName + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "memberId");

        if (bookFound) {
            System.out.println("\nMember found:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM members WHERE firstname = '" + firstName + "' AND lastname = '" + lastName + "';");
            statement.executeQuery();

            ResultClass.setResult(sqlStatement, "members", "firstName", "'" + firstName + "'");
            listAllFromTable();
        } else {
            System.out.println("No member by that name was found!");
        }
    }

    //DONE!
    public void listAllBooks() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library;");
        statement.executeQuery();

        ResultClass.setAll(sqlStatement,"library");
        System.out.println();
        listAllFromTable();
    }

    //DONE!
    public void listAllAuthors() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM authors;");
        statement.executeQuery();

        ResultClass.setAll(sqlStatement,"authors");
        System.out.println();
        listAllFromTable();
    }

    //DONE!
    public void removeAuthor() throws SQLException {
        sqlStatement = Run.getSqlStatement();
        String authorsName = MENU.enterValue("authors name");
        String query = "SELECT * FROM authors WHERE author LIKE '" + authorsName + "';";
        boolean authorFound = ResultClass.ifInputMatches(sqlStatement, query, "authorId");

        if (authorFound) {
            System.out.println("Authors work:\n");
            PreparedStatement authorsWork = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE author = ? ");
            authorsWork.setString(1, "'" + authorsName + "'");
            authorsWork.executeQuery();
            ResultClass.setResult(sqlStatement, "library", "author", "'" + authorsName + "'");
            listAllFromTable();

            System.out.println();

            PreparedStatement showAuthorId = sqlStatement.getConnection().prepareStatement("SELECT * FROM authors WHERE author = ?");
            showAuthorId.setString(1,"'" + authorsName + "'");
            showAuthorId.executeQuery();
            ResultClass.setResult(sqlStatement, "authors", "author", "'" + authorsName + "'" );
            listAllFromTable();


            if (MENU.confirm("Do you want to remove this author?")){
                int authorId = MENU.enterInteger("Author Id", 1, 999);
                authorId = ResultClass.idMatches("authors", authorId, "authorId");

                PreparedStatement removeAuthor = sqlStatement.getConnection().prepareStatement("DELETE FROM authors WHERE authorId = ?");
                removeAuthor.setInt(1, authorId);
                removeAuthor.executeUpdate();

                System.out.println(authorsName + " was successfully removed.");
            } else {
                System.out.println("Removal process was cancelled!");
            }
        } else {
            System.out.println("There is no author by that name present in The Cobalt Reserve!\n");
        }
    }

    //DONE!
    public void removeBook() throws SQLException{
        sqlStatement = Run.getSqlStatement();
        String nameOfBook = MENU.enterValue("name of book");
        String query = "SELECT * FROM books WHERE title LIKE '" + nameOfBook + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "bookId");

        if (bookFound) {
            System.out.println("\nBook found:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM library WHERE title = ? ");
            statement.setString(1, "'" + nameOfBook + "'");
            statement.executeQuery();
            ResultClass.setResult(sqlStatement, "library", "title", "'" + nameOfBook + "'");
            listAllFromTable();

            if (MENU.confirm("Do you want to remove this book?")) {
                int bookId = MENU.enterInteger("Book Id", 1, 999);
                bookId = ResultClass.idMatches("books", bookId, "bookId");

                PreparedStatement removeBooks = sqlStatement.getConnection().prepareStatement("DELETE FROM books WHERE bookId = ?");
                removeBooks.setInt(1, bookId);
                removeBooks.executeUpdate();

                PreparedStatement removeBookAuthor = sqlStatement.getConnection().prepareStatement("DELETE FROM bookAuthor WHERE bookId = ?");
                removeBookAuthor.setInt(1,bookId);
                removeBookAuthor.executeUpdate();

                System.out.println(nameOfBook + " was successfully removed.");
            } else {
                System.out.println("Removal process was cancelled!");
            }

        } else {
            System.out.println("No book by that name was found!");
        }
    }

    //DONE!
    public void removeMember() throws SQLException{
        sqlStatement = Run.getSqlStatement();
        String firstName = MENU.enterValue("first name");
        String lastName = MENU.enterValue("last name");
        String query = "SELECT * FROM members WHERE FirstName = '" + firstName + "' AND LastName = '" + lastName + "';";
        boolean bookFound = ResultClass.ifInputMatches(sqlStatement, query, "memberId");

        if (bookFound) {
            System.out.println("\nMember found:");

            PreparedStatement statement = sqlStatement.getConnection().prepareStatement("SELECT * FROM members WHERE firstname = '" + firstName + "' AND lastname = '" + lastName + "';");
            statement.executeQuery();
            ResultClass.setResult(sqlStatement, "members", "firstName", "'" + firstName + "'");
            listAllFromTable();
            if (MENU.confirm("Do you want to remove this member?")) {
                int memberId = MENU.enterInteger("Member Id", 1, 999);
                memberId = ResultClass.idMatches("members", memberId, "memberId");

                PreparedStatement removeBooks = sqlStatement.getConnection().prepareStatement("DELETE FROM members WHERE memberId = ?");
                removeBooks.setInt(1, memberId);
                removeBooks.executeUpdate();

                System.out.println("Id: " + memberId + ". " + firstName + " " + lastName + " was successfully removed.");
            } else {
                System.out.println("Removal process was cancelled!");
            }
        } else {
            System.out.println("No member by that name was found!");
        }
    }

    //DONE!
    public void listAllFromTable() throws SQLException {
        ResultSet result = ResultClass.getResult();

        int columnCount = result.getMetaData().getColumnCount();

        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = result.getMetaData().getColumnName(i + 1);
        }

        for (String columnName : columnNames) {
            System.out.print(Menu.PadRight(columnName));
        }

        while (result.next()) {
            System.out.println();
            for (String columnName : columnNames) {
                String value = result.getString(columnName);

                if (value == null)
                    value = "null";

                System.out.print(Menu.PadRight(value));
            }
        }
        System.out.println();
    }

    //DONE!
    public void setup() throws SQLException {
        sqlStatement = Run.getSqlStatement();

        //#region Creating database
        String useDatabase = "USE cobalt_reserve;";
        String createTableBooks = "CREATE TABLE books (BookId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,Title VARCHAR(250) NOT NULL,ISBN VARCHAR(13) NOT NULL, Year INT, Availability BOOLEAN DEFAULT TRUE);";
        String createTableAuthors = "CREATE TABLE authors (AuthorId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,Author VARCHAR(100));";
        String createTableBookAuthor = "CREATE TABLE bookAuthor (BookId INT NOT NULL PRIMARY KEY auto_increment,AuthorId INT NOT NULL);";
        String createTableMembers = "CREATE TABLE members (MemberId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,FirstName VARCHAR(50) NOT NULL,LastName VARCHAR(50) NOT NULL);";
        String createTableBorrowedBooks = "CREATE TABLE borrowedBooks (BookId INT NOT NULL, MemberId INT NOT NULL, LoanDate DATE NOT NULL, ReturnDate DATE NOT NULL, RegisteredReturnDate DATE);";
        String createViewLibrary = "CREATE VIEW Library AS SELECT books.bookId AS Id, books.title AS Title, authors.author AS Author, books.isbn AS ISBN, books.year AS Year, IF(books.availability, 'Available', 'Borrowed') Availability FROM bookAuthor LEFT JOIN books ON books.bookId = bookAuthor.bookId LEFT JOIN authors ON authors.authorId = bookAuthor.authorId;";
        //#endregion

        //#region Inserting Authors
        String createJRR = "INSERT INTO authors (author) VALUES ('J.R.R. Tolkien');";
        String createRA = "INSERT INTO authors (author) VALUES ('R.A. Salvatore');";
        //#endregion

        //region Inserting Books
        String createFellowship = "INSERT INTO books (title, isbn, year) VALUES ('Fellowship Of The Ring', '9780008376123', 1954);";
        String createTwoTowers = "INSERT INTO books (title, isbn, year) VALUES ('The Two Towers', '9780008376130', 1954);";
        String createReturnKing = "INSERT INTO books (title, isbn, year) VALUES ('The Return of the King', '9780008376147', 1955);";
        String createHomeland = "INSERT INTO books (title, isbn, year) VALUES ('Homeland', '9781932796407', 1990);";
        String createExile = "INSERT INTO books (title, isbn, year) VALUES ('Exile', '9781932796469', 1990);";
        String createSojourn = "INSERT INTO books (title, isbn, year) VALUES ('Sojourn', '9781932796575', 1991);";
        //endregion

        //region Inserting BookAuthor
        String ett = "INSERT INTO bookAuthor (authorId) VALUES (1)";
        String tva = "INSERT INTO bookAuthor (authorId) VALUES (1)";
        String tre = "INSERT INTO bookAuthor (authorId) VALUES (1)";
        String fyr = "INSERT INTO bookAuthor (authorId) VALUES (2);";
        String fem = "INSERT INTO bookAuthor (authorId) VALUES (2);";
        String sex = "INSERT INTO bookAuthor (authorId) VALUES (2);";
        //endregion

        //#region Sending queries to mySql
        ResultClass.setupResult(sqlStatement, useDatabase);
        ResultClass.setupResult(sqlStatement, createTableBooks);
        ResultClass.setupResult(sqlStatement, createTableAuthors);
        ResultClass.setupResult(sqlStatement, createTableBookAuthor);
        ResultClass.setupResult(sqlStatement, createTableMembers);
        ResultClass.setupResult(sqlStatement,createTableBorrowedBooks);
        ResultClass.setupResult(sqlStatement, createViewLibrary);
        ResultClass.setupResult(sqlStatement, createJRR);
        ResultClass.setupResult(sqlStatement, createRA);
        ResultClass.setupResult(sqlStatement, createFellowship);
        ResultClass.setupResult(sqlStatement, createTwoTowers);
        ResultClass.setupResult(sqlStatement, createReturnKing);
        ResultClass.setupResult(sqlStatement, createHomeland);
        ResultClass.setupResult(sqlStatement, createExile);
        ResultClass.setupResult(sqlStatement, createSojourn);
        ResultClass.setupResult(sqlStatement, ett);
        ResultClass.setupResult(sqlStatement, tva);
        ResultClass.setupResult(sqlStatement, tre);
        ResultClass.setupResult(sqlStatement, fyr);
        ResultClass.setupResult(sqlStatement, fem);
        ResultClass.setupResult(sqlStatement, sex);
        //#endregion

        System.out.println("""

                --- First Time Setup Complete! ---
                Cobalt Reserve has been filled with default content!
                """);
    }

}