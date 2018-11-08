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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author Adrian
 */
public class test {

    public static void main(String[] args) throws IOException {
        postData();
    }

    private static void postData() throws IOException {

        StringBuilder result = new StringBuilder();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            //Create data to send to server
            JSONObject dataToSend = new JSONObject();
            JSONObject dataToSend2 = new JSONObject();

            dataToSend2.put("body", "This is a Firebase Cloud Messaging Topic Message!");
            dataToSend2.put("title", "This is a Firebase Cloud Messaging Topic Message!");
            dataToSend.put("notification", dataToSend2);
            dataToSend.put("to", "/topics/test");

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
        System.out.println("result = "+result.toString());
    }

}
