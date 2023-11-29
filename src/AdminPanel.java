import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.proxy.annotation.Pre;
import oracle.sql.*;
public class AdminPanel {

    private static OracleConnection conx;

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {

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
            System.out.println();
            System.out.println("1. Add a single product");
            System.out.println("2. Delete a product by ProductID");

            System.out.println("3. Show ALL products");

            //under amending
            System.out.println("4. Show details of one product by ProductID");
            //under amending
            System.out.println("5. Check the details of a parcel by ParcelID");
            //under construction
            System.out.println("6. Analyze");

            //Completed
            System.out.println("7. Manually add new user");

            System.out.println("\nEnter -1 to exit\n");
            System.out.print("Please enter the number option: ");

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
                case "4": //要修
                    searchByID();
                    break;
                case "5": //也要修
                    showAParcel();
                    break;
                case "6"://待实现

                    break;
                case "7":
                    addUser();
                    break;
                case "-1":
                    System.out.println("Exiting admin panel...");
                    System.exit(1);
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
    static void addItem() throws SQLException, IOException, InterruptedException {
        clearScreen();
        Console console = System.console();

        System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");
        int productID = Integer.parseInt(console.readLine());
        System.out.print("Enter ProductName: ");
        String productName = console.readLine();
        System.out.print("Enter Price(4 digits integer only): ");
        int  price; //= Integer.parseInt(console.readLine());
        try{
            price = Integer.parseInt(console.readLine());
        }catch (Exception NumberFormatException){
            System.out.print("Invalid number input! Please input again: ");
            price = Integer.parseInt(console.readLine());
        }

        System.out.print("Enter Specification: ");
        String specification = console.readLine();
        System.out.print("Enter Description: ");
        String descript = console.readLine();
        System.out.print("Enter SellerID(Existing Sellers only![4 numbers only, begin with 3XXX]): ");
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
        System.out.println("Product Added Successfully!");
    }
    static void deleteItem() throws SQLException, IOException, InterruptedException {
        Console console = System.console();
        System.out.print("Enter ProductID (4 numbers only, begin with 1XXX): ");
        int productID = Integer.parseInt(console.readLine());

        String deleteQuery = "DELETE FROM PRODUCT WHERE ProductID=?";
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
        clearScreen();
        Console console= System.console();
        System.out.print("Input the product ID: ");
        int productID = Integer.parseInt(console.readLine());
        Statement st1 = conx.createStatement();

//        clearScreen();

        ResultSet productList = st1.executeQuery("SELECT * FROM PRODUCT WHERE ProductID="+productID);
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getString(1), productList.getString(2),
                    productList.getString(3), productList.getString(4));
        }
        st1.close(); // .close = commit
    }
    static void showAParcel() throws SQLException, IOException, InterruptedException {
        clearScreen();
        Console console = System.console();
        System.out.print("Enter ParcelID: ");
        int parID = Integer.parseInt(console.readLine());
        String locQ="SELECT * FROM PARCEL WHERE ParcelID=?";
        PreparedStatement ps=conx.prepareStatement(locQ);
        ps.setInt(1,parID);
        ps.executeQuery();
        ps.close();
    }

    static void showAllItem() throws SQLException, IOException, InterruptedException {
        clearScreen();
        System.out.println("\nProducts in stock are shown below: ");
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

    static void addUser() throws SQLException, IOException, InterruptedException {
        clearScreen();
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
