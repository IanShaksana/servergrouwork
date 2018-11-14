/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class testFirebase {

    public static void main(String[] args) {
        try {
            FirestoreOptions options1 = FirestoreOptions.newBuilder().setTimestampsInSnapshotsEnabled(true).build();
            //Firestore firestore = options1.getService();
            System.out.println("Building");
            InputStream serviceAccount = new FileInputStream("C:\\Users\\Adrian\\Downloads\\grouptaskmanagement-60230-firebase-adminsdk-tv4xw-14171bab51.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://grouptaskmanagement-60230.firebaseio.com/")
                    .setFirestoreOptions(options1)
                    .setProjectId("grouptaskmanagement-60230")
                    .build();
            System.out.println("Option done");
            Firestore db = FirestoreClient.getFirestore(FirebaseApp.initializeApp(options));
            DocumentReference docRef = db.collection("users").document("alovelace");
            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("first", "Ada");
            data.put("last", "Lovelace");
            data.put("born", 1815);
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // result.get() blocks on response
            System.out.println("Update time : " + result.get().getUpdateTime());
            System.out.println("done");
            System.out.println("test self");
            DocumentReference noteRef = db.document("test/hai");
            ApiFuture<WriteResult> result1 = noteRef.set(new testFirebase2("this is title", "andd"));
            System.out.println("Update time : " + result1.get().getUpdateTime());
            System.out.println("done");
        } catch (IOException ex) {
            Logger.getLogger(testFirebase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(testFirebase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(testFirebase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
