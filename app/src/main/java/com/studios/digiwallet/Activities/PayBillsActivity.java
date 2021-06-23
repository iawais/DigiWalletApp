package com.studios.digiwallet.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.studios.digiwallet.Models.Transaction;
import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.R;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.sdf;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class PayBillsActivity extends AppCompatActivity {

    Spinner snrServices;
    TextView tvName;
    EditText etAmount, etRefNumber;
    ImageView ivService, ivSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);
        init();
        listeners();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar5);
        setSupportActionBar(myToolbar);
        setTitle("");
        tvName = findViewById(R.id.tvName_PayBills);
        snrServices = findViewById(R.id.snrServices_PayBills);
        etAmount = findViewById(R.id.etAmount_PayBills);
        etRefNumber = findViewById(R.id.etRefNumber_PayBills);
        ivService = findViewById(R.id.imvService_PayBills);
        ivSend = findViewById(R.id.imvSend_PayBills);
    }

    private void listeners(){
        snrServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(snrServices.getSelectedItemPosition()==1) {
                    ivService.setImageResource(R.drawable.ic_baseline_electrical_services_24);
                    ivService.setVisibility(View.VISIBLE);
                }
                else if(snrServices.getSelectedItemPosition()==2) {
                    ivService.setImageResource(R.drawable.ic_baseline_water_damage_24);
                    ivService.setVisibility(View.VISIBLE);
                }
                else if(snrServices.getSelectedItemPosition()==3) {
                    ivService.setImageResource(R.drawable.ic_baseline_waves_24);
                    ivService.setVisibility(View.VISIBLE);
                }
                else
                    ivService.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etRefNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>14){
                    etRefNumber.setText(etRefNumber.getText().toString().substring(0, 14));
                    etRefNumber.setSelection(etRefNumber.getText().length());
                }
            }
        });

        etRefNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFocusChange(View view, boolean b) {
                if(etRefNumber.getText().toString().length()==14 && tvName.getVisibility()==View.GONE){
                    Float x = new Random().nextFloat() * 1000;
                    tvName.setText("Rs. " + x);
                    tvName.setVisibility(View.VISIBLE);
                }
                else{
                    tvName.setVisibility(View.GONE);
                }
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty()){
                    if(checkBalance()){
                        payBill(Float.parseFloat(etAmount.getText().toString()));
                        createTransaction(Float.parseFloat(etAmount.getText().toString()));
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                        Toast.makeText(PayBillsActivity.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(PayBillsActivity.this, "Please Input all fields", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createTransaction(final float money) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction t = new Transaction("Bill Payment", "", "", activeUser.fName + " " + activeUser.lName, activeUser.phone, snrServices.getSelectedItem().toString(), etRefNumber.getText().toString(), money, 0.0f, time, "OUT");
        HashMap<String, String> users = new HashMap<>();
        users.put("Title", t.title);
        users.put("Receiver Name", t.recName);
        users.put("Receiver Number", t.recNumber);
        users.put("Sender Name", t.senderName);
        users.put("Sender Number", t.senderNumber);
        users.put("Amount", t.amount+"");
        users.put("Fee", t.fee+"");
        users.put("Time", sdf.format(t.timestamp));
        users.put("Direction", t.direction);
        String header = sdf.format(t.timestamp);
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Transactions").child(header).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PayBillsActivity.this, "Sender Transaction added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PayBillsActivity.this, "Error Creating Transaction", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void payBill(final float x) {
        float y = activeUser.balance-x;
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Balance").setValue(y)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(PayBillsActivity.this, "Rs. " + x + " successfully paid to " + snrServices.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PayBillsActivity.this, "Error Sending", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean isEmpty(){
        return etAmount.getText().toString().equals("") || etRefNumber.getText().toString().length()<14 || snrServices.getSelectedItemPosition()==0;
    }

    private boolean checkBalance(){
        return activeUser.balance>=Float.parseFloat(etAmount.getText().toString());
    }
}