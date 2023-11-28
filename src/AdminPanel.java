import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.proxy.annotation.Pre;
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
            System.out.println("\nEntered admin panel");
            System.out.println("");
            System.out.println();
            System.out.println("1. Add single product");
            System.out.println("2. Delete product (By id)");

            System.out.println("3. Show ALL products");
            System.out.println("4. Show product details");

            System.out.println("5. Check parcel details");
            System.out.println("6. Analyze");

            System.out.println("7. Add user");

            String option = console.readLine();
            switch (option)
            {
                case "1":
                    addItem();
                    break;
                case "2":
                    deleteItem();
                    break;
                case "3":
                    showAllItem();
                    break;
                case "4":
                    try {
                        searchByID();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "5":
                    showAParcel();
                    break;
                case "6":

                    break;
                case "7":
                    addUser();
                    break;
                default:
                    inputStatus=false;
                    break;
            }
        }
    }
    static void clearScreen() throws IOException, InterruptedException
    {
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");
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
        System.out.println("Enter Description: ");
        String descript = console.readLine();
        System.out.println("Enter SellerID: ");
        int sellerID = Integer.parseInt(console.readLine());

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
        System.out.println("Product Added!");
    }
    static void deleteItem() throws SQLException {
        Console console = System.console();
        System.out.print("Enter ProductID: ");
        int productID = Integer.parseInt(console.readLine());

        String deleteQuery = "DELETE FROM PRODUCT WHERE ProductID=";

        PreparedStatement preparedStatement = conx.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, productID);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Product Deleted!");
    }

   /* static void showAProduct() throws SQLException {
        Console console = System.console();
        System.out.print("Enter ProductID: ");
        int productID = Integer.parseInt(console.readLine());
        String locQ="SELECT * FROM PRODUCT WHERE ProductID=?";
        PreparedStatement ps=conx.prepareStatement(locQ);
        ps.executeQuery(locQ);
        ps.setInt(1,productID);
        ps.close();
    }*/
    public static void searchByID() throws SQLException, IOException, InterruptedException {
        Console console= System.console();
        System.out.print("Input the product ID: ");
        String productID = console.readLine();
        Statement st1 = conx.createStatement();

        clearScreen();

        ResultSet productList = st1.executeQuery("SELECT * FROM PRODUCT WHERE ProductID="+productID);
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getString(1), productList.getString(2),
                    productList.getString(3), productList.getString(4));
        }
        st1.close(); // .close = commit
    }
    static void showAParcel() throws SQLException {
        Console console = System.console();
        System.out.print("Enter ParcelID: ");
        int parID = Integer.parseInt(console.readLine());
        String locQ="SELECT * FROM PARCEL WHERE ParcelID=?";
        PreparedStatement ps=conx.prepareStatement(locQ);
        ps.setInt(1,parID);
        ps.executeQuery();
        ps.close();
    }

    static void showAllItem() throws SQLException {
        Statement st1;
        st1 = conx.createStatement();
        ResultSet rs;
        rs = st1.executeQuery("SELECT * FROM PRODUCT");
        while (rs.next()){
            System.out.println(rs.getString(1)
                    + " " + rs.getString(2)+
                    " " + rs.getString(3)+
                    " "+rs.getString(4)+
                    " "+rs.getString(5)+
                    " "+rs.getString(6));
        }
        st1.close();

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
        System.out.println("User Added!");
    }
}
