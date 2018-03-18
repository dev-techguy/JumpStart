package com.tecksolke.jumpstart;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

/**
 * Created by techguy on 3/1/18.
 */

public class Login_Processing extends AsyncTask<String,Void,String>{

    TemporaryDB temporaryDB;
    HttpURLConnection httpURLConnection;
    Context context;


    Login_Processing(Context ctx) {
        context = ctx;
    }

    //create a notification for user
    private void notifyUser() {
        //message to display
        String notifMessage = "Login Failed due to Internet Failure";
        //build a return activity
        Intent myIntent = new Intent(context, Login.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        //build a notification to the user
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("Login Status!")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setWhen(0)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notifMessage))
                        .setColor(Color.parseColor("#9dffffff"))
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentText(notifMessage);
        //get an instance of the notification service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //post the notification to the bar
        notificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            String login_url = env.urlLogin;
            URL url = new URL(login_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data =
                    URLEncoder.encode("param_username", "UTF-8") + "=" +
                            URLEncoder.encode(strings[0], "UTF-8") + "&" +
                            URLEncoder.encode("param_password", "UTF-8") + "=" +
                            URLEncoder.encode(strings[1], "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            ///response from post requets
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "", line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            //JSONObject object = new JSONObject(result);

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return result;
        } catch (MalformedURLException e) {
            notifyUser();
        } catch (IOException e) {
            notifyUser();
        }
        return null;
    }

    @Override
    protected void  onPreExecute() {
        super.onPreExecute();
        //Register.spinner.setVisibility(View.VISIBLE);
        Login.progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //set the progress dialog to false
        //Register.spinner.setVisibility(View.GONE);
        Login.progressDialog.hide();
        //call the notification dialog
        final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder.getInstance(context);
        if(s.equalsIgnoreCase("200")){
            String username = Login.userName.getText().toString();
            //pass valor username
            addUserToSqlite(username);

            //direct to another activity
            Intent intent = new Intent(context, Category.class);
            context.startActivity(intent);
            ((Login) context).finish();
        }else if(s.equalsIgnoreCase("401")){
            niftyDialogBuilder
                    .withTitle("Login Status")
                    .withTitleColor("#9dffffff")
                    .withMessage("Wrong details please try again")
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("OK")
                    .withDuration(700)
                    .isCancelable(false)
                    .withEffect(Effectstype.Slidetop)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            niftyDialogBuilder.cancel();
                        }
                    })
                    .show();
        }else{
            niftyDialogBuilder
                    .withTitle("Network Status")
                    .withTitleColor("#9dffffff")
                    .withMessage("Low Network Bandwidth.\nPlease Check Your Internet Data.")
                    .withMessageColor("#9dffffff")
                    .withDialogColor("#2A3342")
                    .withButton1Text("OK")
                    .withDuration(700)
                    .isCancelable(false)
                    .withEffect(Effectstype.Shake)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            niftyDialogBuilder.cancel();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //add to database of SQLITE
    private void addUserToSqlite(String username){
        temporaryDB = new TemporaryDB(context);
        Boolean dbResults = temporaryDB.insertData(username);
        if (dbResults) {
            Log.v(TAG, "save ok.");
        } else {
            Log.v(TAG, "Failed.");
        }
    }
}
