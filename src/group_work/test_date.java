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
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Adrian
 */
public class test_date {

    public static void main(String[] args) throws ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date DATE = new Date();
        Date test1 = df1.parse("2018-11-30");
        Date test2 = df1.parse(df1.format(DATE));
        Date test3 = df2.parse("2018-12-01 12:00:00");
        long diff = test1.getTime() - test2.getTime();
        
        diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        
        
        
        String Sys_Date = df1.format(DATE);
        if (test1.equals(test2)){
            System.out.println("equal");
        }
        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test3);
        System.out.println(Sys_Date);
        System.out.println(diff);
    }
}
