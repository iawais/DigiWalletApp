package com.studios.digiwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.MyApplication;
import com.studios.digiwallet.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.refresh;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    ImageView btnLogin;
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        listeners();
        statusBarColor(this);
    }

    private void init(){
        btnLogin = findViewById(R.id.btnLogin_Login);
        tvSignUp = findViewById(R.id.tvSignUp_Login);
        etEmail = findViewById(R.id.etEmail_SignIn);
        etPassword = findViewById(R.id.etPassword_SignIn);
    }

    private void listeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty()) {
                    int res = checkLogin();
                    if (res==0) {
                        etPassword.setText("");
                        Intent in = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(in);
                    }
                    else if(res==1){
                        Toast.makeText(LoginActivity.this, "No User Found! Try again", Toast.LENGTH_LONG).show();
                    }
                    else if(res==2){
                        Toast.makeText(LoginActivity.this, "Wrong Credentials! Try again", Toast.LENGTH_LONG).show();
                    }
                    else if(res==3){
                        Toast.makeText(LoginActivity.this, "Database Connection Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent up = new Intent(LoginActivity.this, SignupActivity.class);
                up.putExtra("Email", etEmail.getText().toString());
                startActivity(up);
            }
        });
    }

    private int checkLogin() {
       // if(readAll()) {
        refresh();
            for (User user : MyApplication.allUsers) {
                if (user.email.equals(etEmail.getText().toString())) {
                    if (user.pass.equals(etPassword.getText().toString())) {
                        activeUser = user;
                        return 0;
                    } else
                        return 2;
                }
            }
            return 1;
    }

    private boolean isEmpty() {
        return (etEmail.getText().toString().equals("") || etPassword.getText().toString().equals(""));
    }
}