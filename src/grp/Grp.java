/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class Grp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("Building Server");
            ServerSocket serverSocket = new ServerSocket(1236);
            System.out.println("Build Complete");
            
            System.out.println("Building FB");
            Firestore db = postFB();
            System.out.println("Build FB Complete");

            System.out.println("Connecting to SQL");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://telematics.petra.ac.id/gamification", "adrian", "ian123");
            System.out.println("SQL Connected");

            while (true) {
                System.out.println("Waiting incoming socket connection");
                Socket socket = serverSocket.accept();
                System.out.println("Connected to client address: " + socket.getInetAddress());
                //Thread_Server newClient = new Thread_Server(socket, db);
                Thread_Server newClient = new Thread_Server(socket, connection,db);
                new Thread(newClient).start();
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Grp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Firestore postFB() {
        InputStream serviceAccount = null;
        Firestore db = null;
        try {
            FirestoreOptions options = FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build();
            serviceAccount = new FileInputStream("C:\\Users\\Adrian\\Downloads\\grouptaskmanagement-60230-firebase-adminsdk-tv4xw-14171bab51.json");
            FirebaseOptions options1 = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://grouptaskmanagement-60230.firebaseio.com/")
                    .setFirestoreOptions(options)
                    .setProjectId("grouptaskmanagement-60230")
                    .build();
            db = FirestoreClient.getFirestore(FirebaseApp.initializeApp(options1));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                serviceAccount.close();
            } catch (IOException ex) {
                Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("FB process done");
        return db;
    }
}
