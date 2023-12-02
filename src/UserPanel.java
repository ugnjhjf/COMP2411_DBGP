import  java.util.*;
import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class UserPanel {
    static OracleConnection conx;
    static int userID;
    private static boolean loginStatus = false;
    private static boolean loginOracle = false;
//    private static

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {


        Console console = System.console();
        OracleConnection conn = null;
        while (!(loginOracle)) { //Login Oracle
            try {
            System.out.print("Enter your DBMS username: ");    // Your Oracle ID with double quote
            String username = console.readLine();         // e.g. "98765432d"     String username = console.readLine();         // e.g. "98765432d"
            System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account

            char[] password = console.readPassword();
            String pwd = String.valueOf(password);
                DriverManager.registerDriver(new OracleDriver());
                conn = (OracleConnection) DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", username, pwd);
                loginOracle = true;
            } catch (Exception e) {
                System.out.println("Oracle login failed. Try again");
            }
        }

        conx = conn;
        clearScreen();
        System.out.println("Database Login successfully");
        System.out.println(" ");

        if (!(loginStatus)) { //Login user account (OSS)
            System.out.println("Please login your account to continue - Metaverse Online Shopping System");
            System.out.print("Input your username: ");
            String InputUserName = console.readLine();;
            System.out.print("Input your password: ");
            String InputUserPwd = console.readLine();
            loginStatus = loginCheck(InputUserName, InputUserPwd);
            while (!(loginStatus)){
                System.out.println("\nInvalid Username or password! Please Retry.");
                System.out.print("Input your username: ");
                InputUserName = console.readLine();
                System.out.print("Input your password: ");
                InputUserPwd = console.readLine();
                loginStatus = loginCheck(InputUserName, InputUserPwd);
            }

        }

        clearScreen();
        System.out.println("Login successfully! Metaverse Online Shopping System");
        System.out.println(" ");


        boolean inputJ = true;
        while (inputJ) {
            System.out.println("1. Search item");
            System.out.println("2. Inspect shopping cart");
            System.out.println("3. Check parcel status");
            System.out.println("4. Account settings\n");
            System.out.println("Enter -1 to exit\n");
            System.out.print("Input the number option: ");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    searchItem();
                    break;
                case 2:
                    ShoppingCart();
                    break;
                case 3:
                    checkParcel();
                    break;
                case 4:
                    accountManagement();
                    break;
                default:
                    inputJ = false;
                    System.out.println("Good Bye");
                    conx.close();
                    break;
            }
        }
    }

    public static void searchItem() throws SQLException, IOException, InterruptedException {
        System.out.println("\nEntered search item menu\n");
        System.out.println("1. Search by product name");
        System.out.println("2. Search by product ID");
        System.out.println("3. List all products");
        System.out.println("4. Add product\n");
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
                UserPanel.listAllProduct();
                break;
            case 4:
                UserPanel.addProduct();
                break;
            default:
                break;
        }
    }
    public static void ShoppingCart() throws SQLException, IOException, InterruptedException{
        System.out.println("");
        System.out.println("1. List all products in shopping cart");
        System.out.println("2. Add product");
        System.out.println("3. Delete product");
        System.out.println("4. Checkout");
        System.out.print("Input the number option: ");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1:
                listAllProductInCart();
                break;
            case 2:
                listAllProductInCart();
                UserPanel.addProduct();
                break;
            case 3:
                listAllProductInCart();
                deleteProductInCart();
                break;
            case 4:
                checkoutCart();
                break;
            default:
                break;
        }

    }


    public static void accountManagement() throws SQLException {

        System.out.println("");
        System.out.println("1. Change userName(Login name)");
        System.out.println("2. Change password");
        System.out.println("3. Change telephone number");
        System.out.println("4. Change shipping address");
        System.out.println("Input anything to upper menu.");
        System.out.print("Input the number option: ");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1:
                changeLoginName();
                break;
            case 2:
                changePassword();
                break;
            case 3:
                changeTel();
                break;
            case 4:
                changeAddress();
                break;
            default:
                break;
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


    public static void listAllProduct() throws SQLException, IOException, InterruptedException {
        clearScreen();
        System.out.println("\nProducts in stock are shown below: ");
        System.out.println("Products ID, Product Name, Product Price, Product specification, Product description, Seller ID");
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
                    " "+rs.getString(6)+
                    " "+rs.getString(7));
        }
        System.out.println();
        st1.close();

