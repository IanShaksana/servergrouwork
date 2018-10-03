/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class DB_MNG {

    public static void main(String[] args) {
        try {

            System.out.println("Connecting to SQL");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/group_work", "root", "lunabeam");
            System.out.println("SQL Connected");
            DB_MANAGEMENT newManagement = new DB_MANAGEMENT(connection);
            newManagement.run();

                        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB_MNG.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DB_MNG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
