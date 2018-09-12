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
        int result = (int) (10*(Math.pow(2,1.5)));
        System.out.println(""+result);
        String lol="berita1,kat1-berita2,kat2|sukses";
        String lolol ="192.168.43.138";
        String[] lol1 = lol.split("\\|");
        String[] lol2 = lol1[0].split("-");
        String[] lolol1 = lolol.split("\\.");
        System.out.println(lolol1[2]);
        System.out.println(lol2[1]);
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tgl = new Date();
        String tanggal =date.format(tgl);
        System.out.println(""+tanggal);
    }
}