//        ResultSet productList;
//        Statement st1 = conx.createStatement();
//
//        clearScreen();
//
//        productList = st1.executeQuery("SELECT * FROM PRODUCT");
//
//        System.out.printf("%-12s %-20s %-10s %-20s %-20s%n", "Product ID", "Product Name", "Product Price", "Specification", "Description");
//        System.out.println("==============================================================================================");
//        while (productList.next()) {
//            System.out.printf( productList.getInt(1)+"   "+ productList.getString(2)+"   "+
//                    productList.getInt(3)+"   "+productList.getString(4)+"   "+ productList.getString(5));
//        }
//        st1.close();
    }
    public static void checkoutCart() {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 获取购物车中的商品信息
            String cartQuery = "SELECT c.ProductID, p.ProductName, c.Quantity, p.Price " +
                    "FROM CART c " +
                    "JOIN PRODUCT p ON c.ProductID = p.ProductID " +
                    "WHERE c.UserID = ?";
            stmt = conx.prepareStatement(cartQuery);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            List<CartItem> cartItems = new ArrayList<>();
            double totalCost = 0.0;

            // 输出购物车内容，计算总价格
            System.out.printf("%-12s %-20s %-10s %-10s%n", "Product ID", "Product Name", "Quantity", "Price");
            System.out.println("===============================================");
            while (rs.next()) {
                int productID = rs.getInt(1);
                String productName = rs.getString(2);
                int quantity = rs.getInt(3);
                int price = rs.getInt(4);

                totalCost += quantity * price;

                System.out.printf("%-12d %-20s %-10d %-10d %n", productID, productName, quantity, price);

                cartItems.add(new CartItem(productID, quantity));
            }

            // 输出总价格
            System.out.println("===============================================");
            System.out.println("Total Cost: " + totalCost);

            // 检查是否可以进行checkout
            boolean canCheckout = true;
            for (CartItem item : cartItems) {
                int availableQuantity = getAvailableQuantity(item.getProductID());

                if (availableQuantity < item.getQuantity()) {
                    canCheckout = false;
                    break;
                }
            }

            if (canCheckout) {
                // 复制购物车内容到Parcel表并生成ParcelID

                int parcelID = generateParcelID();
                String insertParcelQuery = "INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID,Shipping_address) VALUES (?, ?, ?, ?, ?)";
                stmt = conx.prepareStatement(insertParcelQuery);
                for (CartItem item : cartItems) {
                    stmt.setInt(1, parcelID);
                    stmt.setInt(2, item.getProductID());
                    stmt.setInt(3, item.getQuantity());
                    stmt.setInt(4, userID);
                    stmt.setString(5,getShippingAddress(userID));

                    stmt.addBatch();

                    parcelID++;
                }
                stmt.executeBatch();

                // 清空购物车
                String clearCartQuery = "DELETE FROM CART WHERE UserID = ?";
                stmt = conx.prepareStatement(clearCartQuery);
                stmt.setInt(1, userID);
                stmt.executeUpdate();

                System.out.println("Checkout successful. ParcelID: " + parcelID);
                stmt.close();
            } else {
                System.out.println("Insufficient stock. Cannot checkout.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private static int getAvailableQuantity(int productID) throws SQLException {
        try {
            String stockQuery = "SELECT Quantity FROM PRODUCT WHERE ProductID = ?";
            PreparedStatement stmt = conx.prepareStatement(stockQuery);
            stmt.setInt(1, productID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                // 商品ID不存在，返回一个表示无效库存的特殊值
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static int generateParcelID() throws SQLException {
        try {
            String parcelIDQuery = "SELECT COUNT(*) AS ParcelCount FROM PARCEL";
            Statement stmt = conx.createStatement();
            ResultSet rs = stmt.executeQuery(parcelIDQuery);
            if (rs.next()) {
                int count = rs.getInt("ParcelCount");
                return 5001 + count;
            }
        }
        catch(Exception e)
        {
            System.out.println("generate parcel ID have problem");
        }
        return 0;
    }

    private static class CartItem {
        private int productID;
        private int quantity;

        public CartItem(int productID, int quantity) {
            this.productID = productID;
            this.quantity = quantity;
        }

        public int getProductID() {
            return productID;
        }

        public int getQuantity() {
            return quantity;
        }


    }
    public static String getShippingAddress(int userID) throws SQLException {
        String address = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT Shipping_address FROM CUSTOMER WHERE UserID = ?";
            stmt = conx.prepareStatement(query);
            stmt.setInt(1, userID);
            rs = stmt.executeQuery();

            if (rs.next()) {
                address = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return address;
    }


    static void addProduct() throws SQLException {
        Console console = System.console();

        System.out.print("Enter ProductID(4 digits number begin with 0XXX): ");
        try {
            int productID = Integer.parseInt(console.readLine());

            System.out.print("Enter Quantity: ");
            int quantity = Integer.parseInt(console.readLine());
            String insertQuery = "INSERT INTO CART(UserID,ProductID,Quantity) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
            preparedStatement.setInt(1, UserPanel.userID);
            preparedStatement.setInt(2, productID);
            preparedStatement.setInt(3, quantity);
            System.out.println();

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Product Added!");
        }catch (Exception e)
        {
            System.out.println("Invalid input. Check the id exist");
        }
    }
    private static void checkParcel() throws SQLException, IOException, InterruptedException {

        clearScreen();
        try {
            Statement st1 = conx.createStatement();
            String checkParcel = "SELECT PRODUCT.productName,PARCEL.Quantity,PARCEL.Shipping_address FROM PRODUCT,PARCEL where UserID = ? AND PRODUCT.ProductID = PARCEL.ProductID";
            PreparedStatement checkParcelStatement = conx.prepareStatement(checkParcel);
            checkParcelStatement.setInt(1, UserPanel.userID);
            ResultSet parcelList = checkParcelStatement.executeQuery();
            System.out.printf("%-15s %-10s %-15s %n", "Product Name", "Quantity", "Shipping address");
            System.out.println("==============================================================================================");
            while (parcelList.next()) {
                System.out.printf("%-15s %-10s %-15s %n", parcelList.getString(1), parcelList.getInt(2),
                        parcelList.getString(3));
                st1.close(); // .close = commit
            }
        }
        catch (Exception e)
        {
            System.out.println("Something wrong in checkParcel(). Note to admin");
        }
    }

    private static void changeAddress() throws SQLException {
        Console console = System.console();

        System.out.print("Enter New Address: ");
        try {
            String shippingAddress = console.readLine();

            String insertQuery = "UPDATE CUSTOMER SET Shipping_address = ? WHERE UserID = ? ";

            PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
            preparedStatement.setString(1, shippingAddress);
            preparedStatement.setInt(2, UserPanel.userID);
            System.out.println();
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Address Update!");
        }catch (Exception e)
        {
            System.out.println("Invalid input. Check the input and try again.");
        }
    }

    private static void changeTel() {
        Console console = System.console();

        System.out.print("Enter telephone: ");
        try {
            String telphone = console.readLine();

            String insertQuery = "UPDATE CUSTOMER SET Tel = ? WHERE UserID = ? ";

            PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
            preparedStatement.setString(1, telphone);
            preparedStatement.setInt(2, UserPanel.userID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Telephone Update!");
        }catch (Exception e)
        {
            System.out.println("Invalid input. Check the input and try again.");
        }

    }

    private static void changePassword() {
        Console console = System.console();

        System.out.print("Enter New password: ");
        try {
            String pwd = console.readLine();

            String insertQuery = "UPDATE CUSTOMER SET Password = ? WHERE UserID = ? ";

            PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
            preparedStatement.setString(1, pwd);
            preparedStatement.setInt(2, UserPanel.userID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Password Update!");
        }catch (Exception e)
        {
            System.out.println("Invalid input. Check the input and try again.");
        }
    }

    private static void changeLoginName() {
        Console console = System.console();

        System.out.print("Enter New LoginName: ");
        try {
            String Username = console.readLine();

            String insertQuery = "UPDATE CUSTOMER SET Username = ? WHERE UserID = ? ";

            PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
            preparedStatement.setString(1, Username);
            preparedStatement.setInt(2, UserPanel.userID);
            System.out.println();
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("LoginName Update!");
        }catch (Exception e)
        {
            System.out.println("Invalid input. Check the input and try again.");
        }

    }

    public static void searchByName() throws SQLException, IOException, InterruptedException {
        Console console= System.console();
        System.out.print("Input the product name: ");
        String productName = console.readLine();

        ResultSet productList;
        Statement st1 = conx.createStatement();

        UserPanel.clearScreen();

        productList = st1.executeQuery("SELECT * FROM PRODUCT where ProductName = '" + productName + "'");
//        ProductID    NUMBER(4) PRIMARY KEY,
//        ProductName  VARCHAR(50),
//                Price Number(4),
//                Specification  VARCHAR(50),
//                Description  VARCHAR(50),
//                SellerID NUMBER(4),
        System.out.println("Product ID         Product Name      Product Price      Specification     Description");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s %-15s %n", productList.getInt(1), productList.getString(2),
                    productList.getInt(3), productList.getString(4),productList.getString(5));
        }
        st1.close();
        System.out.println();// .close = commit
    }
    public static void searchByID() throws SQLException, IOException, InterruptedException {
        Console console= System.console();
        System.out.print("Input the product ID (begin with 0XXX): ");
        int productID = Integer.parseInt(console.readLine());
        Statement st1 = conx.createStatement();


        ResultSet productList = st1.executeQuery("SELECT * FROM PRODUCT WHERE ProductID="+productID);
        if(productList.next()){
            System.out.println("\nProduct founded! Details of "+productList.getString(2)+" is shown below.\n");
            System.out.println("ProductID: "+productList.getInt(1));
            System.out.println("Product name: "+productList.getString(2));
            System.out.println("Product price: "+productList.getInt(3));
            System.out.println("Product specification: "+productList.getString(4));
            System.out.println("Product description: "+productList.getString(5));
            System.out.println("Product Seller ID: "+productList.getInt(6));
            Thread.sleep(2000);
        }else{
            System.out.println("\nSorry, product is not founded!");
            Thread.sleep(2500);
            UserPanel.clearScreen();
        }
        st1.close();
    }
    private static void deleteProductInCart() {
        Console console = System.console();

        System.out.print("Enter ProductID: ");
        try {
            int productID = Integer.parseInt(console.readLine());

            String deleteQuery = "DELETE FROM CART WHERE UserID = ? AND ProductID = ?";

            PreparedStatement preparedStatement = conx.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, UserPanel.userID);
            preparedStatement.setInt(2, productID);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            if (rowsAffected > 0) {
                System.out.println("Product deleted!");
            } else {
                System.out.println("Product not found in the cart.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Check if the ID exists.");
        }
    }


    private static void listAllProductInCart() throws SQLException, IOException, InterruptedException {
        ResultSet productList;
        Statement st1 = conx.createStatement();

        UserPanel.clearScreen();

        productList = st1.executeQuery("SELECT * FROM CART where UserID = '" + UserPanel.userID + "'" );
        System.out.println("Product ID"+"               "+"Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.println(productList.getInt(2)+"        "+
                    productList.getInt(3));
        }
        st1.close(); // .close = commit
    }

    public static void USER() throws SQLException,IOException,InterruptedException{

        Console console = System.console();
        OracleConnection conn = null;
        while (!(loginOracle)) { //Login Oracle
            try {
                System.out.print("Enter your DBMS username: ");    // Your Oracle ID with double quote
                String username = console.readLine();         // e.g. "98765432d"     String username = console.readLine();         // e.g. "98765432d"
                System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account

                char[] password = console.readPassword();
                String pwd = String.valueOf(password);
                DriverManager.registerDriver(new OracleDriver());
                conn = (OracleConnection) DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", username, pwd);
                loginOracle = true;
            } catch (Exception e) {
                System.out.println("Oracle login failed. Try again");
            }
        }

        conx = conn;
        clearScreen();
        System.out.println("Database Login successfully");
        System.out.println(" ");

        if (!(loginStatus)) { //Login user account (OSS)
            System.out.println("Please login your account to continue - Metaverse Online Shopping System");
            System.out.print("Input your username: ");
            String InputUserName = console.readLine();;
            System.out.print("Input your password: ");
            String InputUserPwd = console.readLine();
            loginStatus = loginCheck(InputUserName, InputUserPwd);
            while (!(loginStatus)){
                System.out.println("\nInvalid Username or password! Please Retry.");
                System.out.print("Input your username: ");
                InputUserName = console.readLine();
                System.out.print("Input your password: ");
                InputUserPwd = console.readLine();
                loginStatus = loginCheck(InputUserName, InputUserPwd);
            }

        }

        clearScreen();
        System.out.println("Login successfully! Metaverse Online Shopping System");
        System.out.println(" ");


        boolean inputJ = true;
        while (inputJ) {
            System.out.println("1. Search item");
            System.out.println("2. Inspect shopping cart");
            System.out.println("3. Check parcel status");
            System.out.println("4. Account settings\n");
            System.out.println("Enter -1 to exit\n");
            System.out.print("Input the number option: ");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            switch (input) {
                case 1:
                    searchItem();
                    break;
                case 2:
                    ShoppingCart();
                    break;
                case 3:
                    checkParcel();
                    break;
                case 4:
                    accountManagement();
                    break;
                default:
                    inputJ = false;
                    System.out.println("Good Bye");
                    conx.close();
                    break;
            }
        }
     }
}

