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
        while (!(loginOracle)) { //Login Orcale

            System.out.print("Enter your DBMS username: 22084045d");    // Your Oracle ID with double quote
            String username = console.readLine();         // e.g. "98765432d"     String username = console.readLine();         // e.g. "98765432d"
            System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account

            char[] password = console.readPassword();
            String pwd = String.valueOf(password);
//            try {
                DriverManager.registerDriver(new OracleDriver());
                conn = (OracleConnection) DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", username, pwd);
                loginOracle = true;
//            } catch (Exception e) {
//                System.out.println("login failed. Try again");
//            }
        }

        conx = conn;
        clearScreen();
        System.out.println("Database Login successfully");
        System.out.println(" ");

        while (!(loginStatus)) { //Login user account (OSS)
            System.out.println("Please login your account to continue - Metaverse Online Shopping System");
            System.out.print("Input your username: ");
            String InputUserName = console.readLine();
//            String InputUserName = "Jones1";
            System.out.print("Input your password: ");
//            String InputUserPwd = "pass2";
            String InputUserPwd = console.readLine();
            loginStatus = loginCheck(InputUserName, InputUserPwd);
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
                    ShoppingCart();
                    break;
                case 3:
                    checkParcel();
                    break;
                case 4:
                    accountManagement();
                    break;
                case 5:
                case 6:
                default:
                    inputJ = false;
                    System.out.println("Good Bye");
                    conx.close();
            }


        }
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
                UserPanel.listAllProduct();
                break;
            case 4:
                UserPanel.addProduct();
                break;
            case 5:
            case 6:
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

        ResultSet productList;
        Statement st1 = conx.createStatement();

        clearScreen();

        productList = st1.executeQuery("SELECT * FROM PRODUCT");
