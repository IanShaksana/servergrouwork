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
public class test_main {

    public static void main(String[] args) {
        try {
            System.out.println("Building Server");
            ServerSocket serverSocket = new ServerSocket(1236);
            System.out.println("Build Complete");
                        
            while (true) {                
                System.out.println("Waiting incoming socket connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client address: "+socket.getInetAddress());
                testThread newClient = new testThread(socket,serverSocket);
                new Thread(newClient).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(test_main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
