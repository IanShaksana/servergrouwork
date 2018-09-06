/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 *
 * @author Adrian
 */
public class serverimage {
    static Date start,end;
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(13085);
        System.out.println("Waiting");
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(0);
        System.out.println("Someone connected : "+socket.getInetAddress());
        
        InputStream inputStream = socket.getInputStream();
        DataInputStream dis = new DataInputStream(inputStream);
        BufferedInputStream dis1 = new BufferedInputStream(inputStream);
        FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\Adrian\\Pictures\\Pic1taken.jpg"));
        
        int ou;
        int size=0;
        start =new Date();
        System.out.println("Reading");
        while((ou=dis1.read())>-1){
            fos.write(ou);
            size++;
        }
        end = new Date();
        int time = (int) ((end.getTime())-(start.getTime()))/1000;
        
        System.out.println("size : "+size);
        System.out.println("time : "+time);
        
        System.out.println("Fos flushing");
        fos.flush();
        System.out.println("Fos finish flushing");
        
        System.out.println("Fos closing");
        fos.close();
        System.out.println("Fos finish closing");
        
        System.out.println("Dis closing");
        dis1.close();
        System.out.println("Dis finish closing");
        
        System.out.println("Socket closing");
        socket.close();
        System.out.println("Socket finish closing");
    }
    
}
