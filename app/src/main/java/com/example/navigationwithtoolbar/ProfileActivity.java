package com.example.navigationwithtoolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    TextView name,email,dob,password,gender;
    EditText phone;
    Toolbar toolbar;

    ImageButton password_edit,phone_edit,phone_confirm;;
    Constants constants;
    WebView webView;
    View logo_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        constants =  new Constants(this);
        name =findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        dob = findViewById(R.id.profile_dob);
        password = findViewById(R.id.profile_password);
        gender = findViewById(R.id.profile_gender);
        phone = findViewById(R.id.profile_phone);
        phone_edit = findViewById(R.id.profile_phone_edit);
      //  phone_confirm = findViewById(R.id.profile_phone_edit_check);
        password_edit = findViewById(R.id.profile_password_edit);
        name.setText(constants.getName());
        email.setText(constants.getEmail());
        dob.setText(constants.getDob());
        if(constants.getGender().equals("M")){
            gender.setText("Male");
        }else{
            gender.setText("Female");
        }
        password.setText(constants.getPassword_coded());
        phone.setText(constants.getPhone());


        phone_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "working", Toast.LENGTH_SHORT).show();
                if(!phone.isEnabled()){
                    phone_edit.setBackgroundResource(R.drawable.ic_check);
                    phone.setEnabled(true);
                    phone.setSelection(phone.getText().length());
                    phone.requestFocus();
                }else{
                    phone_edit.setBackgroundResource(R.drawable.ic_edit);
                    phone.setEnabled(false);
                }

            }
        });
        toolbar = findViewById(R.id.profile_toolbar);
        logo_back = toolbar.getChildAt(1);
        logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
