/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nishat
 */
import java.sql.*;
import javax.swing.*;

public class javaConnect {

    private static Connection connection = null;

    public static Connection connectDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:sqlite:EasyStat.sqlite");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hospital_management","root","");
            //JOptionPane.showMessageDialog(null, "Connection established!");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}
