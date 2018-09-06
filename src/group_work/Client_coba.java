/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class Client_coba {

    public static void main(String[] args) {
        try {
            System.out.println("Building");
            Socket socket = new Socket();
            String address = "127.0.0.1";
            InetAddress serverAddr = InetAddress.getByName(address);
            SocketAddress sockaddr = new InetSocketAddress(serverAddr, 13085);
            System.out.println("Connecting");
            socket.connect(sockaddr);
            System.out.println("Connected");
            Scanner input = new Scanner(System.in);
            System.out.println("please input file name");
            String filename =input.nextLine();

            OutputStream outputStream = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(new File("C:\\Users\\Adrian\\Pictures\\"+filename+".jpg"));
            DataOutputStream dos  = new DataOutputStream(outputStream);
            BufferedOutputStream dos1  = new BufferedOutputStream(outputStream);
            int io;
            while((io=fis.read())> -1){
                dos1.write(io);
            }
            fis.close();
            dos1.close();
            socket.close();

        } catch (UnknownHostException ex) {
            Logger.getLogger(Client_coba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client_coba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
