package com.studios.digiwallet.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.studios.digiwallet.Models.Transaction;
import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.R;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.sdf;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class DepositMoneyActivity extends AppCompatActivity {

    EditText etCard, etName, etExpiry, etCVV, etAmount;
    ImageView ivSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_money);
        init();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar10);
        setSupportActionBar(myToolbar);
        setTitle("");
        etCard = findViewById(R.id.etCard_DepositMoney);
        etName = findViewById(R.id.etName_DepositMoney);
        etExpiry = findViewById(R.id.etExpiry_DepositMoney);
        etCVV = findViewById(R.id.etCVV_DepositMoney2);
        etAmount = findViewById(R.id.etAmount_DepositMoney);
        ivSend = findViewById(R.id.imvSend_DepositMoney);
        etCVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>3){
                    etCVV.setText(etCVV.getText().toString().substring(0, 3));
                    etCVV.setSelection(etCVV.getText().length());
                }
            }
        });
        etCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>16){
                    etCard.setText(etCard.getText().toString().substring(0, 16));
                    etCard.setSelection(etCard.getText().length());
                }
            }
        });
        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(before==0 && s.length()==2){
                    etExpiry.append("/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>7){
                    etExpiry.setText(etExpiry.getText().toString().substring(0, 7));
                    etExpiry.setSelection(etExpiry.getText().length());
                }
            }
        });
        ivSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if(!isEmpty()){
                    if (checkExpiry()){
                        if(checkBalance()) {
                            depositMoney(Float.parseFloat(etAmount.getText().toString()));
                            createTransaction(Float.parseFloat(etAmount.getText().toString()));
                            setResult(RESULT_OK);
                            finish();
                        }
                        else
                            Toast.makeText(DepositMoneyActivity.this, "Not enough Funds", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(DepositMoneyActivity.this, "Your Card is Expired", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(DepositMoneyActivity.this, "Please input all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createTransaction(final float x) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction t = new Transaction("Money Deposit", "", "", activeUser.fName + " " + activeUser.lName, activeUser.phone, etName.getText().toString(), etCard.getText().toString(), x, 0.0f, time, "IN");
        HashMap<String, String> users = new HashMap<>();
        users.put("Title", t.title);
        users.put("Receiver Name", t.recName);
        users.put("Receiver Number", t.recNumber);
        users.put("Sender Name", t.senderName);
        users.put("Sender Card Number", t.senderNumber);
        users.put("Amount", t.amount+"");
        users.put("Purpose", t.purpose);
        users.put("Fee", t.fee+"");
        users.put("Time", sdf.format(t.timestamp));
        users.put("Direction", t.direction);
        String header = sdf.format(t.timestamp);
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Transactions").child(header).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DepositMoneyActivity.this, "Receiver Transaction added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(DepositMoneyActivity.this, "Error Creating Transaction", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void depositMoney(final float x) {
        float y = activeUser.balance+x;
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Balance").setValue(y)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DepositMoneyActivity.this, "Rs. " + x + " Successfully Deposited in your account", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(DepositMoneyActivity.this, "Error Depositing", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean checkBalance() {
        return Float.parseFloat(etAmount.getText().toString())<5000;
    }

    private boolean checkExpiry() {
        String[] res = etExpiry.getText().toString().split("/",2);
        Date d = new Date(System.currentTimeMillis());
        if(Integer.parseInt(res[0])>d.getYear())
            return false;
        else if(Integer.parseInt(res[0])==d.getYear() && Integer.parseInt(res[1])>d.getMonth())
            return false;
        return true;
    }

    private boolean isEmpty() {
        return etCard.getText().toString().equals("") || etName.getText().toString().equals("") || etExpiry.getText().toString().equals("") || etCVV.getText().toString().equals("") || etAmount.getText().toString().equals("");
    }
}