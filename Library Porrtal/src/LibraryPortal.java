import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class LibraryPortal {
    LinkedList<Book> bookList=new LinkedList();
    LinkedList<readedBookHistory> readedbookList=new LinkedList();
    
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        LibraryPortal portal = new LibraryPortal();
        Admin admin = new Admin("yogesh", "yogesh@123");
        String dburl = "jdbc:mysql://localhost:3306/libraryDB";
        String dbuser = "root";
        String dbpassword = "";
        String driverName = "com.mysql.cj.jdbc.Driver"; // Updated driver name
        Class.forName(driverName);
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);
        System.out.println((con != null) ? "connection done" : "connection failed");
        portal.setdata(con);

        System.out.println("\n---WELCOME---");
        while (true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    portal.adminLogin(sc, con);
                    break;
                case 2:
                    portal.userLogin(sc, con);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return; // Exit the while loop and terminate the program
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
    
    void setdata(Connection con) {
        try {
            String sql = "SELECT * FROM Books";
            PreparedStatement ptmt = con.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            while (rs.next()) {
                int i=rs.getInt(1);
                String t=rs.getString(2);
                String p=rs.getString(4);
                String a=rs.getString(3);
                int y=rs.getInt(5);
                String is=rs.getString(6);
                Book b = new Book(i,t,a,p,y,is);
                bookList.addLast(b);
            }
            String sql1 = "SELECT * FROM readed_BookHistory";
            PreparedStatement ptmt1 = con.prepareStatement(sql1);
            ResultSet rs1 = ptmt1.executeQuery();
            while (rs1.next()) {
                int i=rs1.getInt(1);
                String t=rs1.getString(2);
                readedBookHistory rbh = new readedBookHistory(i,t);
                readedbookList.addFirst(rbh);
            }
            
        } catch (Exception e) {
            System.out.println("error in data setup "+e.getMessage());
        }
    }
    
    void adminLogin(Scanner sc, Connection con) {
        
            System.out.print("Enter admin username: ");
            String enteredUsername = sc.next();
            System.out.print("Enter admin password: ");
            String enteredPassword = sc.next();
            if (enteredUsername.equals(Admin.name) && enteredPassword.equals(Admin.password)) {
                System.out.println("Admin login successful!");
                    while (true) {
                    System.out.println("\nAdmin Menu:");
                    System.out.println("1. Add Book");
                    System.out.println("2. Add User");
                    System.out.println("3. View Book");
                    System.out.println("4. Delete Book");
                    System.out.println("5. Logout");
                    System.out.print("Choose an option: ");
                    int choice = sc.nextInt();
                    sc.nextLine(); // Consume newline
    
                    switch (choice) {
                    case 1:
                        addBook(sc, con);
                            break;
                    case 2:
                        addUser(sc, con);
                        break;
                    case 3: 
                        viewBooks(con);
                        break;
                    case 4: 
                        deleteBooks(sc,con);
                        break;
                    case 5:
                        return; // Logout and return to main menu
                    default:
                        System.out.println("Invalid choice! Try again.");
                    }
                }
            } else {
                System.out.println("Invalid Admin DATA.");
            }                                   
    }
        
    void userLogin(Scanner sc, Connection con){
        try {
            System.out.print("Enter User Email: ");
            String email = sc.nextLine();

            String sql = "SELECT * FROM Users WHERE Email = ? AND Role = 'User'";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, email);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("User login successful!");
                        while (true) {
                            System.out.println("\nUser Menu:");
                            System.out.println("1. View Available Books");
                            System.out.println("2. Read Book");
                            System.out.println("3. Borrow Book");
                            System.out.println("4. Return Book");
                            System.out.println("5. View Readed Book History");
                            System.out.println("6. Logout");
                            System.out.print("Choose an option: ");
                            int choice = sc.nextInt();
                            sc.nextLine(); // Consume newline

                            switch (choice) {
                                case 1:
                                    viewBooks(con);
                                    break;
                                case 2:
                                    readBook(sc,con,rs.getInt(1));
                                    break;
                                case 3:                
                                    borrowBook(sc,con);
                                    break;
                                case 4:                
                                    returnBook(sc,con);
                                    break;
                                case 5:
                                    history(rs.getInt(1));
                                    break;
                                case 6:
                                    return; // Logout and return to main menu
                                default:
                                    System.out.println("Invalid choice! Try again.");
                            }
                        }
                    } else {
                        System.out.println("Invalid User credentials.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during user login: " + e.getMessage());
        }
    }

    void addBook(Scanner sc, Connection con) {
        try {
            System.out.print("Enter Book Title: ");
            String title = sc.nextLine();
            System.out.print("Enter Book Author: ");
            String author = sc.nextLine();
            System.out.print("Enter Book Publisher: ");
            String publisher = sc.nextLine();
            System.out.print("Enter Book Year: ");
            int year = sc.nextInt();
            sc.nextLine(); // Consume newline
            System.out.print("Enter Book ISBN: ");
            String isbn = sc.nextLine();

            String sql = "INSERT INTO Books (Title, Author, Publisher, Year, ISBN) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setString(3, publisher);
                pstmt.setInt(4, year);
                pstmt.setString(5, isbn);
                pstmt.executeUpdate();
                System.out.println("Book added successfully!");
                int i=0;
                for (Book book : bookList) {
                    i=book.getBookID();
                }
                bookList.add(new Book((i+1), title, author, publisher, year, isbn));
            }
        } catch (SQLException e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }

    void addUser(Scanner sc, Connection con) {
        try {
            System.out.print("Enter User Name: ");
            String name = sc.nextLine();
            System.out.print("Enter User Email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO Users (Name, Email, Role) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, "User");
                pstmt.executeUpdate();
                System.out.println("User added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    void viewBooks(Connection con) {
        for (Book book : bookList) {
            System.out.println("\nBook ID: " + book.getBookID());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Publisher: " + book.getPublisher());
            System.out.println("Year: " + book.getYear());
            System.out.println("ISBN: " + book.getIsbn());
        }
    }
  
    void deleteBooks(Scanner sc,Connection con){
        try {
            System.out.println("Enter book Id to delete");
            int di=sc.nextInt();
            Statement st=con.createStatement();
            String q="delete from borrow where BookId="+di+"";
            st.executeUpdate(q);
            String q1="delete from books where BookId="+di+"";
            st.executeUpdate(q1);
            for (Book book : bookList) {
                if(di==book.getBookID()){
                    bookList.remove(book);
                    return;
                }
            }
            System.out.println("Book Deleted Successfully");
            
        } catch (Exception e) {
            System.out.println("error in deleting book "+e.getMessage());
        }
    }

    void readBook(Scanner sc, Connection con,int id) {
        System.out.println("Enter Book id of Book");
        int readBookId = sc.nextInt();
        String q = "select Title from Books where BookId = ?";
        try (PreparedStatement pst = con.prepareStatement(q)) {
            pst.setInt(1, readBookId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String book = rs.getString(1);
                    String q1="insert into readed_BookHistory values("+id+",'"+book+"')";
                    Statement st=con.createStatement();
                    st.executeUpdate(q1);
                    readedbookList.addFirst(new readedBookHistory(id, book));
                    System.out.println("Book Readed");
                } else {
                    System.out.println("Book not found");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading book: " + e.getMessage());
        }
    }
 
    void borrowBook(Scanner sc,Connection con) {
        try {
            System.out.print("Enter book id: ");
            int borrowBookId = sc.nextInt();
            System.out.print("Enter user id: ");
            int borrowUserId = sc.nextInt();
            LocalDate date = LocalDate.now();
            Date borrowDate = Date.valueOf(date);                    
            Borrow borrow = new Borrow(0, borrowBookId, borrowUserId,borrowDate);
            String q = "INSERT INTO borrow(BookID,UserID,borrowDate,ReturnDate) VALUES(?, ?, ?, ?)";
    
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setInt(1, borrow.getBookID());
            pstmt.setInt(2, borrow.getUserID());
            pstmt.setDate(3, borrow.getborrowDate());
            pstmt.setDate(4, borrow.getReturnDate());
            pstmt.executeUpdate();
            System.out.println("Book Borrowed");
        } catch (Exception e) {
            System.out.println("error during borrowing book "+e.getMessage());
        }
    }

    void returnBook(Scanner sc,Connection con) {
        try {
            System.out.print("Enter book id: ");
            int returnBookId = sc.nextInt();
            System.out.print("Enter user id: ");
            int returnUserId = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter year");
            int year1=sc.nextInt();
            System.out.println("Enter month");
            int month=sc.nextInt();
            System.out.println("Enter day");
            int day=sc.nextInt();
            LocalDate rDate=LocalDate.of(year1, month, day);
            Date returnDate=Date.valueOf(rDate);
            String q="update borrows set ReturnDate=? where UserID=? and BookID=?";
    
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setDate(1, returnDate);
            pstmt.setInt(2, returnUserId);
            pstmt.setInt(3, returnBookId);
            pstmt.executeUpdate();
            System.out.println("Return Date Stored!!");
            
        } catch (Exception e) {
            System.out.println("error in returning book "+e.getMessage());
        }
    }

    void history(int id){
        try {
            System.out.println("\nReaded Book History:");
            boolean b=true;
            for (readedBookHistory rbh : readedbookList) {
                if(id==rbh.getUserID()){
                    System.out.println(rbh.getBookName());
                    b=false;
                }
            }
            if (b) {
                System.out.println("You haven't readed any book");
            }
            
        } catch (Exception e) {
            System.out.println("error in viewing history "+e.getMessage());
        }
    }

    static class Admin {
        static String name;
        static String password;
        Admin(String name, String password) {
            this.name=name;
            this.password=password;
        }
    }
}

class Book {
    int bookID;
    String title;
    String author;
    String publisher;
    int year;
    String isbn;

    Book(int bookID, String title, String author, String publisher, int year, String isbn) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
    }

    int getBookID() {
        return bookID;
    }

    void setBookID(int bookID) {
        this.bookID = bookID;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    String getPublisher() {
        return publisher;
    }

    void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    int getYear() {
        return year;
    }

    void setYear(int year) {
        this.year = year;
    }

    String getIsbn() {
        return isbn;
    }

    void setIsbn(String isbn) {
        this.isbn = isbn;
    }

}

class User {
    int userID;
    String name;
    String email;

    int getUserID() {
        return userID;
    }

    void setUserID(int userID) {
        this.userID = userID;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    User(int userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
    }
}

class Borrow {
    int borrowID;
    int bookID;
    int userID;
    Date borrowDate;
    Date returnDate;

    int getborrowID() {
        return borrowID;
    }

    void setborrowID(int borrowID) {
        this.borrowID = borrowID;
    }

    int getBookID() {
        return bookID;
    }

    void setBookID(int bookID) {
        this.bookID = bookID;
    }

    int getUserID() {
        return userID;
    }

    void setUserID(int userID) {
        this.userID = userID;
    }

    Date getborrowDate() {
        return borrowDate;
    }

    void setborrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    Date getReturnDate() {
        return returnDate;
    }

    void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    Borrow(int borrowID, int bookID, int userID, Date borrowDate) {
        this.borrowID = borrowID;
        this.bookID = bookID;
        this.userID = userID;
        this.borrowDate = borrowDate;
        this.returnDate = null; // Initially, ReturnDate is null
    }
}

class readedBookHistory{
    int UserID;
    String BookName;
    int getUserID() {
        return UserID;
    }
    void setUserID(int userID) {
        UserID = userID;
    }
    String getBookName() {
        return BookName;
    }
    void setBookName(String bookName) {
        BookName = bookName;
    }
    readedBookHistory(int UserID,String BookName){
        this.UserID=UserID;
        this.BookName=BookName;
    }
}