//        ProductID    NUMBER(4) PRIMARY KEY,
//        ProductName  VARCHAR(50),
//                Price Number(4),
//                Specification  VARCHAR(50),
//                Description  VARCHAR(50),
//                SellerID NUMBER(4),
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product ID", "Product Name", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getInt(1), productList.getString(2),
                    productList.getInt(3), productList.getString(4),productList.getString(5));
        }
        st1.close(); // .close = commit
    }
    private static void checkoutCart() {
        try {
            Console console = System.console();

            // 读取要结算的产品数量
            int quantity = Integer.parseInt(console.readLine());

            // 查询产品价格和ID
            List<Integer> productIDs = getProductIDsInCart();
            Map<Integer, Double> productPrices = getProductPrices(productIDs);

            // 更新产品列表中的数量并计算总价格
            double totalPrice = 0;
            String updateProductQuery = "UPDATE PRODUCT SET Quantity = Quantity - ? WHERE ProductID = ?";
            PreparedStatement updateProductStatement = conx.prepareStatement(updateProductQuery);
            for (int productID : productIDs) {
                double pricePerUnit = productPrices.get(productID);
                updateProductStatement.setInt(1, quantity);
                updateProductStatement.setInt(2, productID);
                updateProductStatement.executeUpdate();
                totalPrice += pricePerUnit * quantity;
            }
            updateProductStatement.close();

            // 复制购物车内容到包裹表
            String insertParcelQuery = "INSERT INTO PARCEL(UserID, ProductID, Quantity, ShippingAddress, TotalPrice) SELECT UserID, ProductID, Quantity, Shipping_address, ? FROM CART WHERE UserID = ?";
            PreparedStatement insertParcelStatement = conx.prepareStatement(insertParcelQuery);
            insertParcelStatement.setDouble(1, totalPrice);
            insertParcelStatement.setInt(2, UserPanel.userID);
            insertParcelStatement.executeUpdate();
            insertParcelStatement.close();

            // 获取用户地址并填入包裹表
            String updateParcelQuery = "UPDATE PARCEL SET ShippingAddress = (SELECT Shipping_address FROM USER WHERE UserID = ?) WHERE UserID = ?";
            PreparedStatement updateParcelStatement = conx.prepareStatement(updateParcelQuery);
            updateParcelStatement.setInt(1, UserPanel.userID);
            updateParcelStatement.setInt(2, UserPanel.userID);
            updateParcelStatement.executeUpdate();
            updateParcelStatement.close();

            // 清空购物车
            String deleteCartQuery = "DELETE FROM CART WHERE UserID = ?";
            PreparedStatement deleteCartStatement = conx.prepareStatement(deleteCartQuery);
            deleteCartStatement.setInt(1, UserPanel.userID);
            deleteCartStatement.executeUpdate();
            deleteCartStatement.close();

            System.out.println("Checkout completed!");
        } catch (Exception e) {
            System.out.println("Invalid input. Check if the ID exists.");
        }
    }

    // 获取购物车中的产品ID列表
    private static List<Integer> getProductIDsInCart() throws SQLException {
        List<Integer> productIDs = new ArrayList<>();

        String selectProductIDsQuery = "SELECT DISTINCT ProductID FROM CART WHERE UserID = ?";
        PreparedStatement selectProductIDsStatement = conx.prepareStatement(selectProductIDsQuery);
        selectProductIDsStatement.setInt(1, UserPanel.userID);
        ResultSet resultSet = selectProductIDsStatement.executeQuery();

        while (resultSet.next()) {
            int productID = resultSet.getInt("ProductID");
            productIDs.add(productID);
        }

        resultSet.close();
        selectProductIDsStatement.close();

        return productIDs;
    }

    // 查询产品价格的方法
    private static Map<Integer, Double> getProductPrices(List<Integer> productIDs) throws SQLException {
        Map<Integer, Double> productPrices = new HashMap<>();

        String selectPriceQuery = "SELECT ProductID, Price FROM PRODUCT WHERE ProductID IN (" + String.join(",", Collections.nCopies(productIDs.size(), "?")) + ")";
        PreparedStatement selectPriceStatement = conx.prepareStatement(selectPriceQuery);
        for (int i = 0; i < productIDs.size(); i++) {
            selectPriceStatement.setInt(i + 1, productIDs.get(i));
        }
        ResultSet resultSet = selectPriceStatement.executeQuery();

        while (resultSet.next()) {
            int productID = resultSet.getInt("ProductID");
            double pricePerUnit = resultSet.getDouble("Price");
            productPrices.put(productID, pricePerUnit);
        }

        resultSet.close();
        selectPriceStatement.close();

        return productPrices;
    }


    static void addProduct() throws SQLException {
        Console console = System.console();

        System.out.print("Enter ProductID: ");
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
        ResultSet productList;
        clearScreen();
        try {
            Statement st1 = conx.createStatement();
            productList = st1.executeQuery("SELECT PRODUCT.productName,PARCEL.Quantity,PARCEL.Shipping_address FROM PARCEL where UserID = " + UserPanel.userID + "" + "AND PRODUCT.ProductID = PARCEL.ProductID");
            System.out.printf("%-15s %-10s %-15s %n", "Product Name", "Quantity", "Shipping address");
            System.out.println("==============================================================================================");
            while (productList.next()) {
                System.out.printf("%-15s %-10s %-15s %n", productList.getString(1), productList.getInt(2),
                        productList.getString(3));

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
            System.out.println("Password Update!");
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
        System.out.printf("Product ID", "Product Name", "Product Price", "Specification","Description");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.printf("%-15s %-10s %-15s %-17s%n", productList.getInt(1), productList.getString(2),
                    productList.getInt(3), productList.getString(4),productList.getString(5));
        }
        st1.close(); // .close = commit
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
        System.out.printf("%-15s %-10s %-15s %-17s%n", "Product Name", "Product ID", "Product Price", "Product Quantity");
        System.out.println("==============================================================================================");
        while (productList.next()) {
            System.out.println(productList.getInt(1)+""+productList.getInt(2)+""+
                    productList.getInt(3));
        }
        st1.close(); // .close = commit
    }
}
