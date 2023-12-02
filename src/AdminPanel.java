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
            System.out.println("8. Print all users");

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
                case "4":
                    searchByID();
                    break;
                case "5":
                    showAParcel();
                    break;
                case "6"://待完善确认
                    analyzeSales();
                    break;
                case "7":
                    addUser();
                    break;
                case "8":
                    showAllUser();
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

        int productID;
        while (true) {
            System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");

            try {
                productID = Integer.parseInt(console.readLine());
                if(productID<1000&&productID>0){
                    //还要判断是有撞productID
                    String checkSellerQuery = "SELECT COUNT(*) FROM PRODUCT WHERE ProductID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    checkSellerStatement.setInt(1, productID);
                    ResultSet sellerResultSet = checkSellerStatement.executeQuery();
                    sellerResultSet.next();
                    int productCount = sellerResultSet.getInt(1);
                    checkSellerStatement.close();
                    if (productCount == 1) {
                        System.out.println("This ProductID is already exist in the database.");
                    }else{
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ProductID must be a numeric value and smaller than 1000.");
            }
        }

        System.out.print("Enter ProductName: ");
        String productName = console.readLine();

        int price;
        while (true) {
            System.out.print("Enter Price (4 digits integer only): ");
            try {
                price = Integer.parseInt(console.readLine());
                if(price<10000){
                    break;
                }else {
                    System.out.println("Invalid input! Price value should smaller than 10000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Price must be a numeric value smaller than 10000.");
            }
        }

        System.out.print("Enter Specification: ");
        String specification = console.readLine();

        System.out.print("Enter Description: ");
        String descript = console.readLine();

        int sellerID;
        while (true) {
            System.out.print("Enter SellerID (Existing Sellers only! 4 numbers only, begin with 3XXX): ");
            try {
                sellerID = Integer.parseInt(console.readLine());
                if(sellerID>=3000 && sellerID<5000){
                    //判断是否存在SellerID
                    String checkSellerQuery = "SELECT COUNT(*) FROM SELLER WHERE SellerID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    checkSellerStatement.setInt(1, sellerID);
                    ResultSet sellerResultSet = checkSellerStatement.executeQuery();
                    sellerResultSet.next();
                    int sellerCount = sellerResultSet.getInt(1);
                    checkSellerStatement.close();
                    if (sellerCount == 0) {
                        System.out.println("SellerID does not exist in the database. Please input again!");
                    }else{
                        break;
                    }
                }else{
                    System.out.println("Invalid input! SellerID should greater than 3000 smaller than 5000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! SellerID must be a numeric value greater than 3000 smaller than 5000.");
            }
        }

        // Check if the SellerID exists in the database
//        String checkSellerQuery = "SELECT COUNT(*) FROM SELLER WHERE SellerID=?";
//        PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
//        checkSellerStatement.setInt(1, sellerID);
//        ResultSet sellerResultSet = checkSellerStatement.executeQuery();
//        sellerResultSet.next();
//        int sellerCount = sellerResultSet.getInt(1);
//        checkSellerStatement.close();

//        if (sellerCount == 0) {
//            System.out.println("SellerID does not exist in the database.");
//            return;
//        }

        // Insert the product
        String insertQuery = "INSERT INTO PRODUCT(ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = conx.prepareStatement(insertQuery);
        insertStatement.setInt(1, productID);
        insertStatement.setString(2, productName);
        insertStatement.setInt(3, price);
        insertStatement.setString(4, specification);
        insertStatement.setString(5, descript);
        insertStatement.setInt(6, sellerID);

        insertStatement.executeUpdate();
        insertStatement.close();
        System.out.println("Product Added Successfully!");
    }

    //    static void addItem() throws SQLException, IOException, InterruptedException {
//        clearScreen();
//        Console console = System.console();
//
//        System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");
//        int productID = Integer.parseInt(console.readLine());
//        System.out.print("Enter ProductName: ");
//        String productName = console.readLine();
//        System.out.print("Enter Price(4 digits integer only): ");
//        int  price= Integer.parseInt(console.readLine());
//        System.out.print("Enter Specification: ");
//        String specification = console.readLine();
//        System.out.print("Enter Description: ");
//        String descript = console.readLine();
//        System.out.print("Enter SellerID(Existing Sellers only![4 numbers only, begin with 3XXX]): ");
//        int sellerID = Integer.parseInt(console.readLine());
//
//        String insertQuery = "INSERT INTO PRODUCT(ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (?, ?, ?, ?, ?, ?)";
//
//        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
//        preparedStatement.setInt(1, productID);
//        preparedStatement.setString(2, productName);
//        preparedStatement.setInt(3, price);
//        preparedStatement.setString(4, specification);
//        preparedStatement.setString(5, descript);
//        preparedStatement.setInt(6, sellerID);
//
//        preparedStatement.executeUpdate();
//        preparedStatement.close();
//        System.out.println("Product Added Successfully!");
//    }
    static void deleteItem() throws SQLException, IOException, InterruptedException {
        Console console = System.console();
        int productID;
        while (true) {
            System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");
            try {
                productID = Integer.parseInt(console.readLine());
                if(productID<1000&&productID>0){
                    //还要判断是有撞productID
                    String checkSellerQuery = "SELECT COUNT(*) FROM PRODUCT WHERE ProductID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    checkSellerStatement.setInt(1, productID);
                    ResultSet productList = checkSellerStatement.executeQuery();
                    productList.next();
                    int productCount = productList.getInt(1);
                    if (productCount == 0) {
                        System.out.println("This Product is not exist in the product database.");
                    }else{
                        String deleteQuery = "DELETE FROM PRODUCT WHERE ProductID=?";
                        PreparedStatement deleteStatement = conx.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, productID);
                        deleteStatement.executeUpdate();
                        deleteStatement.close();
                        checkSellerStatement.close();
                        System.out.println("Product Deleted!");
                        break;
                    }
                }else{
                    System.out.println("Invalid input! ProductID must be a numeric value smaller than 1000.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input! ProductID must be a numeric value smaller than 1000.");
            }
        }
//        Console console = System.console();
//        System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");
//        int productID = Integer.parseInt(console.readLine());
//        String checkQuery = "SELECT COUNT(*) FROM PRODUCT WHERE ProductID=?";
//        PreparedStatement checkStatement = conx.prepareStatement(checkQuery);
//        checkStatement.setInt(1, productID);
//        ResultSet resultSet = checkStatement.executeQuery();
//        resultSet.next();
//        int count = resultSet.getInt(1);
//        checkStatement.close();
//
//        if (count == 0) {
//            System.out.println("Sorry, this product does not exist in the database.");
//            return;
//        }
//
//        String deleteQuery = "DELETE FROM PRODUCT WHERE ProductID=?";
//        PreparedStatement deleteStatement = conx.prepareStatement(deleteQuery);
//        deleteStatement.setInt(1, productID);
//        deleteStatement.executeUpdate();
//        deleteStatement.close();
//
//        System.out.println("Product Deleted!");

    }

    public static void searchByID() throws SQLException, IOException, InterruptedException {
        clearScreen();
        Console console = System.console();

        int productID;
        while (true) {
            System.out.print("Enter ProductID (4 numbers only, begin with 0XXX): ");
            try {
                productID = Integer.parseInt(console.readLine());
                if(productID<1000&&productID>0){
                    //还要判断是有撞productID
                    String checkSellerQuery = "SELECT COUNT(*) FROM PRODUCT WHERE ProductID=?";
                    String wow = "SELECT * FROM PRODUCT WHERE ProductID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    PreparedStatement output = conx.prepareStatement(wow);
                    checkSellerStatement.setInt(1, productID);
                    output.setInt(1, productID);
                    ResultSet productList = checkSellerStatement.executeQuery();
                    ResultSet oo=output.executeQuery();
                    productList.next();
                    oo.next();
                    int productCount = productList.getInt(1);
                    checkSellerStatement.close();
                    if (productCount == 0) {
                        System.out.println("This Product is not exist in the product database.");
                        Thread.sleep(2500);
                        break;
                    }else{
                        System.out.println("\nProduct founded! Details of "+oo.getString(2)+" is shown below.\n");
                        System.out.println("ProductID: "+oo.getInt(1));
                        System.out.println("Product name: "+oo.getString(2));
                        System.out.println("Product price: "+oo.getInt(3));
                        System.out.println("Product specification: "+oo.getString(4));
                        System.out.println("Product description: "+oo.getString(5));
                        System.out.println("Product Seller ID: "+oo.getInt(6));
                        Thread.sleep(2000);
                        break;
                    }
                }else{
                    System.out.println("Invalid input. ProductID must be a numeric value and smaller than 1000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. ProductID must be a numeric value and smaller than 1000.");
            }
        }
//        Console console= System.console();
//        System.out.print("Input the product ID (begin with 0XXX): ");
//        int productID = Integer.parseInt(console.readLine());
//        Statement st1 = conx.createStatement();
//
//
//        ResultSet productList = st1.executeQuery("SELECT * FROM PRODUCT WHERE ProductID="+productID);
//        if(productList.next()){
//            System.out.println("\nProduct founded! Details of "+productList.getString(2)+" is shown below.\n");
//            System.out.println("ProductID: "+productList.getInt(1));
//            System.out.println("Product name: "+productList.getString(2));
//            System.out.println("Product price: "+productList.getInt(3));
//            System.out.println("Product specification: "+productList.getString(4));
//            System.out.println("Product description: "+productList.getString(5));
//            System.out.println("Product Seller ID: "+productList.getInt(6));
//            Thread.sleep(2000);
//        }else{
//            System.out.println("\nSorry, product is not founded!");
//            Thread.sleep(2500);
//            clearScreen();
//        }
//        st1.close(); // .close = commit
    }

    static void showAParcel() throws SQLException, IOException, InterruptedException {
        clearScreen();
        Console console = System.console();

        int productID;
        while (true) {
            System.out.print("Enter ParcelID (4 numbers only, begin with 5XXX): ");
            try {
                productID = Integer.parseInt(console.readLine());
                if(productID<7000&&productID>=5000){
                    //还要判断是有撞parcelID
                    String checkSellerQuery = "SELECT COUNT(*) FROM Parcel WHERE ParcelID=?";
                    String wow = "SELECT * FROM Parcel WHERE ParcelID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    PreparedStatement output = conx.prepareStatement(wow);
                    checkSellerStatement.setInt(1, productID);
                    output.setInt(1, productID);
                    ResultSet productList = checkSellerStatement.executeQuery();
                    productList.next();
                    int productCount = productList.getInt(1);
                    checkSellerStatement.close();
                    if (productCount == 0) {
                        System.out.println("This Parcel is not exist in the database.");
                        Thread.sleep(2500);
                        break;
                    }else{
                        ResultSet oo=output.executeQuery();
                        oo.next();
                        System.out.println("\nParcel founded! Details are shown below.\n");
                        System.out.println("Parcel ID: "+oo.getInt(1));
                        System.out.println("Product ID: "+oo.getInt(2));
                        System.out.println("Quantity: "+oo.getInt(3));
                        System.out.println("User ID: "+oo.getInt(4));
                        System.out.println("Shipping address: "+oo.getString(5));
                        Thread.sleep(2000);
                        break;
                    }
                }else{
                    System.out.println("Invalid input! ParcelID must greater than 5000 smaller than 7000.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! ParcelID must be a numeric value greater than 5000 smaller than 7000.");
            }
        }
//        Console console= System.console();
//        System.out.print("Input the parcel ID (begin with 5XXX): ");
//        int parcelID = Integer.parseInt(console.readLine());
//        Statement st1 = conx.createStatement();
//
//        ResultSet productList = st1.executeQuery("SELECT * FROM PARCEL WHERE ParcelID="+parcelID);
//        if(productList.next()){
//            System.out.println("\nParcel founded! Details of the parcel is shown below.\n");
//            System.out.println("Parcel ID: "+productList.getInt(1));
//            System.out.println("Product ID: "+productList.getInt(2));
//            System.out.println("Quantity: "+productList.getInt(3));
//            System.out.println("User ID: "+productList.getInt(4));
//            System.out.println("Shipping address: "+productList.getString(5));
//            Thread.sleep(2000);
//        }else{
//            System.out.println("\nSorry, this parcel is not founded!");
//            Thread.sleep(2500);
//            clearScreen();
//        }
//        st1.close();
    }

    static void showAllItem() throws SQLException, IOException, InterruptedException {
        clearScreen();
        System.out.println("\nProducts in stock are shown below: ");
        System.out.println("Products ID, Product Name, Product Price, Product specification, Product description, Seller ID, Quantity");
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
                    " "+rs.getInt(7));
        }
        st1.close();

    }

    static void addUser() throws SQLException, IOException, InterruptedException {
        clearScreen();
        Console console = System.console();
        int productID;
        while (true) {
//        Console console = System.console();
//        System.out.print("Enter UserID: ");
//        int userID = Integer.parseInt(console.readLine());
            System.out.print("Enter UserID (4 numbers only, begin with 1XXX): ");
            try {
                productID = Integer.parseInt(console.readLine());
                if(productID<3000&&productID>1000){
                    //还要判断是有撞productID
                    String checkSellerQuery = "SELECT COUNT(*) FROM CUSTOMER WHERE UserID=?";
                    PreparedStatement checkSellerStatement = conx.prepareStatement(checkSellerQuery);
                    checkSellerStatement.setInt(1, productID);
                    ResultSet sellerResultSet = checkSellerStatement.executeQuery();
                    sellerResultSet.next();
                    int productCount = sellerResultSet.getInt(1);
                    checkSellerStatement.close();
                    if (productCount == 1) {
                        System.out.println("This User is already exist in the database, try another UserID");
                    }else{
                        System.out.print("Enter Username: ");
                        String userName = console.readLine();
                        System.out.print("Enter password: ");
                        String passWord = console.readLine();
                        System.out.print("Enter Telephone: ");
                        String telphone = console.readLine();
                        System.out.print("Enter Shipping address: ");
                        String address = console.readLine();
                        String insertQuery = "INSERT INTO CUSTOMER (UserID, Username, Password,Tel,Shipping_address) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
                        preparedStatement.setInt(1, productID);
                        preparedStatement.setString(2, userName);
                        preparedStatement.setString(3, passWord);
                        preparedStatement.setString(4, telphone);
                        preparedStatement.setString(5, address);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        System.out.println("User Added!");
                        break;
                    }
                }else{
                    System.out.println("Invalid input. UserID must be a numeric value and greater than 1000 smaller than 3000.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. UserID must be a numeric value and greater than 1000 smaller than 3000.");
            }
        }
//        clearScreen();
//        Console console = System.console();
//        System.out.print("Enter UserID: ");
//        int userID = Integer.parseInt(console.readLine());
//        System.out.print("Enter Username: ");
//        String userName = console.readLine();
//        System.out.print("Enter password: ");
//        String passWord = console.readLine();
//        System.out.print("Enter Telephone: ");
//        String telphone = console.readLine();
//        System.out.print("Enter Shipping address: ");
//        String address = console.readLine();
//
//
//        String insertQuery = "INSERT INTO CUSTOMER (UserID, Username, Password,Tel,Shipping_address) VALUES (?, ?, ?, ?, ?)";
//
//        PreparedStatement preparedStatement = conx.prepareStatement(insertQuery);
//        preparedStatement.setInt(1, userID);
//        preparedStatement.setString(2, userName);
//        preparedStatement.setString(3, passWord);
//        preparedStatement.setString(4, telphone);
//        preparedStatement.setString(5, address);
//
//        preparedStatement.executeUpdate();
//        preparedStatement.close();
//        System.out.println("User Added!");
    }
    private static void analyzeSales() throws IOException, InterruptedException {
        clearScreen();
        try {
            // 查询售出物品的数量
            String quantityQuery = "SELECT SUM(Quantity) AS TotalQuantity FROM PARCEL";
            Statement quantityStatement = conx.createStatement();
            ResultSet quantityResult = quantityStatement.executeQuery(quantityQuery);

            if (quantityResult.next()) {
                int totalQuantity = quantityResult.getInt("TotalQuantity");
                System.out.println("Total quantity of sold items: " + totalQuantity);
            }

            quantityResult.close();
            quantityStatement.close();

            // 查询最畅销的产品名称
            String topProductQuery = "SELECT p.ProductName, SUM(pr.Quantity) AS TotalQuantity " +
                    "FROM PRODUCT p " +
                    "JOIN PARCEL pr ON p.ProductID = pr.ProductID " +
                    "GROUP BY p.ProductName " +
                    "ORDER BY TotalQuantity DESC " +
                    "FETCH FIRST ROW ONLY";
            Statement topProductStatement = conx.createStatement();
            ResultSet topProductResult = topProductStatement.executeQuery(topProductQuery);

            if (topProductResult.next()) {
                String topProductName = topProductResult.getString("ProductName");
                int topProductQuantity = topProductResult.getInt("TotalQuantity");
                System.out.println("Top selling product: " + topProductName + " (Quantity: " + topProductQuantity + ")");
            }

            topProductResult.close();
            topProductStatement.close();

            // 查询交易总数
            String totalTransactionsQuery = "SELECT COUNT(*) AS TotalTransactions FROM PARCEL";
            Statement totalTransactionsStatement = conx.createStatement();
            ResultSet totalTransactionsResult = totalTransactionsStatement.executeQuery(totalTransactionsQuery);

            if (totalTransactionsResult.next()) {
                int totalTransactions = totalTransactionsResult.getInt("TotalTransactions");
                System.out.println("Total number of transactions: " + totalTransactions);
            }

            totalTransactionsResult.close();
            totalTransactionsStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void Admin() throws SQLException,IOException,InterruptedException{
        Console console = System.console();
        System.out.print("Enter your DBMS username: ");    // Your Oracle ID with double quote
        String username = console.readLine();         // e.g. "98765432d"
        System.out.print("Enter your DBMS password: ");    // Password of your Oracle Account
        char[] password = console.readPassword();
        String pwd = String.valueOf(password);
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        OracleConnection conn =
                null;
        try {
            conn = (OracleConnection) DriverManager.getConnection(
                    "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",username,pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            System.out.println("7. Register new user");
            System.out.println("8. Print all users");

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
                case "4":
                    searchByID();
                    break;
                case "5":
                    showAParcel();
                    break;
                case "6":
                    analyzeSales();
                    break;
                case "7":
                    addUser();
                    break;
                case "8":
                    showAllUser();
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
    private static void showAllUser() throws SQLException,IOException,InterruptedException{
        clearScreen();
        System.out.println("\nUsers in database are shown below: ");
        System.out.println("User ID, Username, Password, Telephone, Shipping_address, Seller ID");
        Statement st1;
        st1 = conx.createStatement();
        ResultSet rs;
        rs = st1.executeQuery("SELECT * FROM CUSTOMER");
        while (rs.next()){
            System.out.println(rs.getString(1)
                    + " " + rs.getString(2)+
                    " " + rs.getString(3)+
                    " "+rs.getString(4)+
                    " "+rs.getString(5));
        }
        st1.close();
    }

}
