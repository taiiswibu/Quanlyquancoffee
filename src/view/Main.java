/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package view;
import java.sql.*;
/**
 *
 * @author Vuhuy
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LoginF().setVisible(true);
                } catch (SQLException ex) {
                    //Logger.getLogger(LoginF.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
