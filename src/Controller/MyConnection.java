/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Admin
 */

import java.sql.Connection;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class MyConnection {
    
    public static final String username = "root";
    public static final String pasworld = "2004";
    public static final String url = "jdbc:mysql://localhost:3306/cafe2";
    public static Connection con = null;
    
    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//Sử dụng Class.forName("com.mysql.cj.jdbc.Driver") để tải lớp trình điều khiển JDBC cho MySQL.
            con =  DriverManager.getConnection(url,username,pasworld);//Sau đó, sử dụng DriverManager.getConnection(url, username, pasworld) để thiết lập kết nối đến cơ sở dữ liệu.
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(MyConnection.class.getName()).log(Level.SEVERE,null,ex);
            JOptionPane.showInternalMessageDialog(null, ""+ex,"",JOptionPane.WARNING_MESSAGE);
        }      
        return con;
    }
    
}
