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
public class Main_Server {
    public static void main(String[] args) {
        try {
            System.out.println("Building Server");
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Build Complete");
            
            System.out.println("Connecting to SQL");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/group_work","root","lunabeam");
            System.out.println("SQL Connected");
            
            while (true) {                
                System.out.println("Waiting incoming socket connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client address: "+socket.getInetAddress());
                Thread_Server newClient = new Thread_Server(socket,connection);
                newClient.run();
            }          
        } catch (IOException ex) {
            Logger.getLogger(Main_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Main_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
