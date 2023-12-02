

import java.io.*;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;

public class Test
{
    public static void main(String args[]) throws SQLException, IOException, InterruptedException {
        // Login
  // Your Oracle ID with double quote
        Console console = System.console();
        System.out.print("Enter your username: ");    // Your Oracle ID with double quote
        String username = console.readLine();         // e.g. "98765432d"
        System.out.print("Enter your password: ");    // Password of your Oracle Account
        char[] password = console.readPassword();
        String pwd = String.valueOf(password);

        // Connection
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleConnection conn =
                (OracleConnection) DriverManager.getConnection(
                        "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms", username, pwd);
///*
//        Prepare employee list
//        Statement接口定义了执行SQL语句和操作结果集的主要方法:
//        execute()方法可以执行任何SQL语句,返回boolean表示是否是一个ResultSet结果
//        executeQuery()方法执行SELECT语句,返回ResultSet结果集
//        executeUpdate()方法执行INSERT、UPDATE、DELETE等语句,返回影响的行数
//        close()方法关闭Statement对象
//        getXxx()和setXxx()方法用于获取和设置Statement属性,如最大行数、超时时间等*/

//        Java中的ResultSet接口用于表示数据库查询操作的结果集。它的主要功能和方法包括:
//        用来封装SELECT语句执行后的结果集数据
//        next()方法移动指针到下一行,判断是否有更多行
//        getXxx()方法根据列的数据类型提取对应列的值,如getString()、getInt()等
//        findColumn()方法按列名称获取列索引
//        updateXxx()方法更新当前行数据
//        deleteRow()和insertRow()方法删除或插入新的行
//        wasNull()方法判断上一个获取值是否为SQL NULL
//        close()方法关闭结果集对象
//            */


        Statement st1;
        ResultSet resultSet;
        ResultSet customerList;

        st1 = conn.createStatement();
        //此处用resultSet 保存Statement类st1
//        resultSet = st1.executeQuery("SELECT ENO, ENAME FROM EMPLOYEES");
//        resultSet.next();
//        while (resultSet.next()) {
//            System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
//        }

        customerList = st1.executeQuery("SELECT * FROM CUSTOMER");
        customerList.next();
        while (customerList.next()) {
            System.out.println(customerList.getString(1) + "     " + customerList.getString(2)+
                    " " + customerList.getString(3));
        }
        st1.executeUpdate("INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1500,'Echidna','passwd')");
        st1.close(); // .close = commit

    }
}
