/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class time {

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference noteRef = database.getReference("grouptaskmanagement-60230");

    public static void main(String[] args) {
        init();

        noteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewClass post = dataSnapshot.getValue(NewClass.class);
                System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public static void init() {
        FileInputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream("C:\\Users\\Adrian\\Downloads\\grouptaskmanagement-60230-firebase-adminsdk-tv4xw-0f45446d36.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://grouptaskmanagement-60230.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(time.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(time.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                serviceAccount.close();
            } catch (IOException ex) {
                Logger.getLogger(time.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class NewClass {

        private String ID;
        String desc;
        String name;
        String numb;

        public NewClass() {

        }

        public NewClass(String desc, String name) {
            this.name = name;
            this.desc = desc;
        }

        public NewClass(String desc, String name, String numb) {
            this.name = name;
            this.desc = desc;
            this.numb = numb;
        }

        public String getDesc() {
            return desc;
        }

        public String getName() {
            return name;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getNumb() {
            return numb;
        }

    }
}

//        int result = (int) (10*(Math.pow(2,1.5)));
//        System.out.println(""+result);
//        String lol="berita1,kat1-berita2,kat2|sukses";
//        String lolol ="192.168.43.138";
//        String[] lol1 = lol.split("\\|");
//        String[] lol2 = lol1[0].split("-");
//        String[] lolol1 = lolol.split("\\.");
//        System.out.println(lolol1[2]);
//        System.out.println(lol2[1]);
//        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date tgl = new Date();
//        String tanggal =date.format(tgl);
//        System.out.println(""+tanggal);
