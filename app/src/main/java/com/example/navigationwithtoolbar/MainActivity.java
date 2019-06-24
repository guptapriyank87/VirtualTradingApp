package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
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

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView signupButton;
    TextView btnForgotPassword;
    EditText etusername,etpassword;
    Boolean homeCode = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SESSION_ID= null;
    public static final String SESSION_USER = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SESSION_ID != null){
            Intent in = new Intent(MainActivity.this,HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
        if (false){
            Intent in = new Intent(MainActivity.this,HomeActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        signupButton = findViewById(R.id.btnsignup);
        etusername = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        loginButton = findViewById(R.id.btnlogin);
        btnForgotPassword = findViewById(R.id.btnforgetpassword);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(in);
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,SignUp.class);
                startActivity(in);
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,ForgetPassword.class);
                startActivity(in);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });


    }
    public void logIn(){
        String us = etusername.getText().toString();
        String ps = etpassword.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker =new BackgroundWorker(this);
        try {
            backgroundWorker.execute(type, us, ps);
            backgroundWorker.wait(300);
            backgroundWorker.cancel(true);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public static class BackgroundWorker extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;
        String ip = "192.168.43.216";
        boolean loginStatus = false;
        String user = "nouser";


        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String login_url = "http://"+ip+"/pgr/login.php";

            if (type.equals("login")){
                try {
                    String email = strings[1];
                    String password = strings[2];
                    user = email;
                    Log.i("status","inside the login try catch");
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    Log.i("status","string post_data concatenation successful");

                    bufferedWriter.write(post_data);
                    Log.i("status","bufferedWriter.write(post_data) executed successfully");

                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    //reading response for feedback
                    Log.i("status","now reading feedback");


                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result = "";
                    String line  = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("LoginStatus");
        }

        @Override
        protected void onProgressUpdate(String... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                boolean loginSuccessful = s.contains("loginsuccess");
                boolean incorrectPassword = s.contains("incorrectpassword");
                boolean userNotFound = s.contains("userdoesnotexist");
                Log.i("SessionId",s);

                if (loginSuccessful) {
                    //alertDialog.setMessage("Login Successful!");
                    Intent i = new Intent(context,HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    //Generating SESSION_ID
                    s = s.replace("loginsuccess","");

                   /* SharedPreferences Pref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor Editor = Pref.edit();
                    Editor.putString("SESSION_ID",s);
                    Editor.putString("SESSION_USER",user);
                    Editor.apply();
                    SharedPreferences newPref = context.getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
                    System.out.println("Session id:"+newPref.getString(SESSION_ID,"")+"\nSession User:"+newPref.getString(SESSION_USER,""));
                    */
                    context.startActivity(i);
                    return;
                } else if (incorrectPassword) {
                    alertDialog.setMessage("Incorrect Password!");
                    Log.i("info",s);
                } else if (userNotFound) {
                    alertDialog.setMessage("User does not exist!");
                } else {
                    alertDialog.setMessage("Unknown error!");
                    Log.i("error", s);
                }
                alertDialog.show();
            }catch (Exception e){
                e.printStackTrace();
                Log.i("Exception","e");
            }
        }

        public BackgroundWorker(Context ctx) {
            context = ctx;
        }
        public boolean ifLoginSuccess(){
            return loginStatus;

        }
    }
}
