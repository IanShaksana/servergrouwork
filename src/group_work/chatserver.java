/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class chatserver {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/group_work","root","lunabeam");
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server Initiating");
            while (true) {
                    
                String data2 = null;
                String data3 = null;
                String dataNO =null;
                String dataID =null;
                
                Socket socket = serverSocket.accept();
                String InetAddress = socket.getInetAddress().toString();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String data = bufferedReader.readLine();
                
                PrintStream printStream = new PrintStream(socket.getOutputStream());
                
                System.out.println("Query Num get/get : "+data+" from : "+InetAddress);
                if(data.equals("message")){
                    System.out.println("Messaging");
                    String sender = bufferedReader.readLine();
                    String recepient = bufferedReader.readLine();
                    String message = bufferedReader.readLine();
                    System.out.println("From : "+sender);
                    System.out.println("To : "+recepient);
                    System.out.println("Message : "+message);
                    PreparedStatement statement;
                    statement = connection.prepareStatement("INSERT INTO `group_work`.`message` (`sender`, `recepient`, `message`) VALUES ('"+sender+"', '"+recepient+"', '"+message+"');");
                    statement.executeUpdate();
                    System.out.println("Message send");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(chatserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(chatserver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(chatserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
