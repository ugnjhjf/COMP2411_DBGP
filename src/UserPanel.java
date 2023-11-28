import  java.util.*;
import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class UserPanel {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        boolean inputJ = true;
        while (inputJ) {
            System.out.println("1. Search item");
            System.out.println("2. Inspect shopping cart");
            System.out.println("3. Check parcel status");
            System.out.println("4. Account settings");
            System.out.print("Input the number option: ");
            Scanner scanner = new Scanner(System.in);
            int input = scanner.nextInt();
            switch (input) {s
                case 1:
                    searchItem();
                    break;
                case 2:
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
            }


        }
    }
    public static void searchItem() throws SQLException{
        System.out.println("1. Search by product name");
        System.out.println("2. Search by product ID");
        System.out.println("3. List all products");
        System.out.println("4. Add product");
        System.out.print("Input the number option: ");
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

    }
}
