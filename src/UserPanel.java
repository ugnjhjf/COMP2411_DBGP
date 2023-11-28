import  java.util.*;
import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class UserPanel {
    private static OracleConnection conx;
    private static int userID;
    private static boolean loginStatus = false;

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Console console = System.console();
        System.out.print("Enter your DBMS username: ");    // Your Oracle ID with double quote
        String username = console.readLine();         // e.g. "98765432d"
        System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account

        char[] password = console.readPassword();
        String pwd = String.valueOf(password);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn =
                (OracleConnection) DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", username, pwd);


        conx = conn;
        clearScreen();
        System.out.println("Database Login successfully");
        System.out.println(" ");


        while (!(loginStatus)) { //Login user account (OSS)
            System.out.println("Please login your account to continue - Metaverse Online Shopping System");
            System.out.print("Input your username: ");
            String InputUserName = console.readLine();
            System.out.print("Input your password: ");
            String InputUserPwd = console.readLine();
            loginStatus=loginCheck(InputUserName,InputUserPwd);
        }

        clearScreen();
        System.out.println("Login successfully! Metaverse Online Shopping System");
        System.out.println(" ");


        boolean inputJ = true;
        while (inputJ) {
            System.out.println("1. Search item");
            System.out.println("2. Inspect shopping cart");
            System.out.println("3. Check parcel status");
            System.out.println("4. Account settings");
            System.out.println("Enter -1 to exit");
            System.out.print("Input the number option: ");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    searchItem();
                    break;
                case 2:
                    shoppingCart();
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                default:
                    inputJ = false;
                    System.out.println("Good Bye");
                    conx.close();
            }


        }
    }

    static void clearScreen() throws IOException, InterruptedException {
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");
    }

    static boolean loginCheck(String userName, String userPwd) throws SQLException {
        String sql = "select * from CUSTOMER where Username = '" + userName + "' and Password = '" + userPwd + "'";
        try {
            ResultSet rs = conx.createStatement().executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Invalid username or password");
            return false;
        }

        ResultSet rs = conx.createStatement().executeQuery(sql);
        if(rs.next()) {
            userID = rs.getInt(1);
            System.out.println("Login Success");
            return true;
        }
        return false;
    }

    public static void searchItem() throws SQLException, IOException, InterruptedException {
        System.out.println("");
        System.out.println("1. Search by product name");
        System.out.println("2. Search by product ID");
        System.out.println("3. List all products");
        System.out.println("4. Add product");
        System.out.print("Input the number option: ");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1:
                searchByName();
                break;
            case 2:
                searchByID();
                break;
            case 3:
                listAllProduct();
                break;
            case 4:
                addProduct();
            case 5:
            case 6:
            default:
                break;
        }
    }

    public static void shoppingCart() throws SQLException, IOException, InterruptedException{
        System.out.println("4. Add product");
//        case 4:
        addProduct();

    }

    public static void searchByName() throws SQLException, IOException, InterruptedException {
        Console console= System.console();
        System.out.print("Input the product name: ");
        String productName = console.readLine();

        ResultSet productList;
        Statement st1 = conx.createStatement();

        clearScreen();

        productList = st1.executeQuery("SELECT * FROM PRODUCT where ProductName = '" + productName + "'");
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getString(1), productList.getString(2),
                    productList.getString(3), productList.getString(4));
        }
        st1.close(); // .close = commit
    }
    public static void searchByID() throws SQLException, IOException, InterruptedException {
        Console console= System.console();
        System.out.print("Input the product id: ");
        int productID = Integer.parseInt(console.readLine());

        ResultSet productList;
        Statement st1 = conx.createStatement();

        clearScreen();

        productList = st1.executeQuery("SELECT * FROM PRODUCT where ProductID="+ productID );
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getString(1), productList.getString(2),
                    productList.getString(3), productList.getString(4));
        }
        st1.close(); // .close = commit
    }

    public static void listAllProduct() throws SQLException, IOException, InterruptedException {

        ResultSet productList;
        Statement st1 = conx.createStatement();

        clearScreen();

        productList = st1.executeQuery("SELECT * FROM PRODUCT");
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getString(1), productList.getString(2),
                    productList.getString(3), productList.getString(4));
        }
        st1.close(); // .close = commit
    }

    static void addProduct() throws SQLException {
        Console console = System.console();

        System.out.print("Enter ProductID: ");
        int productID = Integer.parseInt(console.readLine());

        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(console.readLine());
        String insertQuery = "INSERT INTO CART(UserID,ProductID,Quantity) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
        preparedStatement.setInt(1, UserPanel.userID);
        preparedStatement.setInt(2, productID);
        preparedStatement.setInt(3, quantity);


        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Product Added!");
    }

}