import java.io.*;
import java.io.Console;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

public class simpleApplication
{
	public static void main(String args[]) throws SQLException, IOException
	{
		Console console = System.console();
		System.out.print("Enter your username: ");    // Your Oracle ID with double quote
		String username = console.readLine();         // e.g. "98765432d"
		System.out.print("Enter your password: ");    // Password of your Oracle Account
		char[] password = console.readPassword();
		String pwd = String.valueOf(password);

		// Connection
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		OracleConnection conn = 
			(OracleConnection)DriverManager.getConnection(
				"jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms",username,pwd);
		 stmt = conn.cre
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT EMPNO, ENAME, JOB FROM EMP");
		while (rset.next())
		{
			System.out.println(rset.getInt(1) 
			+ " " + rset.getString(2) 
			+ " " + rset.getString(3));
		}
		System.out.println();
		conn.close();
	}
}
