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
import android.widget.Spinner;
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
import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.sdf;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class MobileLoadActivity extends AppCompatActivity {

    private final int REQUEST_SELECT_PHONE_NUMBER = 1;

    Spinner snrNetworks;
    EditText etNumber, etAmount;
    ImageView ivContacts, ivSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_load);
        init();
        listeners();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar7);
        setSupportActionBar(myToolbar);
        setTitle("");
        snrNetworks = findViewById(R.id.snrNetworks_MobileLoad);
        etNumber = findViewById(R.id.etNumber_MobileLoad);
        etAmount = findViewById(R.id.etAmount_MobileLoad);
        ivContacts = findViewById(R.id.imvContacts_MobileLoad);
        ivSend = findViewById(R.id.imvSend_MobileLoad);
    }

    private void listeners() {
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>11){
                    etNumber.setText(etNumber.getText().toString().substring(0, 11));
                    etNumber.setSelection(etNumber.getText().length());
                }
            }
        });

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

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty()){
                    if(checkBalance()){
                        mobileLoad(Float.parseFloat(etAmount.getText().toString()));
                        createTransaction(Float.parseFloat(etAmount.getText().toString()));
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                        Toast.makeText(MobileLoadActivity.this, "Insufficient Balance", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MobileLoadActivity.this, "Please Input all fields", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createTransaction(float x) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Transaction t = new Transaction("Mobile Load", "", "", activeUser.fName + " " + activeUser.lName, activeUser.phone, snrNetworks.getSelectedItem().toString(), etNumber.getText().toString(), x, 0.0f, time, "OUT");
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
                            Toast.makeText(MobileLoadActivity.this, "Sender Transaction added", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MobileLoadActivity.this, "Error Creating Transaction", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void mobileLoad(final float x) {
        float y = activeUser.balance-x;
        firebase.child(User.encodeUserEmail(activeUser.email)+"-"+activeUser.cnic).child("Balance").setValue(y)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MobileLoadActivity.this, "Rs. " + x + " successfully paid to " + snrNetworks.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(MobileLoadActivity.this, "Error Sending", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private boolean checkBalance() {
        return activeUser.balance>=Float.parseFloat(etAmount.getText().toString());
    }

    private boolean isEmpty() {
        return etAmount.getText().toString().equals("") || etNumber.getText().toString().length()<11 || snrNetworks.getSelectedItemPosition()==0;
    }

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
            }
        }
    }
}