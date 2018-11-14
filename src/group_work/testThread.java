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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class testThread implements Runnable {

    BufferedReader bufferedReader;
    PrintStream printStream;
    Socket localsocket_thread;
    ServerSocket localserversocket_thread;
    DateFormat df2;

    public testThread(Socket s, ServerSocket sS) {
        try {
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            localserversocket_thread = sS;
            localsocket_thread = s;
            bufferedReader = new BufferedReader(new InputStreamReader(localsocket_thread.getInputStream()));
            printStream = new PrintStream(localsocket_thread.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(testThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Creating Thread");
            String fromClient = bufferedReader.readLine();
            System.out.println("Request From Client : " + fromClient);
            switch (fromClient) {
                case "showtime":
                    printStream.println(df2.format(new Date()).toString());
                    break;
                case "showIP":
                    printStream.println(InetAddress.getLocalHost().getHostAddress());
                    break;
                case "PING":
                    printStream.println("PING");
                    break;
                case "test1":
                    int i = 0;
                    while (i != 4) {
                        Thread.sleep(1000);
                        System.out.println(i);
                        printStream.println("result from test1");
                        i++;
                    }
                    System.out.println("end of test 1");
                    break;
                case "test2":
                    int a = 0;
                    while (a != 4) {
                        Thread.sleep(1000);
                        System.out.println(a);
                        printStream.println("result from test2");
                        a++;
                    }
                    System.out.println("end of test 2");
                    break;

                default:
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(testThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(testThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
