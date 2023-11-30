import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
public class OSS {
    public static void main(String[] args) {
        Console console = System.console();
        boolean inputStatus = true;
        while(inputStatus) {
            System.out.println("\nPlease select the login method");
            System.out.println("1. User Panel");
            System.out.println("2. Admin Panel");
            System.out.println("\nEnter -1 to exit this system.");
            System.out.print("\nPlease enter the login option: ");
            String option = console.readLine();
            switch (option)
            {
                case "1":
                    String[] sus=new String[0];
                    try {
                        UserPanel.main(sus);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "2":
                    String[] awa=new String[0];
                    try {
                        AdminPanel.main(awa);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
