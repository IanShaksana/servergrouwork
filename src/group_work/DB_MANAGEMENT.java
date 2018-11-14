/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group_work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Adrian
 */
public class DB_MANAGEMENT implements Runnable {

    Connection localconnection_thread;
    Statement statement;
    DateFormat df;
    DateFormat df2, df3;
    ResultSet res;
    PreparedStatement preparedStatement;
    Date SysDate;

    public DB_MANAGEMENT(Connection connection) {
        try {
            localconnection_thread = connection;
            df = new SimpleDateFormat("yyyy-MM-dd");
            df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df2 = new SimpleDateFormat("HH:mm");
            statement = localconnection_thread.createStatement();

        } catch (SQLException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //
                SysDate = new Date();
                Date now = df2.parse(df2.format(SysDate));
                System.out.println(SysDate);
                if (now.equals(df2.parse("18:41"))) {
                    System.out.println("LOLLLLL");
                    ReminderDueDate_Job();
                    ReminderDueDate_Task();
                }

                System.out.println("this is task");
                FuncCheckDueDate_Task();

                System.out.println("this is job");
                FuncCheckDueDate_Job();

                //
                //System.out.println("this is cashin");
                //FuncCashIn();
            } catch (ParseException ex) {
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
                String Sys_Date = df3.format(DATE);
                Date Task_Date = df3.parse(Task_time[count]);
                String Print_task_Date = df3.format(Task_Date);
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
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ReminderDueDate_Task() {
        try {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Reminder Task");
            int count = 0;
            System.out.println("SELECT count(Due_Time) FROM task WHERE Completed ='no' AND ID_User IS NOT NULL ");
            res = statement.executeQuery("SELECT count(Due_Time) FROM task WHERE Completed ='no' AND ID_User IS NOT NULL ");
            if (res.next()) {
                count = res.getInt(1);
                System.out.println("count task : "+count);
            }
            String[] Task_time = new String[count];
            String[] ID_Task = new String[count];
            String[] User = new String[count];
            String[] Completed_time = new String[count];

            count = 0;
            System.out.println("SELECT ID_Task,Due_Time,ID_User,Date_Completed FROM task WHERE Completed ='no' AND ID_User IS NOT NULL ");
            res = statement.executeQuery("SELECT ID_Task,Due_Time,ID_User,Date_Completed FROM task WHERE Completed ='no' AND ID_User IS NOT NULL ");
            while (count < Task_time.length) {
                res.next();
                ID_Task[count] = res.getString(1);
                Task_time[count] = res.getString(2);
                User[count] = res.getString(3);
                Completed_time[count] = res.getString(4);
                if (Completed_time[count]!=null) {
                    System.out.println("--------------------------------------------");
                    Date DATE = new Date();

                    Date Task_Date = df3.parse(Task_time[count]);
                    Date Date_COM = df3.parse(Completed_time[count]);

                    long diff = Task_Date.getTime() - Date_COM.getTime();
                    diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                    String Print_task_Date = df3.format(Task_Date);
                    String Sys_Date = df3.format(DATE);
                    System.out.println("This is for task ID : " + ID_Task[count]);
                    System.out.println("Date now: " + Sys_Date);
                    System.out.println("Task date: " + Print_task_Date);

                    postData("task", User[count], diff, ID_Task[count]);

                    count++;
                    System.out.println("--------------------------------------------");
                    System.out.println("");
                }
                count++;
            }
            System.out.println("end");
            System.out.println("---------------------------------------------------------------------");
        } catch (SQLException | ParseException ex) {
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
                String Sys_Date = df3.format(DATE);
                Date Task_Date = df3.parse(Job_time[count]);
                String Print_task_Date = df3.format(Task_Date);
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
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ReminderDueDate_Job() {
        try {
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Reminder Job");
            String duetime = "";
            int count = 0;
            System.out.println("SELECT count(Due_Time) FROM Job WHERE Finished ='no' ");
            res = statement.executeQuery("SELECT count(Due_Time) FROM Job WHERE Finished ='no' ");
            if (res.next()) {
                count = res.getInt(1);
            }
            String[] Job_time = new String[count];
            String[] ID_Job = new String[count];
            String[] Owner = new String[count];

            count = 0;
            System.out.println("SELECT ID_Job,Due_Time, Owner FROM job WHERE Finished ='no' ");
            res = statement.executeQuery("SELECT ID_Job,Due_Time, Owner FROM job WHERE Finished ='no' ");
            while (count < Job_time.length) {
                res.next();
                System.out.println(res.getString(2));
                ID_Job[count] = res.getString(1);
                Job_time[count] = res.getString(2);
                Owner[count] = res.getString(3);
                count++;
            }

            count = 0;
            while (count < ID_Job.length) {
                System.out.println("--------------------------------------------");
                Date DATE = new Date();
                String Sys_Date = df3.format(DATE);
                //String Print_task_Date = df3.format("2018-11-30 06:30:00");

                Date Job_Date = df3.parse(Job_time[count]);
                Date Now_Date = df3.parse(Sys_Date);

                System.out.println("This is for JOB ID : " + ID_Job[count]);
                //System.out.println("Date now: " + Sys_Date);
                //System.out.println("Task date: " + Print_task_Date);

                long diff = Job_Date.getTime() - Now_Date.getTime();
                diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                System.err.println("remaining : " + diff);

                postData("job", Owner[count], diff, ID_Job[count]);

                count++;
                System.out.println("--------------------------------------------");
                System.out.println("");
            }
            System.out.println("end");
            System.out.println("---------------------------------------------------------------------");
        } catch (SQLException | ParseException ex) {
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
            System.out.println("length : " + ID_Job.length);

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
                    System.out.println("Cash in for: " + ID_Job[count]);
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
                    System.out.println("failed Cash in , someone didnt vote in : " + ID_Job[count]);
                    //printStream.println("Failed Cash in , someone didnt vote");
                }
                count++;
            }
            System.out.println("end");

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void postData(String type, String user, long remaining, String task_job) {
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            //Create data to send to server
            JSONObject dataToSend = new JSONObject();
            JSONObject dataToSend2 = new JSONObject();
            switch (type) {
                case "task":
                    dataToSend2.put("body", task_job + " time remaining : " + remaining);
                    dataToSend2.put("title", "TASK REMINDER");
                    dataToSend.put("notification", dataToSend2);
                    dataToSend.put("to", "/topics/" + user);
                    break;
                case "job":
                    dataToSend2.put("body", task_job + " time remaining : " + remaining);
                    dataToSend2.put("title", "JOB REMINDER");
                    dataToSend.put("notification", dataToSend2);
                    dataToSend.put("to", "/topics/" + user);
                    break;
            }

            //Initialize and config request, then connect to server
            String urlpath = "https://fcm.googleapis.com/fcm/send";
            URL url = new URL(urlpath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // enable output (body data)
            urlConnection.setRequestProperty("Content-Type", "application/json"); // set header
            urlConnection.setRequestProperty("Authorization", "key=AAAAQ2NNxbo:APA91bFyfuWOeFmqz2GgvsIWJ6ZGO9rRt3y_ZvgIgU3qYy4idI08McSF7qcAKaxDJUc8xU1SrWWzQijADNQwwD3b8-JdcPjqQp33vvG14eq-kkvagesmbizf-JNI6I4uDrQa7JvZZFqU"); // set header
            urlConnection.connect();

            //Write data into server
            OutputStream outputStream = urlConnection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(dataToSend.toString());
            bufferedWriter.flush();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(DB_MANAGEMENT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("result = " + result.toString());
    }

}
