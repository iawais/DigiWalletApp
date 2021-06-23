package com.studios.digiwallet.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.studios.digiwallet.Models.Transaction;
import com.studios.digiwallet.Models.User;
import com.studios.digiwallet.R;

import java.sql.Timestamp;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.allUsers;
import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.refresh;
import static com.studios.digiwallet.MyApplication.sdf;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class SendMoneyToMobileAccountActivity extends AppCompatActivity {

    private final int REQUEST_SELECT_PHONE_NUMBER = 99;
    ImageView ivSend, ivContacts;
    EditText etNumber, etAmount;
    TextView tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_to_mobile_account);
        init();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        setTitle("");
        etNumber = findViewById(R.id.etNumber_SendMoneyToMobileAccount);
        etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkRecipient();
            }
        });
        etAmount = findViewById(R.id.etAmount_SendMoneyToMobileAccount);
        etAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkBalance();
            }
        });
        tvName = findViewById(R.id.tvName_SendMoneyToMobileAccount);

        ivSend = findViewById(R.id.imvSend_SendMoneyToMobileAccount);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if(!isEmpty()) {
                    float money = Float.parseFloat(etAmount.getText().toString());
                    if (checkBalance()) {
                        User rec = checkRecipient();
                        if (rec!=null) {
                            sendMoney(rec, money);
                            createTransaction(rec, money);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Recipient Does not have a Mobile Account", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(SendMoneyToMobileAccountActivity.this, "Not Enough Balance", Toast.LENGTH_LONG).show();
                    }
                } else{
                    Toast.makeText(SendMoneyToMobileAccountActivity.this, "Please Input All Fields", Toast.LENGTH_LONG).show();
                }
            }
        });

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>11){
                    etNumber.setText(etNumber.getText().toString().substring(0, 11));
                    etNumber.setSelection(etNumber.getText().length());
                }
            }
        });

        ivContacts = findViewById(R.id.imvContacts_SendMoneyToMobileAccount);
        ivContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
                }
            }
        });
    }

    private void createTransaction(User rec, float money){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction t = new Transaction("Money Transfer - MA", "", "", activeUser.fName + " " + activeUser.lName, activeUser.phone, rec.fName + " " + rec.lName, rec.phone, money, 0.0f, time, "OUT");
        HashMap<String, String> users = new HashMap<>();
        users.put("Title", t.title);
        users.put("Receiver Name", t.recName);
        users.put("Receiver Number", t.recNumber);
        users.put("Sender Name", t.senderName);
        users.put("Sender Number", t.senderNumber);
        users.put("Amount", t.amount+"");
        users.put("Fee", t.fee+"");
        users.put("Time", sdf.format(t.timestamp));
        users.put("Direction", "OUT");
        String header = sdf.format(t.timestamp);
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Transactions").child(header).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Sender Transaction added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Error Creating Transaction", Toast.LENGTH_LONG).show();

                        }
                    }
                });
        users.remove("Direction");
        users.put("Direction", "IN");
        firebase.child(User.encodeUserEmail(rec.email)+"-"+rec.cnic).child("Transactions").child(header).setValue(users)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Receiver Transaction added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Error Creating Transaction", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean isEmpty() {
        return etAmount.getText().toString().equals("") || etNumber.getText().toString().equals("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            assert contactUri != null;
            @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(contactUri, projection,
                    null, null, null);
            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                // Do something with the phone number
                //...
                number = number.replace(" ", "");
                if(number.startsWith("+92")) {
                    number = number.substring(3);
                    number = "0" + number;
                }
                etNumber.setText(number);
                checkRecipient();
            }
        }
    }

    private void sendMoney(final User rec, final float x){
        float y = activeUser.balance-x;
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Balance").setValue(y)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Rs. " + x + " sent to " + rec.fName + " Mobile Account " + rec.phone, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SendMoneyToMobileAccountActivity.this, "Error Sending" + rec.phone, Toast.LENGTH_LONG).show();

                        }
                    }
                });
            y = rec.balance + x;
            firebase.child(User.encodeUserEmail(rec.email) + "-" + rec.cnic).child("Balance").setValue(y)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SendMoneyToMobileAccountActivity.this, "Rs. " + x + " received by " + rec.fName + " Mobile Account " + rec.phone, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SendMoneyToMobileAccountActivity.this, "Error Receiving" + rec.phone, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private User checkRecipient(){
        //if(readAll()) {
            refresh();
            for (User u : allUsers) {
                if (u.phone.equals(etNumber.getText().toString())) {
                    tvName.setVisibility(View.VISIBLE);
                    tvName.setText(u.fName + " " + u.lName);
                    etNumber.setLinkTextColor(R.color.Green);
                    return u;
                }
            }
            etNumber.setLinkTextColor(R.color.Red);
            tvName.setVisibility(View.GONE);
            tvName.setText("");
        //}
        return null;
    }

    @SuppressLint("ResourceAsColor")
    private boolean checkBalance(){
        if(!etAmount.getText().toString().equals("")) {
            if (Float.parseFloat(etAmount.getText().toString())<=activeUser.balance){
                etAmount.setLinkTextColor(R.color.Green);
                return true;
            }
            etAmount.setLinkTextColor(R.color.Red);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}