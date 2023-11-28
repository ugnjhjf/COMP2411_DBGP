import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class AdminPanel {

    private static OracleConnection conx;

    public static void main(String[] args) throws SQLException {

        Console console = System.console();
        System.out.print("Enter your DBMS username: ");    // Your Oracle ID with double quote
        String username = console.readLine();         // e.g. "98765432d"
        System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account
        char[] password = console.readPassword();
        String pwd = String.valueOf(password);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn =
                (OracleConnection)DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",username,pwd);
        conx = conn;

        boolean inputStatus = true;
        while(inputStatus) {
            System.out.println("Enter admin panel");
            System.out.println("");
            System.out.println();
            System.out.println("1. Add item");
            System.out.println("2. Delete item");

            System.out.println("3. Show ALL items");
            System.out.println("4. Show item detail");

            System.out.println("5. Check parcel detail");
            System.out.println("6. Analyze");

            System.out.println("7. Add user");

            String option = console.readLine();
            switch (option)
            {
                case "1":
                    addItem();
                    break;
                case "2":
                    break;
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                    addUser();
                    break;
            }
        }
    }
    static void addItem() throws SQLException {
        Console console = System.console();

        System.out.print("Enter ProductID: ");
        int productID = Integer.parseInt(console.readLine());
        System.out.print("Enter ProductName: ");
        String productName = console.readLine();
        System.out.println("Enter Price: ");
        int  price = Integer.parseInt(console.readLine());
        System.out.println("Enter Specification: ");
        String specification = console.readLine();
        System.out.println("Enter Descript: ");
        String descript = console.readLine();
        System.out.println("Enter SellerID: ");
        int sellerID = Integer.parseInt(console.readLine());

//        CREATE TABLE PRODUCT
//                (
//                        ProductID    NUMBER(4) PRIMARY KEY,
//                        ProductName  VARCHAR(50),
//                        Price Number(4),
//                        Specification  VARCHAR(50),
//                        Description  VARCHAR(50),
//                        SellerID NUMBER(4),
//                        CONSTRAINT FK_PRODUCT_SELLER
//                        FOREIGN KEY (SellerID)
//                        REFERENCES SELLER(SellerID)

        String insertQuery = "INSERT INTO PRODUCT(ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
        preparedStatement.setInt(1, productID);
        preparedStatement.setString(2, productName);
        preparedStatement.setInt(3, price);
        preparedStatement.setString(4, specification);
        preparedStatement.setString(5, descript);
        preparedStatement.setInt(6, sellerID);


        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    static void addUser() throws SQLException {
        Console console = System.console();

        System.out.print("Enter UserID: ");
        int userID = Integer.parseInt(console.readLine());
        System.out.print("Enter Username: ");
        String userName = console.readLine();
        System.out.print("Enter password: ");
        String passWord = console.readLine();

        String insertQuery = "INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
        preparedStatement.setInt(1, userID);
        preparedStatement.setString(2, userName);
        preparedStatement.setString(3, passWord);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
