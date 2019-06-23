package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private String TAG = "SignUpActivity";
    TextView dateTextView;
    Button signUp;
    String age, date, stringName, stringEmail, stringPhone, stringGender, stringDob, stringPassword;
    int d, m, y;
    EditText name, email, number, password, confirmPassword, otp;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    RadioGroup maleFemale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dateTextView = findViewById(R.id.selectDate);


        Button btn;


        name = findViewById(R.id.etrname);
        email = findViewById(R.id.etremail);
        number = findViewById(R.id.etrphone);
        signUp = findViewById(R.id.btnRSignup);
        maleFemale = findViewById(R.id.maleFemale);
        password = findViewById(R.id.etrpassword);
        confirmPassword = findViewById(R.id.etrconfirmpassword);
        date = "Select Date of Birth";
        btn = findViewById(R.id.verifyIntent);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.i("Date", year + "/" + month + "/" + dayOfMonth);
                month = month + 1;
                d = dayOfMonth;
                m = month;
                y = year;
                date = dayOfMonth + "/" + month + "/" + year;
                dateTextView.setText(date);
                date = year + "-" + month + "-" + dayOfMonth;
                dateTextView.setTextSize(22);
            }
        };
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                    return;
                }
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Required");
                    return;
                } else if (!isEmailValid(email.getText().toString())) {
                    email.setError("Valid email is required");
                    return;
                }
                if (number.getText().length() < 10) {
                    number.setError("Valid phone number is required");
                    return;
                }
                String value = "No value";
                try {
                    value =
                            ((RadioButton) findViewById(maleFemale.getCheckedRadioButtonId()))
                                    .getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("MF", value);
                if (value.equals("No value")) {
                    Toast.makeText(SignUp.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                age = getAge(y, m, d);
                Log.i("Age", age);
                if (date.equals("Select Date of Birth")) {
                    Toast.makeText(SignUp.this, "Please select your DOB", Toast.LENGTH_SHORT).show();
                    return;
                } else if (Integer.parseInt(age) < 18) {
                    Log.i("Age", age);
                    Toast.makeText(SignUp.this, "Sorry, you must be 18+", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() > 0) {
                    if (password.getText().toString().length() < 8) {
                        password.setError("Password must be of minimum 8 characters");
                    } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                        confirmPassword.setError("Password does not match");
                    }
                } else {
                    password.setError("This field can not be empty");
                    return;
                }

                BackgroundWorker backgroundWorker = new BackgroundWorker(SignUp.this);

                stringName = name.getText().toString();
                stringEmail = email.getText().toString();
                stringPassword = password.getText().toString();
                stringGender = getMF();
                stringPhone = number.getText().toString();

                Log.i("Variables", stringName + "   " + stringEmail + "   " + stringPhone + "   " + stringGender + "   " + date + "   " + stringPassword);

                try {
                    backgroundWorker.execute("checkUserExist", stringEmail);
                    if (backgroundWorker.ifUserExist()) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (date.equals("Select Date of Birth")) {
                    Toast.makeText(SignUp.this, "Please select your DOB", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent in = new Intent(SignUp.this, VerifyEmail.class);
                in.putExtra("name", stringName);
                in.putExtra("email", stringEmail);
                in.putExtra("phone", stringPhone);
                in.putExtra("gender", stringGender);
                in.putExtra("dob", date);
                in.putExtra("password", stringPassword);
                startActivity(in);
            }
        });
    }

    public String getMF() {
        RadioButton checkedRadioButton = maleFemale.findViewById(maleFemale.getCheckedRadioButtonId());
        String s = checkedRadioButton.getText().toString();
        if (s.equals("Male")) {
            return "M";
        } else {
            return "F";
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    public class BackgroundWorker extends AsyncTask<String, String, String> {
        Context context;
        AlertDialog alertDialog;

        Boolean signupSuccess=false,userExist=false;
        String ip = "192.168.1.4";
        @Override
        protected String doInBackground(String... strings) {
            String type = strings[0];
            String register_url = "http://"+ip+"/pgr/register.php";
            String userexist_url = "http://"+ip+"/pgr/userexist.php";

            if (type.equals("checkUserExist")){
                try {
                    String email = strings[1];
                    Log.i("status","inside the login try catch");
                    URL url = new URL(userexist_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
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
                    System.out.println(result);
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("register")){
                try {
                    String name = strings[1];
                    String email = strings[2];
                    String phone = strings[3];
                    String gender = strings[4];
                    String dob = strings[5];
                    String password = strings[6];
                    Log.i("status","inside the try catch");
                    URL url = new URL(register_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoOutput(true);
                    Log.i("status","Http url connection established properly");

                    OutputStream outputStream = httpURLConnection.getOutputStream();

                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    Log.i("status","buffer writer working");

                    String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                            +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                            +URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                            +URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"
                            +URLEncoder.encode("dob","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"
                            +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
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
                    System.out.println(result);

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
               boolean insertSuccessful = s.contains("insertsuccessful");
                boolean insertUnsuccessful = s.contains("insertunsuccessful");
                boolean deleteSuccessful = s.contains("deletesuccessful");
                boolean userExist = s.contains("userExist");

                if (insertSuccessful) {
                    signupSuccess = true;
                } else if (insertUnsuccessful) {
                    alertDialog.setMessage("Sorry, Something went wrong!");
                } else if (deleteSuccessful) {
                    alertDialog.setMessage("Delete successful!");
                } else if (userExist) {
                    userExist = true;
                    alertDialog.setMessage("Email already registered!");
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
        public Boolean ifSignUpSuccess(){
            if(signupSuccess==true){
                return true;
            }else {
                return false;
            }
        }
        public Boolean ifUserExist(){
            if(userExist==true){
                return true;
            }else {
                return false;
            }
        }
    }
}