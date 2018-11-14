package group_work;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

public class Thread_Server implements Runnable {

    File fileloc;
    String folderGroup;
    String memberGroup;
    BufferedReader bufferedReader;
    PrintStream printStream;
    DataInputStream dataInputStream;
    FileOutputStream fileOutputStream;
    Socket localsocket_thread;
    Connection localconnection_thread;
    Statement statement;
    PreparedStatement preparedStatement;
    ResultSet res;
    DateFormat df;
    DateFormat df2;
    Firestore db;
    int i;

    public Thread_Server(Socket socket, Connection connection, Firestore db) {
        try {
            this.db = db;
            localsocket_thread = socket;
            localconnection_thread = connection;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            df = new SimpleDateFormat("yyyy-MM-dd");
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            statement = localconnection_thread.createStatement();
            i = 0;
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Creating Thread");

            String fromClient = bufferedReader.readLine();
            System.out.println("Request From Client : " + fromClient);
            String temporary = fromClient;
            String[] data = fromClient.split("-");

            //SCB
            switch (data[0]) {
                case "login":
                    FuncLogin(data);
                    break;
                case "request_home":
                    FuncHome(data);
                    break;
                case "request_job_offer":
                    //FuncJobOffer2(data);
                    FuncJobOffer(data);
                    break;
                case "request_job_task":
                    FuncJobTask1(data);
                    break;
                case "request_job_task2":
                    FuncJobTask2(data);
                    //FuncJobTask22(data);
                    break;
                case "request_job_task3":
                    FuncJobTask3(data);
                    break;
                case "request_apply_task":
                    FuncOldApply(data);
                    break;
                case "create_job":
                    //FuncCreateJob(data, temporary);
                    //FuncCreateJob2(data, temporary);
                    FuncCreateJob3(data, temporary);
                    break;
                case "request_myjob_worker":
                    FuncRequestWorkerJob(data);
                    break;
                case "request_worker_task":
                    FuncRequestWorkerTask(data);
                    break;
                case "complete_task":
                    FuncCompletetask(data);
                    break;
                case "abandon_task":
                    FuncAbandonTask(data);
                    break;
                case "approve_yes":
                    FuncApproveYes(data);
                    break;
                case "approve_no":
                    FuncApproveNo(data);
                    break;
                case "Delete_task":
                    FuncDeleteTask(data);
                    break;
                case "request_myjob_leader":
                    FuncRequestLeaderJob(data);
                    //FuncRequestLeaderJob2(data);
                    break;
                case "create_task":
                    //FuncCreateTask(data, temporary);
                    FuncCreateTask2(data, temporary);
                    break;
                case "request_worker":
                    FuncViewWorker(data);
                    break;
                case "invite_worker":
                    FuncInviteWorker(data);
                    break;
                case "remove_worker":
                    FuncRemoveWorker(data);
                    break;
                case "finish_job":
                    FuncGetFinish(data);
                    break;
                case "image":
                    System.out.println("This is image");
                    printStream.println("Success");
                    break;
                case "request_user":
                    FuncUser();
                    break;
                case "get_message_in":
                    FuncGetMessage_in(data);
                    break;
                case "get_message_out":
                    FuncGetMessage_out(data);
                    break;
                case "get_message_conf":
                    FuncGetMessage_conf(data);
                    break;
                case "reply_message":
                    FuncReply(data);
                    break;
                case "apply_req":
                    FuncMessage(data, "apply_request");
                    break;
                case "msg_assign":
                    FuncMessage(data, "assign_permission");
                    break;
                case "msg_invite_worker":
                    FuncMessage(data, "invite_permission");
                    break;
                case "request_detail_task":
                    FuncDetailTask(data);
                    break;
                case "abandon_group":
                    FuncAbandonGroup(data);
                    break;
                case "cashIn":
                    FuncCashIn(data);
                    break;
                case "cek_finished":
                    FuncFIN(data);
                    //FuncFIN2(data);
                    break;
                case "postorno":
                    FuncStartJob(data);
                    break;
                case "upload_img_nm":
                    FuncCompletetask2(data);
                    break;
                case "req_prove":
                    FuncReqProve(data);
                    break;
                default:
                    printStream.println("Unknown");
                    System.out.println("Unknown");
                    break;
            }
            System.out.println("Closing Thread, closed ?: " + localsocket_thread.isClosed());
            localsocket_thread.close();
            System.out.println("Thread closed, closed ?: " + localsocket_thread.isClosed());
        } catch (IOException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //SF
    public void FuncLogin(String[] data) {
        try {
            System.out.println("This is login request");
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(data[2].getBytes("utf8"));
            String sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
            System.out.println("SELECT ID_User from user WHERE "
                    + "`pass`='" + sha1 + "' AND "
                    + "`ID_User`='" + data[1] + "' ");
            res = statement.executeQuery("SELECT ID_User from user WHERE "
                    + "`pass`='" + sha1 + "' AND "
                    + "`ID_User`='" + data[1] + "' ");
            if (res.next()) {
                System.out.println("Login Success");
                printStream.println("Login Success");
                statement.close();
            } else {
                System.out.println("Login Failed");
                printStream.println("Login Failed");
                statement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncHome(String[] data) {
        try {
            System.out.println("Enter Request :" + data[1]);
            System.out.println("SELECT Experience, Strength, Dexterity, Intelligence, Nxt_Experience FROM user WHERE ID_User = '" + data[1] + "'");
            res = statement.executeQuery("SELECT Experience, Strength, Dexterity, Intelligence, Nxt_Experience FROM user WHERE ID_User = '" + data[1] + "'");
            res.next();
            String request_home = data[1] + "-" + res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(5);
            System.out.println(request_home);
            printStream.println(request_home);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncFIN(String[] data) {
        String Condition = "";
        try {
            System.out.println("SELECT Finished FROM job WHERE ID_Job ='" + data[1] + "'");
            res = statement.executeQuery("SELECT Finished FROM job WHERE ID_Job ='" + data[1] + "'");
            res.next();
            Condition = res.getString(1);
            if (Condition.equals("yes")) {
                printStream.println("finished");
            } else {
                printStream.println("not finished");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncFIN2(String[] data) {
        String Condition = "";
        try {
            System.out.println("SELECT Status FROM job2 WHERE ID_Job ='" + data[1] + "'");
            res = statement.executeQuery("SELECT Status FROM job2 WHERE ID_Job ='" + data[1] + "'");
            res.next();
            Condition = res.getString(1);
            if (Condition.equals("finished")) {
                printStream.println("finished");
            } else {
                printStream.println("not finished");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Halaman OFFER
    public void FuncJobOffer(String[] data) {
        try {
            System.out.println("This is request job offer");
            res = statement.executeQuery("SELECT Name,Description,Owner, ID_Job from job where Finished ='no'");
            String append;
            String appendix = "";
            int i = 0;
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1) + "," + res.getString(4);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;

            }

            if (appendix.equals("")) {
                printStream.println("failed no job");
                statement.close();
            } else {
                System.out.println("Sending :" + appendix);
                printStream.println(appendix);
                statement.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncJobOffer2(String[] data) {
        try {
            System.out.println("This is request job offer");
            res = statement.executeQuery("SELECT Name,Description,Owner, ID_Job from job2 where Status ='startH'");
            String append;
            String appendix = "";
            int i = 0;
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1) + "," + res.getString(4);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;

            }

            if (appendix.equals("")) {
                printStream.println("failed no job");
                statement.close();
            } else {
                System.out.println("Sending :" + appendix);
                printStream.println(appendix);
                statement.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //MINTA TASK DARI JOB TERTENTU
    public void FuncJobTask1(String[] data) {
        try {
            System.out.println("This is request job task for individual");
            System.out.println("Enter Request :" + data[1]);
            statement = localconnection_thread.createStatement();

            //cek if job still exist
            //res = statement.executeQuery("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Job =(SELECT ID_Job FROM job WHERE Name ='" + data[1] + "')");
            res = statement.executeQuery("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Job ='" + data[1] + "' AND Finished = 'no'");
            String list = "";

            i = 0;
            while (res.next()) {
                System.out.println("result Name: " + res.getString(1));
                System.out.println("result Desc: " + res.getString(2));
                System.out.println("result Type: " + res.getString(3));
                System.out.println("result Diff: " + res.getString(4));
                System.out.println("result IDJO: " + res.getString(5));
                System.out.println("result COMP: " + res.getString(6));
                System.out.println("result IDTA: " + res.getString(7));
                System.out.println("result WORK: " + res.getString(8));
                System.out.println("result Time: " + res.getString(9));

                if (i == 0) {
                    list = list;
                } else {
                    list = list + "-LIST-";
                }
                list = list + res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(5) + "-" + res.getString(6) + "-" + res.getString(7) + "-" + res.getString(8) + "-," + res.getString(9);
                i++;

            }
            System.out.println("Sending :" + list);
            if (list.equals("")) {
                System.out.println("failed No Task");
                printStream.println("failed No Task");
            } else {
                printStream.println(list);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncJobTask2(String[] data) {
        try {
            System.out.println("This is request job task for offer");
            System.out.println("Enter Request :" + data[1]);
            statement = localconnection_thread.createStatement();

            //cek if job still exist
            System.out.println("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Job ='" + data[1] + "' AND ID_User IS NULL  AND Finished = 'no'");
            res = statement.executeQuery("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Job ='" + data[1] + "' AND ID_User IS NULL  AND Finished = 'no'");
            String list2 = "";

            i = 0;
            while (res.next()) {
                System.out.println("result Name: " + res.getString(1));
                System.out.println("result Desc: " + res.getString(2));
                System.out.println("result Type: " + res.getString(3));
                System.out.println("result Diff: " + res.getString(4));
                System.out.println("result IDJO: " + res.getString(5));
                System.out.println("result COMP: " + res.getString(6));
                System.out.println("result IDTA: " + res.getString(7));
                System.out.println("result WORK: " + res.getString(8));
                System.out.println("result Time: " + res.getString(9));

                if (i == 0) {
                    list2 = list2;
                } else {
                    list2 = list2 + "-LIST-";
                }
                list2 = list2 + res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(5) + "-" + res.getString(6) + "-" + res.getString(7) + "-" + res.getString(8) + "-," + res.getString(9);
                i++;

            }
            System.out.println("Sending :" + list2);
            if (list2.equals("")) {
                System.out.println("failed No Task");
                printStream.println("failed No Task");
            } else {
                printStream.println(list2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncJobTask22(String[] data) {
        try {
            System.out.println("This is request job task for offer");
            System.out.println("Enter Request :" + data[1]);
            statement = localconnection_thread.createStatement();

            //cek if job still exist
            System.out.println("SELECT Name,Description,Type,Difficulty,ID_Job,ID_Task,ID_User FROM task2 WHERE ID_Job ='" + data[1] + "' AND ID_User IS NULL  AND Status = 'no'");
            res = statement.executeQuery("SELECT Name,Description,Type,Difficulty,ID_Job,ID_Task,ID_User FROM task2 WHERE ID_Job ='" + data[1] + "' AND ID_User IS NULL  AND Status = 'no'");
            String list2 = "";

            i = 0;
            while (res.next()) {
                System.out.println("result Name: " + res.getString(1));
                System.out.println("result Desc: " + res.getString(2));
                System.out.println("result Type: " + res.getString(3));
                System.out.println("result Diff: " + res.getString(4));
                System.out.println("result IDJO: " + res.getString(5));
                System.out.println("result COMP: " + res.getString(6));
                System.out.println("result IDTA: " + res.getString(7));
                System.out.println("result WORK: " + res.getString(8));
                System.out.println("result Time: " + res.getString(9));

                if (i == 0) {
                    list2 = list2;
                } else {
                    list2 = list2 + "-LIST-";
                }
                list2 = list2 + res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(5) + "-" + res.getString(6) + "-" + res.getString(7) + "-" + res.getString(8) + "-," + res.getString(9);
                i++;

            }
            System.out.println("Sending :" + list2);
            if (list2.equals("")) {
                System.out.println("failed No Task");
                printStream.println("failed No Task");
            } else {
                printStream.println(list2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncJobTask3(String[] data) {
        try {
            System.out.println("This is request job task for worker");
            System.out.println("Enter Request :" + data[1]);

            //cek if job still in
            System.out.println("SELECT ID_Job,ID_Task,Name, Description,Difficulty, Type, Due_Time, Completed FROM task WHERE ID_Job ='" + data[1] + "' AND ID_User='" + data[2] + "'  AND Finished = 'no'");
            res = statement.executeQuery("SELECT ID_Job,ID_Task,Name, Description,Difficulty, Type, Due_Time, Completed FROM task WHERE ID_Job ='" + data[1] + "' AND ID_User='" + data[2] + "'  AND Finished = 'no'");

            String list2 = "";

            i = 0;
            while (res.next()) {
                System.out.println("result Name: " + res.getString(3));
                System.out.println("result Desc: " + res.getString(4));
                System.out.println("result Type: " + res.getString(6));
                System.out.println("result Diff: " + res.getString(5));
                System.out.println("result IDJO: " + res.getString(1));
                System.out.println("result COMP: " + res.getString(8));
                System.out.println("result IDTA: " + res.getString(2));
                System.out.println("result Time: " + res.getString(7));

                if (i == 0) {
                    list2 = list2;
                } else {
                    list2 = list2 + "-LIST-";
                }
                list2 = list2 + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(6) + "-" + res.getString(5) + "-" + res.getString(1) + "-" + res.getString(8) + "-" + res.getString(2) + "-," + res.getString(7);
                i++;

            }
            System.out.println("Sending :" + list2);
            if (list2.equals("")) {
                System.out.println("failed No Task");
                printStream.println("failed No Task");
            } else {
                printStream.println(list2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //MINTA TASK DARI JOB TERTENTU

    //Halaman LIST
    public void FuncCreateJob(String[] data, String temporary) {
        try {
            System.out.println("This is request create job and group");
            System.out.println(temporary);
            String[] date = temporary.split(",");
            System.out.println("Job Name  : " + data[1]);
            System.out.println("Job Desc  : " + data[2]);
            System.out.println("Job Owner : " + data[3]);
            System.out.println("Due Date  : " + date[1]);
            System.out.println("Due Time  : " + date[2]);

            Date SysDate = new Date();
            Date jobdate = df.parse(date[1]);
            if (jobdate.compareTo(SysDate) > 0) {
                System.out.println("after");
                String id_supplement = Long.toString(System.currentTimeMillis());
                String ID_job = data[1] + "|" + id_supplement;
                String tanggal = df2.format(SysDate);
                System.out.println("INSERT INTO `group_work`.`job` (`ID_Job`, `Name`, `Description`, `Owner`, `Due_Time`,`Finished`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) VALUES ('" + ID_job + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + date[1] + " " + date[2] + "','no','" + tanggal + "', '3','0','0','0', 'hiring')");
                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`job` (`ID_Job`, `Name`, `Description`, `Owner`, `Due_Time`,`Finished`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) VALUES ('" + ID_job + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + date[1] + " " + date[2] + "','no','" + tanggal + "', '3','0','0','0', 'hiring')");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("Create job done");
                    statement.close();
                } catch (Exception e) {
                    System.out.println("Create job failed");
                    printStream.println("Create job failed Due date cant be same or before");
                    statement.close();
                }

                System.out.println("INSERT INTO `group_work`.`grouping` (`ID_User`, `ID_Job`, `Role`, `Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_User`, `ID_Job`, `Role`,`Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("Create group done");
                    printStream.println("succes");
                    statement.close();
                } catch (Exception e) {
                    System.out.println("Create group failed");
                    printStream.println("Create group failed");
                    statement.close();
                }
            } else if (jobdate.compareTo(SysDate) < 0) {
                System.out.println("before");
                System.out.println("Create job failed Due date cant be same or before");
                printStream.println("Create job failed Due date cant be same or before");
                statement.close();
            } else if (jobdate.compareTo(SysDate) == 0) {
                System.out.println("same");
                System.out.println("Create job failed Due date cant be same or before");
                printStream.println("Create job failed Due date cant be same or before");
                statement.close();
            }
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCreateJob2(String[] data, String temporary) {
        try {
            System.out.println("This is request create job and group");
            //System.out.println(temporary);
            //String[] date = temporary.split(",");
            System.out.println("Job Name  : " + data[1]);
            System.out.println("Job Desc  : " + data[2]);
            System.out.println("Job Owner : " + data[3]);
            System.out.println("Slot      : " + data[5]);
            System.out.println("Due Date  : " + data[4]);
            //System.out.println("Due Time  : " + date[2]);

            Date SysDate = new Date();
            String id_supplement = Long.toString(System.currentTimeMillis());
            String ID_job = data[1] + "|" + id_supplement;
            String tanggal = df2.format(SysDate);
            System.out.println("INSERT INTO `group_work`.`job2` (`ID_Job`, `Name`, `Description`, `Owner`, `Day_R`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) "
                    + "VALUES ("
                    + "'" + ID_job + "', "
                    + "'" + data[1] + "', "
                    + "'" + data[2] + "', "
                    + "'" + data[3] + "', "
                    + "'" + data[4] + "', "
                    + "'" + tanggal + "', "
                    + "'" + data[5] + "',"
                    + "'0','0','0', "
                    + "'hiring')");
            preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`job2` (`ID_Job`, `Name`, `Description`, `Owner`, `Day_R`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) "
                    + "VALUES ("
                    + "'" + ID_job + "', "
                    + "'" + data[1] + "', "
                    + "'" + data[2] + "', "
                    + "'" + data[3] + "', "
                    + "'" + data[4] + "', "
                    + "'" + tanggal + "', "
                    + "'" + data[5] + "',"
                    + "'0','0','0', "
                    + "'hiring')");
            try {
                preparedStatement.executeUpdate();
                System.out.println("Create job done");
                statement.close();
            } catch (Exception e) {
                System.out.println("Create job failed");
                printStream.println("Create job failed Due date cant be same or before");
                statement.close();
            }

            System.out.println("INSERT INTO `group_work`.`grouping2` (`ID_User`, `ID_Job`, `Role`, `Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
            preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping2` (`ID_User`, `ID_Job`, `Role`,`Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
            try {
                preparedStatement.executeUpdate();
                System.out.println("Create group done");
                printStream.println("succes");
                statement.close();
            } catch (Exception e) {
                System.out.println("Create group failed");
                printStream.println("Create group failed");
                statement.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCreateJob3(String[] data, String temporary) {
        try {
            System.out.println("This is request create job and group");
            System.out.println(temporary);
            String[] date = temporary.split(",");
            System.out.println("Job Name  : " + data[1]);
            System.out.println("Job Desc  : " + data[2]);
            System.out.println("Job Owner : " + data[3]);
            System.out.println("Max : " + data[4]);
            System.out.println("Due Date  : " + date[1]);
            System.out.println("Due Time  : " + date[2]);

            Date SysDate = new Date();
            Date jobdate = df.parse(date[1]);
            if (jobdate.compareTo(SysDate) > 0) {
                System.out.println("after");
                String id_supplement = Long.toString(System.currentTimeMillis());
                String ID_job = data[1] + "|" + id_supplement;
                String tanggal = df2.format(SysDate);
                System.out.println("INSERT INTO `group_work`.`job` (`ID_Job`, `Name`, `Description`, `Owner`, `Due_Time`,`Finished`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) VALUES ('" + ID_job + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + date[1] + " " + date[2] + "','no','" + tanggal + "', '" + data[4] + "','0','0','0', 'hiring')");
                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`job` (`ID_Job`, `Name`, `Description`, `Owner`, `Due_Time`,`Finished`,`Date_Created`,`Max_Worker`,`Current_Worker`,`UpVote`,`DownVote`,`Status`) VALUES ('" + ID_job + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + date[1] + " " + date[2] + "','no','" + tanggal + "', '" + data[4] + "','0','0','0', 'hiring')");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("Create job done");
                    statement.close();
                } catch (Exception e) {
                    System.out.println("Create job failed");
                    printStream.println("Create job failed Due date cant be same or before");
                    statement.close();
                }

                System.out.println("INSERT INTO `group_work`.`grouping` (`ID_User`, `ID_Job`, `Role`, `Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_User`, `ID_Job`, `Role`,`Finished`) VALUES ('" + data[3] + "','" + ID_job + "', 'Leader','no')");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("Create group done");
                    printStream.println(ID_job);
                    postFB("create_job", "List_Job/" + ID_job, data, date);
                    statement.close();
                } catch (Exception e) {
                    System.out.println("Create group failed");
                    System.out.println(e);
                    printStream.println("Create group failed");
                    statement.close();
                }
            } else if (jobdate.compareTo(SysDate) < 0) {
                System.out.println("before");
                System.out.println("Create job failed Due date cant be same or before");
                printStream.println("Create job failed Due date cant be same or before");
                statement.close();
            } else if (jobdate.compareTo(SysDate) == 0) {
                System.out.println("same");
                System.out.println("Create job failed Due date cant be same or before");
                printStream.println("Create job failed Due date cant be same or before");
                statement.close();
            }
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncOldApply(String[] data) {
        try {
            String ID_Task,
                    ID_Job;
            String condition1 = "",
                    condition2 = "",
                    condition3 = "",
                    choosenID_Task = data[2];
            System.out.println("This is request apply task");
            //res = statement.executeQuery("SELECT Level,Experience,Leader_Poin,Strength,Dexterity,Intelligence FROM user WHERE ID_User ='" + data[1] + "'");
            // cek if task exist
            System.out.println("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
            res = statement.executeQuery("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
            if (res.next()) {
                condition1 = "sukses";
            } else {
                condition1 = "fail";
            }
            System.out.println("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
            res = statement.executeQuery("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
            if (res.next()) {
                condition2 = "sukses";
            } else {
                condition2 = "fail";
            }
            // cek if task exist
            // cek if task was own
            System.out.println("SELECT ID_Job FROM job WHERE Owner ='" + data[1] + "'");
            res = statement.executeQuery("SELECT ID_Job FROM job WHERE Owner ='" + data[1] + "'");
            //res.next();
            //System.out.println(""+res.getString(1));
            String[] owned_job = new String[99];
            int numb = 1;
            while (res.next()) {
                System.out.println("" + res.getString(1));
                owned_job[numb] = res.getString(1);
                numb++;
            }
            System.out.println("" + numb);
            int numbSame_job = 1;
            String[] samejob_IDTASK = new String[99];
            while (numb != 0) {
                System.out.println("SELECT ID_Task FROM task WHERE ID_Job ='" + owned_job[numb] + "'");
                res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_Job ='" + owned_job[numb] + "'");
                while (res.next()) {
                    System.out.println("" + res.getString(1));
                    samejob_IDTASK[numbSame_job] = res.getString(1);
                    numbSame_job++;
                }
                numb--;
            }
            System.out.println("" + numbSame_job);
            while (numbSame_job != 0) {
                System.out.println("choosen:" + choosenID_Task);
                System.out.println("owned  :" + samejob_IDTASK[numbSame_job]);
                if (choosenID_Task.equals(samejob_IDTASK[numbSame_job])) {
                    condition3 = "fail";
                    break;
                } else {
                    condition3 = "sukses";
                }
                numbSame_job--;
            }
            //cek if task was own
            System.out.println("con1: " + condition1);
            System.out.println("con2: " + condition2);
            System.out.println("con3: " + condition3);
            if (condition1.equals("sukses") && condition2.equals("sukses") && condition3.equals("sukses")) {

                //FuncMessage(data, "apply_request");
                //nope harus ada notifikasi
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = '" + data[1] + "' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                System.out.println("UPDATE `group_work`.`task` SET `ID_User` = '" + data[1] + "' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("success apply");
                } catch (Exception e) {
                    System.out.println("failed apply");
                    printStream.println("failed");
                }

                //cek if already joined
                System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[3] + "' AND Role='Member'");
                res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[3] + "' AND Role='Member'");
                if (res.next()) {
                    System.out.println("Already joined job");
                    printStream.println("success");
                    preparedStatement.close();
                } else {
                    System.out.println("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[1] + "', '" + data[3] + "', 'Member');");
                    preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[1] + "', '" + data[3] + "', 'Member');");

                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("success join job");
                        printStream.println("success");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("failed join job");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
                }

            } else {
                System.out.println("failed too late");
                printStream.println("failed too late");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //Halaman LIST    

    //Previlege dari WORKER
    public void FuncRequestWorkerTask(String[] data) {
        try {
            System.out.println("This is request myjob worker");
            String ID_joblist = "";
            String ID_tasklist = "";
            String name_list = "";
            String desc_list = "";
            String diff_list = "";
            String type_list = "";
            String due_date_list = "";
            //String due_time_list = "";
            String approved_list = "";

            i = 0;
            statement = localconnection_thread.createStatement();
            System.out.println("SELECT ID_Job,ID_Task, Name, Description,Difficulty, Type, Due_Time, Completed FROM task WHERE ID_User='" + data[1] + "' AND Finished = 'no' AND ID_Job='" + data[2] + "'");
            res = statement.executeQuery("SELECT ID_Job,ID_Task,Name, Description,Difficulty, Type, Due_Time, Completed FROM task WHERE ID_User='" + data[1] + "' AND Finished = 'no' AND ID_Job='" + data[2] + "'");

            while (res.next()) {
                if (i == 0) {
                    ID_joblist = res.getString(1);
                    ID_tasklist = res.getString(2);
                    name_list = res.getString(3);
                    desc_list = res.getString(4);
                    diff_list = res.getString(5);
                    type_list = res.getString(6);
                    due_date_list = res.getString(7);
                    approved_list = res.getString(8);
                } else {
                    ID_joblist = ID_joblist + "," + res.getString(1);
                    ID_tasklist = ID_tasklist + "," + res.getString(2);
                    name_list = name_list + "," + res.getString(3);
                    desc_list = desc_list + "," + res.getString(4);
                    diff_list = diff_list + "," + res.getString(5);
                    type_list = type_list + "," + res.getString(6);
                    due_date_list = due_date_list + "," + res.getString(7);
                    approved_list = approved_list + "," + res.getString(8);
                }
                i++;
            }
            System.out.println(ID_joblist);
            System.out.println(ID_tasklist);
            System.out.println(name_list);
            System.out.println(desc_list);
            System.out.println(diff_list);
            System.out.println(type_list);
            System.out.println(due_date_list);
            System.out.println(approved_list);
            statement = localconnection_thread.createStatement();
            String[] id_job_list = ID_joblist.split(",");
            String[] id_task_list = ID_tasklist.split(",");
            String[] id_desc_list = desc_list.split(",");
            String[] id_diff_list = diff_list.split(",");
            String[] id_type_list = type_list.split(",");
            String[] id_duetime_list = due_date_list.split(",");
            String[] id_approved_list = approved_list.split(",");
            String[] id_name_list = name_list.split(",");

            String send = "";

            int i2 = 0;

            while (i != 0) {
                res = statement.executeQuery("SELECT Name FROM job WHERE ID_Job='" + id_job_list[i - 1] + "' AND Finished = 'no'");
                System.out.println("SELECT Name FROM job WHERE ID_Job='" + id_job_list[i - 1] + "' AND Finished = 'no'");
                System.out.println("ID JOB : " + id_job_list[i - 1]);
                System.out.println("ID TAS : " + id_task_list[i - 1]);
                System.out.println("Name   : " + id_name_list[i - 1]);
                System.out.println("Desc   : " + id_desc_list[i - 1]);
                System.out.println("Diff   : " + id_diff_list[i - 1]);
                System.out.println("Type   : " + id_type_list[i - 1]);
                System.out.println("Time   : " + id_duetime_list[i - 1]);
                System.out.println("Approvd: " + id_approved_list[i - 1]);
                res.next();
                if (i2 == 0) {
                    send = res.getString(1) + "-" + id_job_list[i - 1] + "-" + id_task_list[i - 1] + "-" + id_name_list[i - 1] + "-" + id_desc_list[i - 1] + "-" + id_diff_list[i - 1] + "-" + id_type_list[i - 1] + "-" + id_approved_list[i - 1] + "-," + id_duetime_list[i - 1];
                    i2++;
                } else {
                    send = send + "-list-" + res.getString(1) + "-" + id_job_list[i - 1] + "-" + id_task_list[i - 1] + "-" + id_name_list[i - 1] + "-" + id_desc_list[i - 1] + "-" + id_diff_list[i - 1] + "-" + id_type_list[i - 1] + "-" + id_approved_list[i - 1] + "-," + id_duetime_list[i - 1];
                }
                i--;
            }
            String[] counte = send.split("-list-");
            //String singletask = getItem(position);

            System.out.println("printing");
            for (int j = 0; j < counte.length; j++) {
                System.out.println(j + " : " + counte[j]);
                String[] splitted_task = counte[j].split("-");
                System.out.println("begin");
                for (int k = 0; k < splitted_task.length; k++) {
                    System.out.println(k + " : " + splitted_task[k]);
                }
                String[] splitted_time = counte[j].split(",");
                if (splitted_time.length < 0) {
                    String[] splitted_time2 = splitted_time[1].split(" ");
                    for (int l = 0; l < splitted_time2.length; l++) {
                        System.out.println(l + " : " + splitted_time2[l]);
                    }
                }

                System.out.println("end");

            }
            System.out.println("close printing");
            if (ID_joblist.equals("")) {
                System.out.println("failed no task");
                printStream.println("failed no task");
                statement.close();
            } else {
                System.out.println("Sending : " + send);
                printStream.println(send);
                statement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void FuncRequestWorkerJob(String[] data) {
        try {
            System.out.println("This is request worker job");
            String joblist2 = "";
            i = 0;
            statement = localconnection_thread.createStatement();
            System.out.println("SELECT Name,ID_Job FROM grouping WHERE ID_User='" + data[1] + "' AND Role ='Member' AND Finished = 'no'");
            res = statement.executeQuery("SELECT ID_Job FROM grouping WHERE ID_User='" + data[1] + "' AND Role ='Member' AND Finished = 'no'");
            while (res.next()) {
                if (i == 0) {
                    joblist2 = res.getString(1);
                    i++;
                } else {
                    joblist2 = joblist2 + "-" + res.getString(1);
                }
            }
            if (joblist2.equals("")) {
                System.out.println("failed no job");
                printStream.println("failed no job");
            } else {
                System.out.println(joblist2);
                printStream.println(joblist2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
            printStream.println("failed sql");
        }
    }

    public void FuncCompletetask(String[] data) {
        try {
            System.out.println("This is complete task");
            System.out.println("UPDATE `group_work`.`task` SET `Completed` = 'yes' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Completed` = 'yes' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            //UPDATE `group_work`.`task` SET `Completed` = 'yes' WHERE `task`.`ID_Task` = 'testattack|1528525967118';
            try {
                preparedStatement.executeUpdate();
                System.out.println("Success");
                printStream.println("Success");
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed");
                printStream.println("failed");
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCompletetask2(String[] data) {
        try {
            System.out.println("This is complete task2");
            System.out.println("UPDATE `group_work`.`task` SET `Completed` = 'yes' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Completed` = 'yes', `prove_nm` = '" + data[2] + "' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            //UPDATE `group_work`.`task` SET `Completed` = 'yes' WHERE `task`.`ID_Task` = 'testattack|1528525967118';
            try {
                preparedStatement.executeUpdate();
                System.out.println("Success");
                printStream.println("Success");
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed");
                printStream.println("failed");
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncAbandonTask(String[] data) {
        try {
            System.out.println("This is abandon task");

            //UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_Task` = 'fgbj|1534910572771';
            System.out.println("UPDATE `group_work`.`task` SET `ID_User` = NULL, SET `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_Task` = '" + data[1] + "'");
            try {
                preparedStatement.executeUpdate();
                System.out.println("Success abandon task");
                printStream.println("Success");
                //preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed abandon task");
                printStream.println("failed");
                //preparedStatement.close();
            }

            /*
                    System.out.println("DELETE FROM `grouping` WHERE ID_User = '" + data[3] + "' AND ID_Job ='" + data[2] + "' AND ID_Task ='" + data[1] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `grouping` WHERE ID_User = '" + data[3] + "' AND ID_Job ='" + data[2] + "' AND ID_Task= '" + data[1] + "'");

                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("Success abandon group");
                        printStream.println("Success");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("Failed abandon group");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
             */
            //losses
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncAbandonGroup(String[] data) {
        try {
            int Qty_Cur_w = 0, Qty_Max_w = 0;
            int NumberOfTask = 0;
            int i = 0;
            int a = 0;
            String ID_job = data[1];

            //listing task to update firebase
            String append;
            String appendix = "";

            System.out.println("SELECT ID_Task FROM task WHERE ID_User ='" + data[2] + "'");
            res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_User ='" + data[2] + "'");
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1);
                if (a == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                a++;
            }

            //delete messagenya
            System.out.println("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + data[1] + "' ");
            res = statement.executeQuery("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + data[1] + "' ");
            if (res.next()) {
                NumberOfTask = res.getInt(1);
            }

            String[] ID_Task = new String[NumberOfTask];
            System.out.println("SELECT ID_Task FROM task WHERE ID_Job ='" + data[1] + "' ");
            res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_Job ='" + data[1] + "' ");
            while (res.next()) {
                ID_Task[i] = res.getString(1);
                System.out.println("ID Task [" + i + "] : " + ID_Task[i]);
                i++;
            }

            String owner = "";
            System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + data[1] + "' ");
            res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + data[1] + "' ");
            if (res.next()) {
                owner = res.getString(1);
                System.out.println("owner : " + owner);
            }
            i--;
            String message = "";
            while (i >= 0) {
                message = data[2] + "-assign-" + ID_Task[i];
                System.out.println("DELETE FROM `message` WHERE message = '" + message + "' AND sender ='" + owner + "' ");
                preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `message` WHERE message = '" + message + "' AND sender ='" + owner + "'");
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("Sukses delete : " + message);
                    i--;
                } catch (Exception e) {
                    System.out.println("failed delete : " + message);
                }
            }

            //delete groupingnya
            System.out.println("DELETE FROM `grouping` WHERE ID_User = '" + data[2] + "' AND ID_Job ='" + data[1] + "' AND Role='Member'");
            preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `grouping` WHERE ID_User = '" + data[2] + "' AND ID_Job ='" + data[1] + "' AND Role='Member'");

            try {
                preparedStatement.executeUpdate();
                System.out.println("Success abandon group");
                //printStream.println("Success");
                //preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed abandon group");
                //printStream.println("failed");
                //preparedStatement.close();
            }

            //delete tasknya
            System.out.println("UPDATE `group_work`.`task` SET `ID_User` = NULL, SET `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_User` = '" + data[2] + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_User` = '" + data[2] + "'");
            try {
                preparedStatement.executeUpdate();
                System.out.println("Success abandon all task");
                //printStream.println("Success");
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed abandon all task");
                //printStream.println("failed");
                preparedStatement.close();
            }

            //ta kurangi di groupnya
            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res.next();
            Qty_Cur_w = Integer.parseInt(res.getString(1)) - 1;

            System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "'");
            try {
                preparedStatement.executeUpdate();
                System.out.println(Qty_Cur_w + "-LISTJOB-" + appendix);
                printStream.println(Qty_Cur_w + "-LISTJOB-" + appendix);
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed minus worker");
                printStream.println("failed");
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Previlege dari WORKER

    //Previlege dari LEADER
    public void FuncStartJob(String[] data) {
        try {
            System.out.println("SELECT Status FROM job2 WHERE ID_Job ='" + data[1] + "'");
            res = statement.executeQuery("SELECT Status FROM job2 WHERE ID_Job ='" + data[1] + "'");
            res.next();
            String Condition = res.getString(1);

            switch (Condition) {
                case "created":
                    System.out.println("UPDATE `group_work`.`job2` SET `Status` = 'startH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job2` SET `Status` = 'startH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("Success");
                        printStream.println("Start Hiring");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("Failed");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
                    break;
                case "startH":
                    System.out.println("UPDATE `group_work`.`job2` SET `Status` = 'stopH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job2` SET `Status` = 'stopH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("Success");
                        printStream.println("Stop Hiring");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("Failed");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
                    break;
                case "stopH":
                    System.out.println("UPDATE `group_work`.`job2` SET `Status` = 'startH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job2` SET `Status` = 'startH' WHERE `job2`.`ID_Job` = '" + data[1] + "'");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("Success");
                        printStream.println("Hiring");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("Failed");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncReqProve(String[] data) {
        try {
            System.out.println("SELECT prove_nm FROM task WHERE ID_Task='" + data[1] + "'");
            res = statement.executeQuery("SELECT prove_nm FROM task WHERE ID_Task='" + data[1] + "'");
            res.next();
            String pesan = res.getString(1);

            if (pesan == null) {
                System.out.println("no prove");
                printStream.println("no prove");
            } else {
                System.out.println(res.getString(1));
                printStream.println(res.getString(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void FuncApproveYes(String[] data) {
        try {
            System.out.println("This is approve_yes " + data[2]);
            System.out.println("UPDATE `group_work`.`task` SET `Approved` = 'yes' WHERE `task`.`ID_Task` = '" + data[2] + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Approved` = 'yes' WHERE `task`.`ID_Task` = '" + data[2] + "';");
            preparedStatement.executeUpdate();
            printStream.println("sukses");
            //FuncGetReward(data);
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncApproveNo(String[] data) {
        try {
            System.out.println("This is approve_no " + data[1]);
            System.out.println("UPDATE `group_work`.`task` SET `Approved` = 'no', `Completed` ='no' WHERE `task`.`ID_Task` = '" + data[1] + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Approved`= 'no', `Completed` ='no', `prove_nm` = NULL WHERE `task`.`ID_Task` = '" + data[1] + "';");
            preparedStatement.executeUpdate();
            printStream.println("Success");
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncDeleteTask(String[] data) {
        try {
            System.out.println("This is delete task");
            System.out.println("DELETE FROM `group_work`.`task` WHERE `task`.`ID_Task` = '" + data[1] + "'");
            preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `group_work`.`task` WHERE `task`.`ID_Task` = '" + data[1] + "'");
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncRequestLeaderJob(String[] data) {
        try {
            System.out.println("This is request myjob leader");
            String joblist2 = "";
            i = 0;
            statement = localconnection_thread.createStatement();
            //res = statement.executeQuery("SELECT Name,ID_Job FROM job WHERE Owner='" + data[1] + "' AND Finished='no'");
            res = statement.executeQuery("SELECT Name,ID_Job FROM job WHERE Owner='" + data[1] + "' AND Status != 'end'");
            while (res.next()) {
                if (i == 0) {
                    joblist2 = res.getString(1) + "," + res.getString(2);
                    i++;
                } else {
                    joblist2 = joblist2 + "-" + res.getString(1) + "," + res.getString(2);
                }
            }
            if (joblist2.equals("")) {
                printStream.println("failed no job");
            } else {
                System.out.println(joblist2);
                printStream.println(joblist2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncRequestLeaderJob2(String[] data) {
        try {
            System.out.println("This is request myjob leader");
            String joblist2 = "";
            i = 0;
            statement = localconnection_thread.createStatement();
            //res = statement.executeQuery("SELECT Name,ID_Job FROM job WHERE Owner='" + data[1] + "' AND Finished='no'");
            res = statement.executeQuery("SELECT Name,ID_Job FROM job2 WHERE Owner='" + data[1] + "' AND Status != 'end'");
            while (res.next()) {
                if (i == 0) {
                    joblist2 = res.getString(1) + "," + res.getString(2);
                    i++;
                } else {
                    joblist2 = joblist2 + "-" + res.getString(1) + "," + res.getString(2);
                }
            }
            if (joblist2.equals("")) {
                printStream.println("failed no job");
            } else {
                System.out.println(joblist2);
                printStream.println(joblist2);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCreateTask(String[] data, String temporary) {
        try {
            String ID_job2, JobDate;
            System.out.println("This is create task");
            System.out.println(temporary);
            String[] date2 = temporary.split(",");
            System.out.println("task Name  : " + data[1]);
            System.out.println("task Desc  : " + data[2]);
            System.out.println("task type : " + data[3]);
            System.out.println("task diff : " + data[4]);
            System.out.println("Job name : " + data[5]);
            statement = localconnection_thread.createStatement();
            System.out.println("SELECT ID_Job,Due_Time FROM job WHERE ID_Job ='" + data[5] + "'");
            res = statement.executeQuery("SELECT ID_Job,Due_Time FROM job WHERE ID_Job ='" + data[5] + "'");
            res.next();
            ID_job2 = res.getString(1);
            JobDate = res.getString(2);
            System.out.println("Due Date Job:" + JobDate);
            String[] JobDateSplit = JobDate.split(" ");
            JobDate = JobDateSplit[0];
            statement.close();
            System.out.println("Due Date Job2 : " + JobDate);
            System.out.println("Due Date  : " + date2[1]);
            System.out.println("Due Time  : " + date2[2]);
            Date DateNow = new Date();
            Date test1 = df.parse(JobDate);
            Date test2 = df.parse(date2[1]);
            String id_supplement2 = Long.toString(System.currentTimeMillis());
            if (test1.compareTo(test2) > 0) {
                System.out.println("after");
                if (test2.compareTo(DateNow) > 0) {
                    System.out.println("after");
                    preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`task` (`ID_Task`,`ID_Job`, `Name`, `Description`, `Type`, `Difficulty`, `Due_Time`,`Approved`,`Completed`,`Finished`,`Status`) VALUES ('" + data[1] + "|" + id_supplement2 + "','" + ID_job2 + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + "', '" + date2[1] + " " + date2[2] + "','no','no','no','empty')");
                    System.out.println("INSERT INTO `group_work`.`task` (`ID_Task`,`ID_Job`, `Name`, `Description`, `Type`, `Difficulty`, `Due_Time`,`Approved`,`Completed`,`Finished`,`Status`) VALUES ('" + data[1] + "|" + id_supplement2 + "','" + ID_job2 + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + "', '" + date2[1] + " " + date2[2] + "','no','no','no','empty')");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("success");
                        printStream.println("success");
                        statement.close();
                    } catch (Exception e) {
                        System.out.println("create task failed");
                        printStream.println("create task failed");
                        statement.close();
                    }
                } else if (test2.compareTo(DateNow) < 0) {
                    System.out.println("before");
                    System.out.println("Create Task failed");
                    printStream.println("Create Task failed");
                    statement.close();
                } else if (test2.compareTo(DateNow) == 0) {
                    System.out.println("same");
                    System.out.println("Create Task failed");
                    printStream.println("Create Task failed");
                    statement.close();
                }

            } else if (test1.compareTo(test2) < 0) {
                System.out.println("before");
                System.out.println("Create Task failed");
                printStream.println("Create Task failed");
                statement.close();
            } else if (test1.compareTo(test2) == 0) {
                System.out.println("same");
                System.out.println("Create Task failed");
                printStream.println("Create Task failed");
                statement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCreateTask2(String[] data, String temporary) {
        try {
            String ID_job2, JobDate;
            System.out.println("This is create task");
            System.out.println(temporary);
            String[] date2 = temporary.split(",");
            System.out.println("task Name  : " + data[1]);
            System.out.println("task Desc  : " + data[2]);
            System.out.println("task type : " + data[3]);
            System.out.println("task diff : " + data[4]);
            System.out.println("Job name : " + data[5]);
            statement = localconnection_thread.createStatement();
            System.out.println("SELECT ID_Job,Due_Time FROM job WHERE ID_Job ='" + data[5] + "'");
            res = statement.executeQuery("SELECT ID_Job,Due_Time FROM job WHERE ID_Job ='" + data[5] + "'");
            res.next();
            ID_job2 = res.getString(1);
            JobDate = res.getString(2);
            System.out.println("Due Date Job:" + JobDate);
            String[] JobDateSplit = JobDate.split(" ");
            JobDate = JobDateSplit[0];
            statement.close();
            System.out.println("Due Date Job2 : " + JobDate);
            System.out.println("Due Date  : " + date2[1]);
            System.out.println("Due Time  : " + date2[2]);
            Date DateNow = new Date();
            Date test1 = df.parse(JobDate);
            Date test2 = df.parse(date2[1]);
            String id_supplement2 = Long.toString(System.currentTimeMillis());
            if (test1.compareTo(test2) > 0) {
                System.out.println("after");
                if (test2.compareTo(DateNow) > 0) {
                    System.out.println("after");
                    preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`task` (`ID_Task`,`ID_Job`, `Name`, `Description`, `Type`, `Difficulty`, `Due_Time`,`Approved`,`Completed`,`Finished`,`Status`) VALUES ('" + data[1] + "|" + id_supplement2 + "','" + ID_job2 + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + "', '" + date2[1] + " " + date2[2] + "','no','no','no','empty')");
                    System.out.println("INSERT INTO `group_work`.`task` "
                            + "(`ID_Task`,`ID_Job`, `Name`, `Description`, `Type`, `Difficulty`, `Due_Time`,`Approved`,`Completed`,`Finished`,`Status`) VALUES "
                            + "('" + data[1] + "|" + id_supplement2 + "','" + ID_job2 + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "', '" + data[4] + "', '" + date2[1] + " " + date2[2] + "','no','no','no','empty')");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("success");
                        printStream.println(data[1] + "|" + id_supplement2);
                        postFB("create_task", "List_Job/" + ID_job2 + "/List_Task/" + data[1] + "|" + id_supplement2, data, date2);
                        statement.close();
                    } catch (Exception e) {
                        System.out.println("create task failed");
                        System.out.println(e);
                        printStream.println("create task failed");
                        statement.close();
                    }
                } else if (test2.compareTo(DateNow) < 0) {
                    System.out.println("before");
                    System.out.println("Create Task failed");
                    printStream.println("Create Task failed");
                    statement.close();
                } else if (test2.compareTo(DateNow) == 0) {
                    System.out.println("same");
                    System.out.println("Create Task failed");
                    printStream.println("Create Task failed");
                    statement.close();
                }

            } else if (test1.compareTo(test2) < 0) {
                System.out.println("before");
                System.out.println("Create Task failed");
                printStream.println("Create Task failed");
                statement.close();
            } else if (test1.compareTo(test2) == 0) {
                System.out.println("same");
                System.out.println("Create Task failed");
                printStream.println("Create Task failed");
                statement.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncInviteWorker(String[] data) {
        try {
            //cek if user correct
            System.out.println("SELECT ID_User FROM user WHERE ID_User ='" + data[1] + "'");
            res = statement.executeQuery("SELECT ID_User FROM user WHERE ID_User ='" + data[1] + "'");
            if (res.next()) {
                //cek already exist in group
                System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[2] + "' AND Role='Member'");
                res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[2] + "' AND Role='Member'");
                if (res.next()) {
                    System.out.println("Already joined job");
                    printStream.println("success");
                    //preparedStatement.close();
                } else {
                    System.out.println("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[1] + "', '" + data[2] + "', 'Member');");
                    preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[1] + "', '" + data[2] + "', 'Member');");
                    try {
                        preparedStatement.executeUpdate();
                        System.out.println("success join job");
                        printStream.println("success");
                        preparedStatement.close();
                    } catch (Exception e) {
                        System.out.println("failed join job");
                        printStream.println("failed");
                        preparedStatement.close();
                    }
                }

            } else {
                System.out.println("Failed No User registered");
                printStream.println("Failed No User registered");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void FuncRemoveWorker(String[] data) {
        int Qty_Cur_w = 0, Qty_Max_w = 0;
        String ID_job = data[2];
        try {

            //grouping delete
            System.out.println("DELETE FROM `grouping` WHERE ID_User = '" + data[1] + "' AND ID_Job ='" + data[2] + "'");
            preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `grouping` WHERE ID_User = '" + data[1] + "' AND ID_Job ='" + data[2] + "'");

            try {
                preparedStatement.executeUpdate();
                System.out.println("Success abandon group");
                //printStream.println("Success");
                //preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed abandon group");
                //printStream.println("failed");
                //preparedStatement.close();
            }

            //listing task to update firebase
            String append;
            String appendix = "";

            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_User ='" + data[1] + "'");
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;
            }

            //task ta null kan;
            System.out.println("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_User` = '" + data[1] + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = NULL, `Approved` = 'no', `Completed` = 'no' WHERE `task`.`ID_User` = '" + data[1] + "'");
            try {
                preparedStatement.executeUpdate();
                System.out.println("Success abandoning task");
                //printStream.println("Success");
                //preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed abandon task");
                //printStream.println("failed");
                //preparedStatement.close();
            }

            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res.next();
            Qty_Cur_w = Integer.parseInt(res.getString(1)) - 1;

            System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "'");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "'");
            try {
                preparedStatement.executeUpdate();
                System.out.println(Qty_Cur_w + "-LISTJOB-" + appendix);
                printStream.println(Qty_Cur_w + "-LISTJOB-" + appendix);
                preparedStatement.close();
            } catch (Exception e) {
                System.out.println("Failed minus worker");
                printStream.println("failed");
                preparedStatement.close();
            }

            //message berkaitan dihapus
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncRemoveWorker2(String[] data) {
        int Qty_Cur_w = 0, Qty_Max_w = 0;
        String ID_job = data[2];
        try {
            //listing task to update firebase
            String append;
            String appendix = "";

            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_User ='" + data[1] + "'");
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;
            }

            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
            res.next();
            Qty_Cur_w = Integer.parseInt(res.getString(1)) - 1;

            System.out.println(Qty_Cur_w + "-LISTJOB-" + appendix);
            printStream.println(Qty_Cur_w + "-LISTJOB-" + appendix);

            //message berkaitan dihapus
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //Previlege dari LEADER

    //ALUR REWARD
    //COMPLETE --> APPROVE --> WAIT FOR FINISH
    //ALUR FINISH
    //FINISH
    public void FuncGetFinish(String[] data) {
        try {
            String ID_Job = data[1];
            //cek task, all
            int count_task_all = 0;
            System.out.println("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' ");
            res = statement.executeQuery("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' ");
            if (res.next()) {
                count_task_all = res.getInt(1);
            }
            String[] task = new String[count_task_all];
            System.out.println("count task all : " + count_task_all);

            if (count_task_all > 3) {
                //cek task finished but not yet approved
                String need_approved;
                System.out.println("SELECT ID_Task FROM task WHERE ID_Job ='" + ID_Job + "' AND Completed='yes' AND Approved ='no'");
                res = statement.executeQuery("SELECT ID_Task FROM task WHERE ID_Job ='" + ID_Job + "' AND Completed='yes' AND Approved ='no'");
                if (res.next()) {
                    need_approved = "yes";
                } else {
                    need_approved = "none";
                }
                System.out.println("need approved = " + need_approved);
                //cek task finished but not yet approved

                //cek task, finished
                int count_task_finished = 0;
                System.out.println("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' AND Approved='yes'");
                res = statement.executeQuery("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' AND Approved='yes'");
                if (res.next()) {
                    count_task_finished = res.getInt(1);
                }
                System.out.println("count task finished : " + count_task_finished);

                //cek if task finished was greater than
                int count_task_unfinished = 0;
                if (need_approved.equals("none")) {
                    count_task_unfinished = count_task_all - count_task_finished;
                    System.out.println("count task unfinished = " + count_task_unfinished);
                    if (count_task_finished > count_task_unfinished) {
                        System.out.println("finished greater than unfinished");
                        FuncGetVote(data);
                    } else {
                        System.out.println("unfinished greater than finished");
                        //FuncGetUnsatisfactoryJob(data);
                    }
                } else {
                    System.out.println("task need approval");
                    printStream.println("failed task need approval");
                }
                //cek if task finished was greater than

            } else {
                System.out.println("failed task requirement must be 3");
                printStream.println("failed task requirement must be 3");

                //FuncGetUnsatisfactoryJob(data);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void FuncGetVote(String[] data) {
        try {
            //cara dapat member
            int count_member = 0;
            String ID_Job = data[1];
            System.out.println("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
            res = statement.executeQuery("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
            if (res.next()) {
                count_member = res.getInt(1);
            }
            System.out.println("Count Member : " + count_member);

            String[] member = new String[count_member];
            int i = 0;
            System.out.println("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
            res = statement.executeQuery("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
            while (res.next()) {
                member[i] = res.getString(1);
                System.out.println("Member : " + i + ". " + member[i]);
                i++;
                System.out.println("i : " + i);
            }
            System.out.println("member length : " + member.length);
            System.out.println("UPDATE `group_work`.`job` SET `Finished` = 'yes', `Status`='vote' WHERE `job`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Finished` = 'yes', `Status`='vote' WHERE `job`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement.executeUpdate();

            System.out.println("UPDATE `group_work`.`task` SET `Finished` = 'yes',  WHERE `task`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement.executeUpdate();

            System.out.println("UPDATE `group_work`.`grouping` SET `Finished` = 'yes',  WHERE `grouping`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`grouping` SET `Finished` = 'yes' WHERE `grouping`.`ID_Job` = '" + ID_Job + "';");
            preparedStatement.executeUpdate();

            FuncMessage(data, "vote");

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncCashIn(String[] data) {
        try {
            int count_member = 0;
            String ID_Job = data[1];
            int QtyDown = 0, QtyUp = 0, QtyCurW = 0, QtyVote = 0;
            //cek if vote and down vote diplus sama dengan current worker
            System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job + "' ");
            res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_Job + "'");
            res.next();
            QtyCurW = res.getInt(1);

            System.out.println("SELECT UpVote FROM job WHERE ID_Job ='" + ID_Job + "' ");
            res = statement.executeQuery("SELECT UpVote FROM job WHERE ID_Job ='" + ID_Job + "'");
            res.next();
            QtyUp = res.getInt(1);

            System.out.println("SELECT DownVote FROM job WHERE ID_Job ='" + ID_Job + "' ");
            res = statement.executeQuery("SELECT DownVote FROM job WHERE ID_Job ='" + ID_Job + "'");
            res.next();
            QtyDown = res.getInt(1);

            QtyVote = QtyDown + QtyUp;
            if (QtyVote == QtyCurW) {
                //cara dapat member
                System.out.println("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
                res = statement.executeQuery("SELECT count(ID_User) FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
                if (res.next()) {
                    count_member = res.getInt(1);
                }
                System.out.println("Count Member : " + count_member);

                String[] member = new String[count_member];
                int i = 0;
                System.out.println("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
                res = statement.executeQuery("SELECT ID_User FROM grouping WHERE ID_Job ='" + ID_Job + "' AND Role ='Member' ");
                while (res.next()) {
                    member[i] = res.getString(1);
                    System.out.println("Member : " + i + ". " + member[i]);
                    i++;
                    System.out.println("i : " + i);
                }
                System.out.println("member length : " + member.length);
                int leaderReward = FuncGetReward(member, ID_Job);
                FuncGetLeaderReward(ID_Job, leaderReward);

                //meng end kan
                System.out.println("UPDATE `group_work`.`job` SET `Status` = 'end' where ID_Job = '" + ID_Job + "'");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Status` = 'end' where ID_Job = '" + ID_Job + "'");
                preparedStatement.executeUpdate();

                printStream.println("sukses");

            } else {
                printStream.println("Failed Cash in , someone didnt vote");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int FuncGetReward(String[] member, String ID_Job) {
        int forleader = 0;
        String level, exp, str, dex, intl, Nxt_Exp;
        String Type, Difficulty, Time;
        System.out.println(member.length);
        int count_member = 0;
        try {
            while (count_member < member.length) {
                //the user
                System.out.println("SELECT Level,Experience,Strength,Dexterity,Intelligence, Nxt_Experience FROM user WHERE ID_User ='" + member[count_member] + "'");
                res = statement.executeQuery("SELECT Level,Experience,Strength,Dexterity,Intelligence, Nxt_Experience FROM user WHERE ID_User ='" + member[count_member] + "'");
                res.next();
                level = res.getString(1);
                exp = res.getString(2);
                str = res.getString(3);
                dex = res.getString(4);
                intl = res.getString(5);
                Nxt_Exp = res.getString(6);
                int totalExp = Integer.parseInt(exp), totalStr = Integer.parseInt(str), totalDex = Integer.parseInt(dex), totalIntl = Integer.parseInt(intl);
                //the user

                //select all task approved
                int count_task_personal = 0;
                System.out.println("SELECT count(ID_Task) from task where ID_User = '" + member[count_member] + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
                res = statement.executeQuery("SELECT count(ID_Task) from task where ID_User = '" + member[count_member] + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
                if (res.next()) {
                    count_task_personal = res.getInt(1);
                }
                System.out.println("Count task personal finished : " + member[count_member] + ", " + count_task_personal);

                String[] task = new String[count_task_personal];
                int i = 0;
                System.out.println("SELECT ID_Task FROM task where ID_User = '" + member[count_member] + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
                res = statement.executeQuery("SELECT ID_Task FROM task where ID_User = '" + member[count_member] + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
                while (res.next()) {
                    task[i] = res.getString(1);
                    System.out.println("task[" + i + "] " + task[i]);
                    i++;
                }
                System.out.println("--------------------------------------------");

                int count = 0;

                while (count < count_task_personal) {
                    //the task
                    res = statement.executeQuery("SELECT Type, Difficulty, Due_Time from task where ID_Task = '" + task[count] + "'");
                    res.next();
                    Type = res.getString(1);
                    Difficulty = res.getString(2);
                    Time = res.getString(3);
                    //the task

                    //cek tanggal
                    //tambahi date completion
                    String condition = "";
                    DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
                    Date tgl = new Date();
                    Date test1 = date2.parse(Time);
                    System.out.println("System date: " + date2.format(tgl));
                    System.out.println("Due date : " + date2.format(test1));
                    if (test1.compareTo(tgl) < 0) {
                        System.out.println("late (after)");
                        condition = "late";
                    } else if (test1.compareTo(tgl) > 0) {
                        System.out.println("before");
                        condition = "clear";
                    } else if (test1.compareTo(tgl) == 0) {
                        System.out.println("same");
                        condition = "clear";
                    }
                    //cek tanggal

                    //total poin 1 task
                    if (condition.equals("clear")) {
                        switch (Type) {
                            case "Str":
                                totalStr = totalStr + Integer.parseInt(Difficulty);
                                break;
                            case "Dex":
                                totalDex = totalDex + Integer.parseInt(Difficulty);
                                break;
                            case "Int":
                                totalIntl = totalIntl + Integer.parseInt(Difficulty);
                                break;
                        }
                        totalExp = totalExp + Integer.parseInt(exp) + (2 * (50 + (int) Math.sqrt(Math.pow(20, Integer.parseInt(Difficulty)))));
                    } else {
                        //FuncGetPunishment(data);
                    }

                    //System.out.println("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Task` = '" + task[count] + "';");
                    //preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Task` = '" + task[count] + "';");
                    //preparedStatement.executeUpdate();
                    //totalpoin
                    count++;
                }
                forleader = forleader + totalExp;
                /*
                System.out.println("UPDATE `group_work`.`user` SET `Experience` = '" + totalExp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Experience` = '" + totalExp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
                preparedStatement.executeUpdate();*/
                //checking level
//                int Nxt_Lvl = Integer.parseInt(level);
//                int Nxt_exp = Integer.parseInt(Nxt_Exp);
//                System.out.println("Initial LVL: " + Nxt_Lvl);
//                System.out.println("Total EXP  : " + totalExp);
//                if (count_task_personal > 0) {
//                    while (totalExp >= Nxt_exp) {
//                        System.out.println("Level UP");
//                        System.out.println("Initial Total EXP : " + totalExp);
//                        System.out.println("Initial Next  EXP : " + Nxt_exp);
//                        Nxt_Lvl++;
//                        System.out.println("Level : " + Nxt_Lvl);
//                        totalExp = totalExp - Nxt_exp;
//                        Nxt_exp = (int) (10 * (Math.pow(Nxt_Lvl, 1.5)));
//                        System.out.println("Total EXP : " + totalExp);
//                        System.out.println("Next  EXP : " + Nxt_exp);
//                    }
//                    System.out.println("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
//                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
//                    preparedStatement.executeUpdate();
//                } else {
//                    System.out.println("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
//                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
//                    preparedStatement.executeUpdate();
//                }
                count_member++;
            }
            //total poin
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return forleader;
    }

    public void FuncGetRewardSingle(String member, String ID_Job) {
        //int forleader = 0;
        String level, exp, str, dex, intl, Nxt_Exp;
        String Type, Difficulty, Time;
        //System.out.println(member.length);
        //int count_member = 0;
        try {

            //the user
            System.out.println("SELECT Level,Experience,Strength,Dexterity,Intelligence, Nxt_Experience FROM user WHERE ID_User ='" + member + "'");
            res = statement.executeQuery("SELECT Level,Experience,Strength,Dexterity,Intelligence, Nxt_Experience FROM user WHERE ID_User ='" + member + "'");
            res.next();
            level = res.getString(1);
            exp = res.getString(2);
            str = res.getString(3);
            dex = res.getString(4);
            intl = res.getString(5);
            Nxt_Exp = res.getString(6);
            int totalExp = Integer.parseInt(exp), totalStr = Integer.parseInt(str), totalDex = Integer.parseInt(dex), totalIntl = Integer.parseInt(intl);
            //the user

            //select all task approved
            int count_task_personal = 0;
            System.out.println("SELECT count(ID_Task) from task where ID_User = '" + member + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
            res = statement.executeQuery("SELECT count(ID_Task) from task where ID_User = '" + member + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
            if (res.next()) {
                count_task_personal = res.getInt(1);
            }
            System.out.println("Count task personal finished : " + member + ", " + count_task_personal);

            String[] task = new String[count_task_personal];
            int i = 0;
            System.out.println("SELECT ID_Task FROM task where ID_User = '" + member + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
            res = statement.executeQuery("SELECT ID_Task FROM task where ID_User = '" + member + "' AND Approved = 'yes' AND ID_Job ='" + ID_Job + "'");
            while (res.next()) {
                task[i] = res.getString(1);
                System.out.println("task[" + i + "] " + task[i]);
                i++;
            }
            System.out.println("--------------------------------------------");

            int count = 0;

            while (count < count_task_personal) {
                //the task
                res = statement.executeQuery("SELECT Type, Difficulty, Due_Time from task where ID_Task = '" + task[count] + "'");
                res.next();
                Type = res.getString(1);
                Difficulty = res.getString(2);
                Time = res.getString(3);
                //the task

                //cek tanggal
                //tambahi date completion
                String condition = "";
                DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat date2 = new SimpleDateFormat("yyyy-MM-dd");
                Date tgl = new Date();
                Date test1 = date2.parse(Time);
                System.out.println("System date: " + date2.format(tgl));
                System.out.println("Due date : " + date2.format(test1));
                if (test1.compareTo(tgl) < 0) {
                    System.out.println("late (after)");
                    condition = "late";
                } else if (test1.compareTo(tgl) > 0) {
                    System.out.println("before");
                    condition = "clear";
                } else if (test1.compareTo(tgl) == 0) {
                    System.out.println("same");
                    condition = "clear";
                }
                //cek tanggal

                //total poin 1 task
                if (condition.equals("clear")) {
                    switch (Type) {
                        case "Str":
                            totalStr = totalStr + Integer.parseInt(Difficulty);
                            break;
                        case "Dex":
                            totalDex = totalDex + Integer.parseInt(Difficulty);
                            break;
                        case "Int":
                            totalIntl = totalIntl + Integer.parseInt(Difficulty);
                            break;
                    }
                    totalExp = totalExp + Integer.parseInt(exp) + (2 * (50 + (int) Math.sqrt(Math.pow(20, Integer.parseInt(Difficulty)))));
                } else {
                    //FuncGetPunishment(data);
                }

                System.out.println("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Task` = '" + task[count] + "';");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Task` = '" + task[count] + "';");
                preparedStatement.executeUpdate();

                //totalpoin
                count++;
            }
            /*
                System.out.println("UPDATE `group_work`.`user` SET `Experience` = '" + totalExp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Experience` = '" + totalExp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member[count_member] + "';");
                preparedStatement.executeUpdate();*/
            //checking level
            int Nxt_Lvl = Integer.parseInt(level);
            int Nxt_exp = Integer.parseInt(Nxt_Exp);
            System.out.println("Initial LVL: " + Nxt_Lvl);
            System.out.println("Total EXP  : " + totalExp);
            if (count_task_personal > 0) {
                while (totalExp >= Nxt_exp) {
                    System.out.println("Level UP");
                    System.out.println("Initial Total EXP : " + totalExp);
                    System.out.println("Initial Next  EXP : " + Nxt_exp);
                    Nxt_Lvl++;
                    System.out.println("Level : " + Nxt_Lvl);
                    totalExp = totalExp - Nxt_exp;
                    Nxt_exp = (int) (10 * (Math.pow(Nxt_Lvl, 1.5)));
                    System.out.println("Total EXP : " + totalExp);
                    System.out.println("Next  EXP : " + Nxt_exp);
                }
                System.out.println("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member + "';");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member + "';");
                preparedStatement.executeUpdate();
            } else {
                System.out.println("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member + "';");
                preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Level` = '" + Nxt_Lvl + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_exp + "', `Strength` = '" + totalStr + "', `Dexterity` = '" + totalDex + "', `Intelligence` = '" + totalIntl + "' WHERE `user`.`ID_User` = '" + member + "';");
                preparedStatement.executeUpdate();
            }

            //total poin
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return forleader;
    }

    public void FuncGetLeaderReward(String ID_Job, int leaderReward) {
        try {
            int level, exp, Nxt_Exp;
            String owner;
            System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + ID_Job + "'");
            res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + ID_Job + "'");
            res.next();
            owner = res.getString(1);

            System.out.println("SELECT Level,Experience, Nxt_Experience FROM user WHERE ID_User ='" + owner + "'");
            res = statement.executeQuery("SELECT Level,Experience, Nxt_Experience FROM user WHERE ID_User ='" + owner + "'");
            res.next();
            level = res.getInt(1);
            exp = res.getInt(2);
            Nxt_Exp = res.getInt(3);

            int count_task_all = 0;
            System.out.println("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' ");
            res = statement.executeQuery("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' ");
            if (res.next()) {
                count_task_all = res.getInt(1);
            }
            System.out.println("count task finished : " + count_task_all);

            int count_task_finished = 0;
            System.out.println("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' AND Approved='yes'");
            res = statement.executeQuery("SELECT count(ID_Task) FROM task WHERE ID_Job ='" + ID_Job + "' AND Approved='yes'");
            if (res.next()) {
                count_task_finished = res.getInt(1);
            }
            System.out.println("count task finished : " + count_task_finished);
            //int reward_leader = (int) ( count_task_all* 100 * count_task_finished / (Math.pow(count_task_all, 0.5)));
            int totalExp = (int) (exp + (0.75 * leaderReward));

            //int Nxt_exp = Integer.parseInt(Nxt_Exp);
            System.out.println("Initial LVL: " + level);
            System.out.println("Total EXP  : " + totalExp);

            while (totalExp >= Nxt_Exp) {
                System.out.println("Initial Total EXP : " + totalExp);
                System.out.println("Initial Next  EXP : " + Nxt_Exp);
                level++;
                System.out.println("Level UP");
                System.out.println("Level : " + level);
                totalExp = totalExp - Nxt_Exp;
                Nxt_Exp = (int) (10 * (Math.pow(level, 1.5)));
                System.out.println("Total EXP : " + totalExp);
                System.out.println("Next  EXP : " + Nxt_Exp);
            }
            System.out.println("UPDATE `group_work`.`user` SET `Level` = '" + level + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_Exp + "' WHERE `user`.`ID_User` = '" + owner + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`user` SET `Level` = '" + level + "', `Experience` = '" + totalExp + "', `Nxt_Experience` = '" + Nxt_Exp + "' WHERE `user`.`ID_User` = '" + owner + "';");
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //not done yet
    public void FuncGetUnsatisfactoryJob(String[] data) {
        try {
            //job
            System.out.println("UPDATE `group_work`.`grouping` SET `Finished` = 'yes' WHERE `grouping`.`ID_Job` = '" + data[1] + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`grouping` SET `Finished` = 'yes' WHERE `grouping`.`ID_Job` = '" + data[1] + "';");
            preparedStatement.executeUpdate();

            //group
            System.out.println("UPDATE `group_work`.`job` SET `Finished` = 'yes' WHERE `job`.`ID_Job` = '" + data[1] + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Finished` = 'yes' WHERE `job`.`ID_Job` = '" + data[1] + "';");
            preparedStatement.executeUpdate();

            //task
            System.out.println("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Job` = '" + data[1] + "';");
            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `Finished` = 'yes' WHERE `task`.`ID_Job` = '" + data[1] + "';");
            preparedStatement.executeUpdate();

            printStream.println("failed Unsatisfactory");
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncGetPunishment(String[] data) {

    }

    public void FuncUser() {
        try {
            res = statement.executeQuery("SELECT ID_User from user");
            String append;
            String appendix = "";
            int i = 0;
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;

            }

            if (appendix.equals("")) {
                printStream.println("failed no job");
                statement.close();
            } else {
                System.out.println("Sending :" + appendix);
                printStream.println(appendix);
                statement.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //not done yet

    //MESSAGE SYSTEM
    //INBOX
    public void FuncGetMessage_in(String[] data) {
        try {
            int i = 0;
            String streammessage = "";
            System.out.println("SELECT sender,recepient,message FROM message WHERE recepient ='" + data[1] + "' AND confirmation='send' ");
            res = statement.executeQuery("SELECT sender,recepient,message FROM message WHERE recepient ='" + data[1] + "' AND confirmation='send'");
            while (res.next()) {
                if (i == 0) {
                    streammessage = res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                    i++;
                } else {
                    streammessage = streammessage + "-msg-" + res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                }
            }
            System.out.println("send : " + streammessage);
            if (streammessage.isEmpty()) {
                System.out.println("failed no msg");
                printStream.println("failed no msg");
            } else {
                System.out.println("Sukses");
                printStream.println(streammessage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncGetMessage_out(String[] data) {
        try {
            int i = 0;
            String streammessage = "";
            System.out.println("SELECT sender,recepient,message FROM message WHERE sender ='" + data[1] + "' AND confirmation='send' ");
            res = statement.executeQuery("SELECT sender,recepient,message FROM message WHERE sender ='" + data[1] + "' AND confirmation='send'");
            while (res.next()) {
                if (i == 0) {
                    streammessage = res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                    i++;
                } else {
                    streammessage = streammessage + "-msg-" + res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                }
            }
            System.out.println("send : " + streammessage);
            if (streammessage.isEmpty()) {
                System.out.println("failed no msg");
                printStream.println("failed no msg");
            } else {
                System.out.println("Sukses");
                printStream.println(streammessage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncGetMessage_conf(String[] data) {
        try {
            int i = 0;
            String streammessage = "";
            System.out.println("SELECT sender,recepient,message FROM message WHERE sender ='" + data[1] + "' ");
            res = statement.executeQuery("SELECT sender,recepient,message FROM message WHERE sender ='" + data[1] + "' ");
            while (res.next()) {
                if (i == 0) {
                    streammessage = res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                    i++;
                } else {
                    streammessage = streammessage + "-msg-" + res.getString(1) + "," + res.getString(2) + "," + res.getString(3);
                }
            }
            System.out.println("send : " + streammessage);
            if (streammessage.isEmpty()) {
                System.out.println("failed no msg");
                printStream.println("failed no msg");
            } else {
                System.out.println("Sukses");
                printStream.println(streammessage);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //INBOX

    //FUNCTIONALITY
    public void FuncMessage(String[] data, String message) {
        String owner;
        String request;
        int Qty_Cur_w = 0, Qty_Max_w = 0;

        switch (message) {
            case "apply_request":
                try {
                    //cek if full or not
                    System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + data[3] + "'");
                    res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + data[3] + "'");
                    res.next();
                    Qty_Cur_w = 1 + Integer.parseInt(res.getString(1));

                    System.out.println("SELECT Max_Worker FROM job WHERE ID_Job ='" + data[3] + "'");
                    res = statement.executeQuery("SELECT Max_Worker FROM job WHERE ID_Job ='" + data[3] + "'");
                    res.next();
                    Qty_Max_w = Integer.parseInt(res.getString(1));

                    if (Qty_Cur_w <= Qty_Max_w) {
                        //cek if already request
                        request = data[1] + "-apply-" + data[2];
                        res = statement.executeQuery("SELECT message FROM message WHERE message ='" + request + "' and confirmation ='send'");
                        if (res.next()) {
                            if (res.getString(1).equals(request)) {
                                System.out.println("failed already ask");
                                printStream.println("failed already ask");
                            }
                        } else {
                            System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + data[3] + "'");
                            res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + data[3] + "'");
                            res.next();
                            owner = res.getString(1);
                            if (owner.equals(data[1])) {
                                System.out.println("failed cant apply owned job");
                                printStream.println("failed cant apply owned job");
                                //preparedStatement.close();
                            } else {
                                //INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`, `message`) VALUES (NULL, 'ian', 'kez', 'kez-apply-test1|153603447984');
                                System.out.println("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + data[1] + "', '" + owner + "','apply_task', '" + data[1] + "-apply-" + data[2] + "','send');");
                                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + data[1] + "', '" + owner + "','apply_task', '" + data[1] + "-apply-" + data[2] + "','send');");
                                try {
                                    preparedStatement.executeUpdate();
                                    System.out.println("Sukses");
                                    printStream.println(owner + "|" + data[1] + "-apply-" + data[2]);
                                    postData(owner + "-" + data[1] + " apply to your task-Apply Request");
                                    preparedStatement.close();
                                } catch (Exception e) {
                                    System.out.println("failed");
                                    printStream.println("failed");
                                    preparedStatement.close();
                                }
                            }
                        }
                    } else {
                        System.out.println("WORKER FULL");
                        printStream.println("failed Worker FULL");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case "assign_permission":
                try {
                    //cek if already request
                    request = data[1] + "-assign-" + data[2];
                    res = statement.executeQuery("SELECT message FROM message WHERE message ='" + request + "' and confirmation ='send'");
                    if (res.next()) {
                        if (res.getString(1).equals(request)) {
                            System.out.println("failed already ask");
                            printStream.println("failed already ask");
                        }
                    } else {
                        System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + data[3] + "'");
                        res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + data[3] + "'");
                        res.next();
                        owner = res.getString(1);
                        //INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`, `message`) VALUES (NULL, 'ian', 'kez', 'kez-apply-test1|153603447984');
                        System.out.println("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + owner + "', '" + data[1] + "','assg_task', '" + data[1] + "-assign-" + data[2] + "','send');");
                        preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + owner + "', '" + data[1] + "','assg_task', '" + data[1] + "-assign-" + data[2] + "','send');");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("Sukses");
                            printStream.println(request);
                            postData(data[1] + "-" + owner + " has message for you-Assign Permission");
                            preparedStatement.close();
                        } catch (Exception e) {
                            System.out.println("failed");
                            printStream.println("failed");
                            preparedStatement.close();
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case "invite_permission":
                try {
                    //cek if already joined
                    System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[2] + "' AND Role='Member'");
                    res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[1] + "' AND ID_Job ='" + data[2] + "' AND Role='Member'");
                    if (res.next()) {

                        System.out.println("failed already join");
                        printStream.println("failed already join");

                    } else {
                        System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + data[2] + "'");
                        res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + data[2] + "'");
                        res.next();
                        Qty_Cur_w = 1 + Integer.parseInt(res.getString(1));

                        System.out.println("SELECT Max_Worker FROM job WHERE ID_Job ='" + data[2] + "'");
                        res = statement.executeQuery("SELECT Max_Worker FROM job WHERE ID_Job ='" + data[2] + "'");
                        res.next();
                        Qty_Max_w = Integer.parseInt(res.getString(1));

                        if (Qty_Cur_w <= Qty_Max_w) {
                            //cek if already request
                            request = data[1] + "-invite-" + data[2];
                            res = statement.executeQuery("SELECT message FROM message WHERE message ='" + request + "' and confirmation ='send'");
                            if (res.next()) {
                                if (res.getString(1).equals(request)) {
                                    System.out.println("failed already ask");
                                    printStream.println("failed already ask");
                                }
                            } else {
                                System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + data[2] + "'");
                                res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + data[2] + "'");
                                res.next();
                                owner = res.getString(1);
                                //INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`, `message`) VALUES (NULL, 'ian', 'kez', 'kez-apply-test1|153603447984');
                                System.out.println("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + owner + "', '" + data[1] + "','invite', '" + data[1] + "-invite-" + data[2] + "','send');");
                                preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, '" + owner + "', '" + data[1] + "','invite', '" + data[1] + "-invite-" + data[2] + "','send');");
                                try {
                                    preparedStatement.executeUpdate();
                                    System.out.println("Sukses");
                                    printStream.println(request);
                                    postData(data[1] + "-" + owner + " has message for you-Invite Permission");
                                    preparedStatement.close();
                                } catch (Exception e) {
                                    System.out.println("failed");
                                    printStream.println("failed");
                                    preparedStatement.close();
                                }
                            }
                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "vote":
                try {

                    System.out.println("Select ID_User from grouping where Role='Leader' AND ID_Job='" + data[1] + "'");
                    res = statement.executeQuery("Select ID_User from grouping where  Role='Leader' AND ID_Job='" + data[1] + "'");
                    res.next();
                    owner = res.getString(1);

                    System.out.println("Select count(ID_User) from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");
                    res = statement.executeQuery("Select count(ID_User) from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");
                    res.next();
                    String[] member = new String[res.getInt(1)];

                    System.out.println("Select ID_User from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");
                    res = statement.executeQuery("Select ID_User from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");

                    int i = 0;
                    while (res.next()) {
                        member[i] = res.getString(1);
                        i++;
                    }
                    int count = 0;
                    String con = "";
                    while (count < member.length) {
                        System.out.println("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, 'System', '" + member[count] + "','vote', '" + member[count] + "-vote-" + owner + "-" + data[1] + "-" + "','send');");
                        preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`message` (`ID`, `sender`, `recepient`,`type`, `message`,`confirmation`) VALUES (NULL, 'System', '" + member[count] + "','vote', '" + member[count] + "-vote-" + owner + "-" + data[1] + "-" + "','send');");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("Sukses");
                            con = "sukses";
                            postData(member[count] + "-Time to vote-Invite Permission");
                            preparedStatement.close();
                        } catch (Exception e) {
                            System.out.println("failed");
                            con = "failed";
                            preparedStatement.close();
                        }
                        count++;
                    }

                    //listing task to update firebase
                    String append;
                    String appendix = "";
                    int a = 0;

                    System.out.println("Select ID_User from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");
                    res = statement.executeQuery("Select ID_User from grouping where ID_Job = '" + data[1] + "' AND Role='Member'");

                    while (res.next()) {
                        System.out.println("result : " + res.getString(1));
                        append = res.getString(1);
                        if (a == 0) {
                            appendix = append;
                        } else {
                            appendix = appendix + "-" + append;
                        }
                        a++;
                    }

                    if (con.equals("sukses")) {
                        System.out.println(owner + "-" + data[1] + "-" + appendix);
                        printStream.println(owner + "-" + data[1] + "-" + appendix);
                    } else {
                        printStream.println("failed");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
        }
    }

    public void FuncReply(String[] data) {
        String condition1 = "", condition2 = "", condition3 = "", condition4 = "";
        String message = "";
        int Qty_Cur_w = 0, Qty_Max_w = 0, Qty_already = 0;
        switch (data[1]) {
            case "apply_yes":
                try {
                    //get id job
                    String ID_job = "";
                    System.out.println("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    res = statement.executeQuery("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    if (res.next()) {
                        ID_job = res.getString(1);
                        condition3 = "sukses";
                    } else {
                        condition3 = "fail";
                    }
                    //get id job

                    System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    Qty_already = Integer.parseInt(res.getString(1));
                    Qty_Cur_w = 1 + Integer.parseInt(res.getString(1));

                    System.out.println("SELECT Max_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Max_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    Qty_Max_w = Integer.parseInt(res.getString(1));

                    //cek if task exist
                    System.out.println("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
                    res = statement.executeQuery("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
                    if (res.next()) {
                        condition1 = "sukses";
                    } else {
                        condition1 = "fail";
                    }
                    System.out.println("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
                    res = statement.executeQuery("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
                    if (res.next()) {
                        condition2 = "sukses";
                    } else {
                        condition2 = "fail";
                    }
                    // cek if task exist

                    //cek if message exist
                    message = data[3] + "-" + data[5] + "-" + data[6];
                    System.out.println("SELECT message FROM message WHERE message ='" + message + "'");
                    res = statement.executeQuery("SELECT message FROM message WHERE message ='" + message + "'");
                    if (res.next()) {
                        //ID_job = res.getString(1);
                        condition4 = "sukses";
                    } else {
                        condition4 = "fail";
                    }

                    System.out.println("con1 : " + condition1);
                    System.out.println("con2 : " + condition2);
                    System.out.println("con3 : " + condition3);
                    System.out.println("con4 : " + condition4);

                    if (condition1.equals("sukses") && condition2.equals("sukses") && condition3.equals("sukses") && condition4.equals("sukses")) {
                        //message = data[3] + "-" + data[5] + "-" + data[6];
                        //delete that message
                        /*
                        System.out.println("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success delete message");
                        } catch (Exception e) {
                            System.out.println("failed apply");
                            printStream.println("failed");
                        }*/
                        System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success reply message");
                        } catch (Exception e) {
                            System.out.println("failed reply");
                            printStream.println("failed reply");
                        }

                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = '" + data[3] + "', `Status` ='on' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                        System.out.println("UPDATE `group_work`.`task` SET `ID_User` = '" + data[3] + "', `Status` ='on' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success apply");
                        } catch (Exception e) {
                            System.out.println("failed apply");
                            printStream.println("failed");
                        }
                        //cek if already joined
                        System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[3] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[3] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        if (res.next()) {
                            //preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");
                            //System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");

//                            try {
//                                preparedStatement.executeUpdate();
//                                System.out.println("success adding job");
//                                printStream.println("success");
//                                preparedStatement.close();
//                            } catch (Exception e) {
//                                System.out.println("failed adding job");
//                                printStream.println("failed");
//                                preparedStatement.close();
//                            }
                            System.out.println("Already joined job");
                            printStream.println("succes-" + ID_job + "-" + Qty_already);
                        } else {
                            System.out.println("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`,`Finished`) VALUES (NULL, '" + data[3] + "', '" + ID_job + "', 'Member','no');");
                            preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`,`Finished`) VALUES (NULL, '" + data[3] + "', '" + ID_job + "', 'Member','no');");

                            try {
                                preparedStatement.executeUpdate();
                                System.out.println("success join job");
                                //printStream.println("success");
                                preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println("failed join job");
                                //printStream.println("failed");
                                preparedStatement.close();
                            }

                            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");
                            System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");

                            try {
                                preparedStatement.executeUpdate();
                                System.out.println("success adding job");
                                System.out.println("succes-" + ID_job + "-" + Qty_Cur_w);
                                printStream.println("succes-" + ID_job + "-" + Qty_Cur_w);
                                preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println("failed adding job");
                                printStream.println("failed");
                                preparedStatement.close();
                            }
                        }

                        //add ke current worker
                    } else {
                        System.out.println("failed someone already joined");
                        printStream.println("failed someone already joined");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "apply_reject":
                try {
                    message = data[3] + "-" + data[5] + "-" + data[6];
                    System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("success reply message");
                    printStream.println("success");
                } catch (Exception e) {
                    System.out.println("failed reply");
                    printStream.println("failed reply");
                }
                break;
            case "assign_yes":
                try {
                    //cek if task exist
                    System.out.println("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
                    res = statement.executeQuery("SELECT * FROM task WHERE ID_Task ='" + data[2] + "'");
                    if (res.next()) {
                        condition1 = "sukses";
                    } else {
                        condition1 = "fail";
                    }
                    System.out.println("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
                    res = statement.executeQuery("SELECT ID_User FROM task WHERE ID_Task ='" + data[2] + "' AND ID_User is NULL");
                    if (res.next()) {
                        condition2 = "sukses";
                    } else {
                        condition2 = "fail";
                    }
                    // cek if task exist

                    //get id job
                    String ID_job = "";
                    System.out.println("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    res = statement.executeQuery("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    if (res.next()) {
                        ID_job = res.getString(1);
                        condition3 = "sukses";
                    } else {
                        condition3 = "fail";
                    }
                    //get id job

                    if (condition1.equals("sukses") && condition2.equals("sukses") && condition3.equals("sukses")) {
                        message = data[4] + "-" + data[5] + "-" + data[6];
                        System.out.println(message);
                        //delete that message
                        /*
                        System.out.println("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success delete message");
                        } catch (Exception e) {
                            System.out.println("failed apply");
                            printStream.println("failed");
                        }*/
                        System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success reply message");
                        } catch (Exception e) {
                            System.out.println("failed reply");
                            printStream.println("failed reply");
                        }

                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`task` SET `ID_User` = '" + data[4] + "', `Status`='on' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                        System.out.println("UPDATE `group_work`.`task` SET `ID_User` = '" + data[4] + "', `Status`='on' WHERE `task`.`ID_Task` = '" + data[2] + "';");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success apply");
                            printStream.println(ID_job + "-" + data[4]);
                        } catch (Exception e) {
                            System.out.println("failed apply");
                            printStream.println("failed");
                        }
                        //cek if already joined
                        /*
                        System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[3] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[3] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        if (res.next()) {
                            System.out.println("Already joined job");
                            printStream.println("success");
                            preparedStatement.close();
                        } else {
                            System.out.println("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[3] + "', '" + ID_job + "', 'Member');");
                            preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`) VALUES (NULL, '" + data[3] + "', '" + ID_job + "', 'Member');");

                            try {
                                preparedStatement.executeUpdate();
                                System.out.println("success join job");
                                printStream.println("success");
                                preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println("failed join job");
                                printStream.println("failed");
                                preparedStatement.close();
                            }
                        }*/

                    } else {
                        System.out.println("failed someone already joined");
                        printStream.println("failed someone already joined");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case "assign_reject":
                try {
                    message = data[4] + "-" + data[5] + "-" + data[6];
                    System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("success reply message");
                    printStream.println("success");
                } catch (Exception e) {
                    System.out.println("failed reply");
                    printStream.println("failed reply");
                }
                break;
            case "invite_yes":
                try {
                    //get id job
                    String ID_job = "";
                    System.out.println("SELECT ID_Job FROM job WHERE ID_Job ='" + data[2] + "'");
                    res = statement.executeQuery("SELECT ID_Job FROM job WHERE ID_Job ='" + data[2] + "'");
                    if (res.next()) {
                        ID_job = res.getString(1);
                        condition3 = "sukses";
                    } else {
                        condition3 = "fail";
                    }
                    //get id job

                    //get worker qty
                    System.out.println("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Current_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    Qty_Cur_w = 1 + Integer.parseInt(res.getString(1));

                    System.out.println("SELECT Max_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Max_Worker FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    Qty_Max_w = Integer.parseInt(res.getString(1));
                    //get worker qty

                    if (condition3.equals("sukses")) {
                        message = data[4] + "-" + data[5] + "-" + data[6];
                        System.out.println(message);
                        //delete that message
                        /*
                        System.out.println("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        preparedStatement = localconnection_thread.prepareStatement("DELETE FROM `group_work`.`message` WHERE `message`.`message` = '"+message+"'");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success delete message");
                        } catch (Exception e) {
                            System.out.println("failed apply");
                            printStream.println("failed");
                        }*/
                        System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'accepted' WHERE `message`.`message` = '" + message + "';");
                        try {
                            preparedStatement.executeUpdate();
                            System.out.println("success reply message");
                        } catch (Exception e) {
                            System.out.println("failed reply");
                        }

                        //cek if already joined
                        System.out.println("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[4] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        res = statement.executeQuery("SELECT ID_User, ID_Job, Role FROM grouping WHERE ID_User ='" + data[4] + "' AND ID_Job ='" + ID_job + "' AND Role='Member'");
                        if (res.next()) {
                            //preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");
                            //System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");

//                            try {
//                                preparedStatement.executeUpdate();
//                                System.out.println("success adding job");
//                                //printStream.println("success");
//                                //preparedStatement.close();
//                            } catch (Exception e) {
//                                System.out.println("failed adding job");
//                                //printStream.println("failed");
//                                //preparedStatement.close();
//                            }
                            System.out.println("Already joined job");
                            printStream.println("success");
                            preparedStatement.close();
                        } else {
                            System.out.println("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`,`Finished`) VALUES (NULL, '" + data[4] + "', '" + ID_job + "', 'Member','no');");
                            preparedStatement = localconnection_thread.prepareStatement("INSERT INTO `group_work`.`grouping` (`ID_Grouping`, `ID_User`, `ID_Job`, `Role`,`Finished`) VALUES (NULL, '" + data[4] + "', '" + ID_job + "', 'Member','no');");

                            try {
                                preparedStatement.executeUpdate();
                                System.out.println("success join job");
                                //printStream.println("success");
                                //preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println("failed join job");
                                //printStream.println("failed");
                                //preparedStatement.close();
                            }

                            preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");
                            System.out.println("UPDATE `group_work`.`job` SET `Current_Worker` = '" + Qty_Cur_w + "' WHERE `job`.`ID_Job` = '" + ID_job + "';");

                            try {
                                preparedStatement.executeUpdate();
                                System.out.println("success adding job");
                                printStream.println(ID_job + "-" + Qty_Cur_w);
                                preparedStatement.close();
                            } catch (Exception e) {
                                System.out.println("failed adding job");
                                printStream.println("failed");
                                preparedStatement.close();
                            }
                        }

                    } else {
                        System.out.println("failed job unavaible");
                        printStream.println("failed job unavaible");
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case "invite_reject":
                try {
                    message = data[3] + "-" + data[5] + "-" + data[6];
                    System.out.println("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`message` SET `confirmation` = 'rejected' WHERE `message`.`message` = '" + message + "';");
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    preparedStatement.executeUpdate();
                    System.out.println("success reply message");
                    printStream.println("success");
                } catch (Exception e) {
                    System.out.println("failed reply");
                    printStream.println("failed reply");
                }
                break;
            case "UpVote":
                try {
                    //System.out.println("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    //res = statement.executeQuery("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    //res.next();
                    String ID_job = data[2];

                    System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    String job_owner = res.getString(1);

                    message = data[3] + "-vote-" + job_owner + "-" + ID_job + "-";
                    System.out.println("" + message);

                    System.out.println("SELECT UpVote FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT UpVote FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    int upVote = res.getInt(1);
                    upVote++;

                    System.out.println("UPDATE `group_work`.`job` SET `UpVote`= '" + upVote + "' where ID_Job = '" + ID_job + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `UpVote`= '" + upVote + "' where ID_Job = '" + ID_job + "'");
                    preparedStatement.executeUpdate();

                    System.out.println("Delete From `group_work`.`message` where message='" + message + "'");
                    preparedStatement = localconnection_thread.prepareStatement("Delete From `group_work`.`message` where message='" + message + "'");
                    preparedStatement.executeUpdate();

                    //get reward
                    FuncGetRewardSingle(data[3], ID_job);
                    printStream.println("sukses");
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                    printStream.println("failed sql");
                }

                break;
            case "DownVote":
                try {
                    //System.out.println("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    //res = statement.executeQuery("SELECT ID_Job FROM task WHERE ID_Task ='" + data[2] + "'");
                    //res.next();
                    String ID_job = data[2];

                    System.out.println("SELECT Owner FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT Owner FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    String job_owner = res.getString(1);

                    message = data[3] + "-vote-" + job_owner + "-" + ID_job + "-";

                    System.out.println("SELECT DownVote FROM job WHERE ID_Job ='" + ID_job + "'");
                    res = statement.executeQuery("SELECT DownVote FROM job WHERE ID_Job ='" + ID_job + "'");
                    res.next();
                    int downVote = res.getInt(1);
                    downVote++;

                    System.out.println("UPDATE `group_work`.`job` SET `DownVote`= '" + downVote + "' where ID_Job = '" + ID_job + "'");
                    preparedStatement = localconnection_thread.prepareStatement("UPDATE `group_work`.`job` SET `DownVote`= '" + downVote + "' where ID_Job = '" + ID_job + "'");
                    preparedStatement.executeUpdate();

                    System.out.println("Delete From `group_work`.`message` where message='" + message + "'");
                    preparedStatement = localconnection_thread.prepareStatement("Delete From `group_work`.`message` where message='" + message + "'");
                    preparedStatement.executeUpdate();

                    //get reward
                    FuncGetRewardSingle(data[3], ID_job);
                    printStream.println("sukses");
                } catch (SQLException ex) {
                    Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
                    printStream.println("failed sql");
                }

                break;
        }

    }
    //FUNCTIONALITY

    //MESSAGE SYSTEM
    public void FuncViewWorker(String[] data) {
        try {
            System.out.println("This is view worker------------------------------------------------------------------------");
            System.out.println("select ID_User FROM grouping where role ='Member' and ID_Job = '" + data[1] + "' ");
            res = statement.executeQuery("select ID_User FROM grouping where role ='Member' and ID_Job = '" + data[1] + "' ");
            String append;
            String appendix = "";
            int i = 0;
            while (res.next()) {
                System.out.println("result : " + res.getString(1));
                append = res.getString(1);
                if (i == 0) {
                    appendix = append;
                } else {
                    appendix = appendix + "-" + append;
                }
                i++;

            }
            System.out.println("Sending :" + appendix);
            if (appendix.equals("")) {
                System.out.println("failed No Worker");
                printStream.println("failed No Worker");
            } else {
                printStream.println(appendix);
            }

            statement.close();

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void FuncDetailTask(String[] data) {
        try {
            String ID_Task = data[1];
            System.out.println("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Task ='" + data[1] + "'");
            res = statement.executeQuery("SELECT Name,Description,Type,Difficulty,ID_Job,Completed,ID_Task,ID_User,Due_Time FROM task WHERE ID_Task ='" + data[1] + "'");
            String list = "";

            i = 0;
            if (res.next()) {
                System.out.println("result Name: " + res.getString(1));
                System.out.println("result Desc: " + res.getString(2));
                System.out.println("result Type: " + res.getString(3));
                System.out.println("result Diff: " + res.getString(4));
                System.out.println("result IDJO: " + res.getString(5));
                System.out.println("result COMP: " + res.getString(6));
                System.out.println("result IDTA: " + res.getString(7));
                System.out.println("result WORK: " + res.getString(8));
                System.out.println("result Time: " + res.getString(9));

                list = res.getString(1) + "-" + res.getString(2) + "-" + res.getString(3) + "-" + res.getString(4) + "-" + res.getString(5) + "-" + res.getString(6) + "-" + res.getString(7) + "-" + res.getString(8) + "-," + res.getString(9);

            }
            System.out.println("Sending :" + list);
            if (list.equals("")) {
                System.out.println("failed No Task");
                printStream.println("failed No Task");
            } else {
                System.out.println("sukses");
                printStream.println(list);
            }
            statement.close();

        } catch (SQLException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //FIREBASE
    //Post Data
    private void postData(String topic) throws IOException {
        String[] data = topic.split("-");
        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            //Create data to send to server
            JSONObject dataToSend = new JSONObject();
            JSONObject dataToSend2 = new JSONObject();
            System.out.println("json object");
            System.out.println("body " + data[1]);
            System.out.println("title " + data[2]);
            System.out.println("to " + data[0]);

            dataToSend2.put("body", data[1]);
            dataToSend2.put("title", data[2]);
            dataToSend.put("notification", dataToSend2);
            dataToSend.put("to", "/topics/" + data[0]);

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
                bufferedWriter.close();
            }
        }
        System.out.println("result = " + result.toString());
    }

    //upload to firebase
    private void postFB(String type, String docRef, String[] data, String[] date) {
        try {
            System.out.println("Type : " + type);
            System.out.println("Write to : " + docRef);
            DocumentReference noteRef = db.document(docRef);
            ApiFuture<WriteResult> result1;
            switch (type) {
                case "create_job":
                    result1 = noteRef.set(new createJob(data[1], data[2], date[1] + " " + date[2], data[4], "0", "on", data[3]));
                    System.out.println("Update time : " + result1.get().getUpdateTime());
                    break;
                case "create_task":
                    result1 = noteRef.set(new createTask(data[1], data[2], data[4], data[3], date[1], date[2], "none", "on"));
                    System.out.println("Update time : " + result1.get().getUpdateTime());
                    break;
                case "messaging":
                    break;
                case "abandon":
                    break;
                case "finished":
                    break;
                case "update":
                    result1 = noteRef.update("slotnow", "100");
                    System.out.println("Update time : " + result1.get().getUpdateTime());
                    break;
                default:
                    System.out.println("Unknown");
                    break;
            }
            System.out.println("FB process done");
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Thread_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
