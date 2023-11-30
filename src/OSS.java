import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class OSS {
    public static void main(String[] args) throws SQLException,IOException,InterruptedException {
        Console console = System.console();
        boolean inputStatus = true;
        while(inputStatus) {
            System.out.println("Welcome to Group30 Online Shopping System");
            System.out.println("\nPlease select the login method");
            System.out.println("1. User Panel");
            System.out.println("2. Admin Panel");
            System.out.println("\nEnter -1 to exit this system.");
            System.out.print("\nPlease enter the login option: ");
            String option = console.readLine();
            switch (option)
            {
                case "1":
                    UserPanel.USER();
                    break;
                case "2":
                    AdminPanel.Admin();
                    break;
                case "-1":
                    System.out.println("Exiting panel...");
                    System.exit(1);
                    break;
                default:
                    inputStatus=false;
                    break;
            }
        }
    }
    }
