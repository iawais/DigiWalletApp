package com.studios.digiwallet.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.MyApplication;
import com.studios.digiwallet.R;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class SignupActivity extends AppCompatActivity {

    EditText etFName, etLName, etCNIC, etEmail, etPhone, etPassword, etCPassword;
    ImageView btnSignup;
    CheckBox cbxTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
        listeners();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("");
        etFName = findViewById(R.id.etFirstName_Signup);
        etLName = findViewById(R.id.etLastName_Signup);
        etEmail = findViewById(R.id.etEmail_Signup);
        etPhone = findViewById(R.id.etPhone_Signup);
        etCNIC = findViewById(R.id.etCNIC_Signup);
        etPassword = findViewById(R.id.etPassword_Signup);
        etCPassword = findViewById(R.id.etConfirmPassword_Signup);
        btnSignup = findViewById(R.id.btnSignup_Signup);
        cbxTerms = findViewById(R.id.checkBox);
        etEmail.setText(getIntent().getStringExtra("Email"));
    }

    private void listeners(){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if(!isEmpty()) {
                    if (passMatch()) {
                        User u = new User(etFName.getText().toString(), etLName.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(), etCNIC.getText().toString(), etPassword.getText().toString());
                        int res = checkSameEmailCNIC(u);
                        if(res==0){
                            addToDB(u);
                        }
                        else if(res==1){
                            Toast.makeText(SignupActivity.this, "Email already exists! Please Sign in", Toast.LENGTH_LONG).show();
                        }
                        else if(res==2){
                            Toast.makeText(SignupActivity.this, "CNIC already exists!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(SignupActivity.this, "Password do not match! Please re-Enter Passwords", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(SignupActivity.this, "Please ensure all Fields are filled.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean passMatch() {
        return etPassword.getText().toString().equals(etCPassword.getText().toString());
    }

    private boolean isEmpty() {
        return (etFName.getText().toString().equals("") || etLName.getText().toString().equals("") || etPhone.getText().toString().equals("") || etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("") || etCPassword.getText().toString().equals("") || etCNIC.getText().toString().equals(""));
    }

    public int checkSameEmailCNIC(final User u) {
        for(User user: MyApplication.allUsers) {
            if (user.email.equals(u.email))
                return 1;
            else if (user.cnic.equals(u.cnic))
                return 2;
        }
        return 0;
    }

    public void addToDB(User u){
        HashMap<String, String> users = new HashMap<>();
        users.put("FName", u.fName);
        users.put("LName", u.lName);
        users.put("Email", u.email);
        users.put("Phone", u.phone);
        users.put("CNIC", u.cnic);
        users.put("Password", u.pass);
        users.put("Balance", u.balance+"");
        String header = u.encodeUserEmail(u.email)+"-"+u.cnic;
        try {
            firebase.child(header).setValue(users)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "User Successfully Created", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "User Creation Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch(Exception ex){
            Log.d("Firebase Error", ex.getMessage());
        }
    }
}