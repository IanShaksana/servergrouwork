/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DB_MANAGEMENT implements Runnable {

    Connection localconnection_thread;
    Statement statement;
    DateFormat df;
    DateFormat df2;
    ResultSet res;
    PreparedStatement preparedStatement;

    public DB_MANAGEMENT(Connection connection) {
        try {
            localconnection_thread = connection;
            df = new SimpleDateFormat("yyyy-MM-dd");
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement = localconnection_thread.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {

                System.out.println("this is task");
                FuncCheckDueDate_Task();
                Thread.sleep(2000);
                System.out.println("this is job");
                FuncCheckDueDate_Job();
                Thread.sleep(2000);
                System.out.println("this is cashin");
                FuncCashIn();
            } catch (InterruptedException ex) {
                Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //To change body of generated methods, choose Tools | Templates.
    }

    public void FuncCheckDueDate_Task() {
        try {

            int count = 0;
            System.out.println("SELECT count(Due_Time) FROM task WHERE Completed ='no' ");
            res = statement.executeQuery("SELECT count(Due_Time) FROM task WHERE Completed ='no' ");
            if (res.next()) {
                count = res.getInt(1);
            }
            String[] Task_time = new String[count];
            String[] ID_Task = new String[count];

            count = 0;
            System.out.println("SELECT ID_Task,Due_Time FROM task WHERE Completed ='no' ");
            res = statement.executeQuery("SELECT ID_Task,Due_Time FROM task WHERE Completed ='no' ");
            while (count < Task_time.length) {
                res.next();
                ID_Task[count] = res.getString(1);
                Task_time[count] = res.getString(2);
                count++;
            }

            count = 0;
            while (count < ID_Task.length) {
                System.out.println("--------------------------------------------");
                Date DATE = new Date();
                String Sys_Date = df2.format(DATE);
                Date Task_Date = df2.parse(Task_time[count]);
                String Print_task_Date = df2.format(Task_Date);
                System.out.println("This is for task ID : " + ID_Task[count]);
                System.out.println("Date now: " + Sys_Date);
                System.out.println("Task date: " + Print_task_Date);

                if (DATE.compareTo(Task_Date) > 0) {
                    System.out.println(Sys_Date + " is after " + Print_task_Date);
                    System.out.println("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no', Finished = 'yes',  `Status` ='late' WHERE `task`.`ID_Task` = '" + ID_Task[count] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no', Finished = 'yes', `Status` ='late' WHERE `task`.`ID_Task` = '" + ID_Task[count] + "'");
                    preparedStatement.executeUpdate();
                } else if (DATE.compareTo(Task_Date) < 0) {
                    System.out.println(Sys_Date + " is before " + Print_task_Date);
                } else if (DATE.compareTo(Task_Date) == 0) {
                    System.out.println(Sys_Date + " is the same " + Print_task_Date);
                }
                count++;
                System.out.println("--------------------------------------------");
                System.out.println("");
            }
            System.out.println("end");
        } catch (SQLException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void FuncCheckDueDate_Job() {
        try {
            String duetime = "";
            int count = 0;
            System.out.println("SELECT count(Due_Time) FROM Job WHERE Finished ='no' ");
            res = statement.executeQuery("SELECT count(Due_Time) FROM Job WHERE Finished ='no' ");
            if (res.next()) {
                count = res.getInt(1);
            }
            String[] Job_time = new String[count];
            String[] ID_Job = new String[count];

            count = 0;
            System.out.println("SELECT ID_Job,Due_Time FROM job WHERE Finished ='no' ");
            res = statement.executeQuery("SELECT ID_Job,Due_Time FROM job WHERE Finished ='no' ");
            while (count < Job_time.length) {
                res.next();
                ID_Job[count] = res.getString(1);
                Job_time[count] = res.getString(2);
                count++;
            }

            count = 0;
            while (count < ID_Job.length) {
                System.out.println("--------------------------------------------");
                Date DATE = new Date();
                String Sys_Date = df2.format(DATE);
                Date Task_Date = df2.parse(Job_time[count]);
                String Print_task_Date = df2.format(Task_Date);
                System.out.println("This is for JOB ID : " + ID_Job[count]);
                System.out.println("Date now: " + Sys_Date);
                System.out.println("Task date: " + Print_task_Date);

                if (DATE.compareTo(Task_Date) > 0) {
                    System.out.println(Sys_Date + " is after " + Print_task_Date);
                    System.out.println("UPDATE `group_work`.`job` SET `Status`='end' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Status`='end' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                    preparedStatement.executeUpdate();
                    duetime = "no";
                } else if (DATE.compareTo(Task_Date) < 0) {
                    System.out.println(Sys_Date + " is before " + Print_task_Date);
                    System.out.println("UPDATE `group_work`.`job` SET `Status`='on' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Status`='on' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                    preparedStatement.executeUpdate();
                    duetime = "yes";
                } else if (DATE.compareTo(Task_Date) == 0) {
                    System.out.println(Sys_Date + " is the same " + Print_task_Date);
                    duetime = "no";
                }

                int QtyCurW = 0, QtyMax = 0;
                if (duetime.equals("yes")) {
                    System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job[count] + "' ");
                    res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job[count] + "'");
                    res.next();
                    QtyCurW = res.getInt(1);

                    System.out.println("SELECT Max_Worker FROM job WHERE ID_Job ='" + ID_Job[count] + "' ");
                    res = statement.executeQuery("SELECT Max_Worker  FROM job WHERE ID_Job ='" + ID_Job[count] + "'");
                    res.next();
                    QtyMax = res.getInt(1);
                    if (QtyCurW < QtyMax) {
                        System.out.println("UPDATE `group_work`.`job` SET `Status`='hiring' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Status`='hiring' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                        preparedStatement.executeUpdate();
                    } else if (QtyCurW == QtyMax) {
                        System.out.println("UPDATE `group_work`.`job` SET `Status`='full' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Status`='full' WHERE `job`.`ID_Job` = '" + ID_Job[count] + "'");
                        preparedStatement.executeUpdate();
                    }
                }

                count++;
                System.out.println("--------------------------------------------");
                System.out.println("");
            }
            System.out.println("end");
        } catch (SQLException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCashIn() {
        try {

            int count = 0;
            System.out.println("SELECT count(ID_Job) FROM Job WHERE Status ='vote' ");
            res = statement.executeQuery("SELECT count(ID_Job) FROM Job WHERE Status ='vote' ");
            if (res.next()) {
                count = res.getInt(1);
            }
            //String[] Job_time = new String[count];
            String[] ID_Job = new String[count];
            System.out.println("length : "+ID_Job.length);

            count = 0;
            System.out.println("SELECT ID_Job FROM job WHERE Status ='vote' ");
            res = statement.executeQuery("SELECT ID_Job FROM job WHERE Status ='vote' ");
            while (count < ID_Job.length) {
                res.next();
                ID_Job[count] = res.getString(1);
                //Job_time[count] = res.getString(2);
                count++;
            }
            count = 0;
            int count_member = 0;
            while (count < ID_Job.length) {
                int QtyDown = 0, QtyUp = 0, QtyCurW = 0, QtyVote = 0;
                //cek if vote and down vote diplus sama dengan current worker
                System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job[count] + "' ");
                res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job[count] + "'");
                res.next();
                QtyCurW = res.getInt(1);

                System.out.println("SELECT UpVote FROM job WHERE ID_Job ='" + ID_Job[count] + "' ");
                res = statement.executeQuery("SELECT UpVote FROM job WHERE ID_Job ='" + ID_Job[count] + "'");
                res.next();
                QtyUp = res.getInt(1);

                System.out.println("SELECT DownVote FROM job WHERE ID_Job ='" + ID_Job[count] + "' ");
                res = statement.executeQuery("SELECT DownVote FROM job WHERE ID_Job ='" + ID_Job[count] + "'");
                res.next();
                QtyDown = res.getInt(1);

                QtyVote = QtyDown + QtyUp;
                if (QtyVote == QtyCurW) {
                    System.out.println("Cash in for: "+ID_Job[count]);
                    //cara dapat member
                    System.out.println("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job[count] + "' AND Role ='Member' ");
                    res = statement.executeQuery("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job[count] + "' AND Role ='Member' ");
                    if (res.next()) {
                        count_member = res.getInt(1);
                    }
                    System.out.println("Count Member : " + count_member);

                    String[] member = new String[count_member];
                    int i = 0;
                    System.out.println("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job[count] + "' AND Role ='Member' ");
                    res = statement.executeQuery("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job[count] + "' AND Role ='Member' ");
                    while (res.next()) {
                        member[i] = res.getString(1);
                        System.out.println("Member : " + i + ". " + member[i]);
                        i++;
                        System.out.println("i : " + i);
                    }
                    System.out.println("member length : " + member.length);
                    //FuncGetReward(member);
                } else {
                    System.out.println("failed Cash in , someone didnt vote in : "+ID_Job[count]);
                    //printStream.println("Failed Cash in , someone didnt vote");
                }
                count++;
            }
            System.out.println("end");

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
