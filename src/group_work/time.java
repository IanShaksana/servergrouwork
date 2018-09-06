/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class time {
    public static void main(String[] args) {
        try {
            int result = (int) Math.pow(9, 3);
            System.out.println(""+result);
            String lol="berita1,kat1-berita2,kat2|sukses";
            String lolol ="192.168.43.138";
            String[] lol1 = lol.split("\\|");
            String[] lol2 = lol1[0].split("-");
            String[] lolol1 = lolol.split("\\.");
            System.out.println(lolol1[2]);
            System.out.println(lol2[1]);
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            Date tgl = new Date();
            
            Date test1 = date.parse("12-09-2995");
            Date test2 = date.parse("12-09-1995");
            System.out.println(""+date.format(tgl));
            if (test1.compareTo(tgl)>0) {
                System.out.println("after");
            }else if (test1.compareTo(tgl)<0){
                System.out.println("before");
            }else if (test1.compareTo(tgl)==0){
                System.out.println("same");
            }
            /*
            long wasd = System.currentTimeMillis();
            String lol = Long.toString(wasd);
            System.out.println(lol);
        */      } catch (ParseException ex) {
            Logger.getLogger(time.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
