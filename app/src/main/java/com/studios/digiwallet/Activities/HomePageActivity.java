package com.studios.digiwallet.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.studios.digiwallet.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.activeUser;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_SEND_MONEY = 101;
    private final int REQUEST_DEPOSIT_MONEY = 102;
    private final int REQUEST_PAY_BILL = 103;
    private final int REQUEST_MOBILE_LOAD = 104;
    private static int REQUEST_MY_ACCOUNT = 105;
    private static int REQUEST_HISTORY = 106;

    TextView tvName;
    TextView tvBalance;
    TextView[] tvOptions;
    ImageView ivProfile;
    ImageView ivRefresh;
    ImageView[] ivOptions;
    LinearLayout[] llOptions;
    String[] optionsText;
    int[] optionsImages;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if(activeUser==null) {
            Toast.makeText(this, "Please Login First", Toast.LENGTH_LONG).show();
            finish();
        }
        init();
        statusBarColor(this);
    }

    @SuppressLint("SetTextI18n")
    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar8);
        setSupportActionBar(myToolbar);
        setTitle("");
        tvName= findViewById(R.id.tvName_Homepage);
        tvName.setOnClickListener(this);
        tvName.setTag(10);
        tvBalance= findViewById(R.id.tvBalance_Homepage);
        tvBalance.setOnClickListener(this);
        tvBalance.setTag(11);
        ivProfile = findViewById(R.id.ivProfilePhoto_Homepage);
        ivProfile.setOnClickListener(this);
        ivProfile.setTag(12);
        ivRefresh = findViewById(R.id.imvRefresh_Homepage);
        ivRefresh.setOnClickListener(this);
        ivRefresh.setTag(13);

        optionsText = new String[]{"Send Money", "Deposit Money", "Pay Bills", "Mobile Load", "My Account", "History", "Logout"};
        optionsImages = new int[]{R.drawable.ic_baseline_send_24, R.drawable.ic_baseline_call_received_24, R.drawable.ic_baseline_assignment_24, R.drawable.ic_baseline_mobile_screen_share_24, R.drawable.ic_baseline_account_circle_24, R.drawable.ic_baseline_history_24, R.drawable.ic_baseline_power_settings_new_24};

        tvOptions = new TextView[9];
        tvOptions[0] = findViewById(R.id.tvOption1_Homepage);
        tvOptions[1] = findViewById(R.id.tvOption2_Homepage);
        tvOptions[2] = findViewById(R.id.tvOption3_Homepage);
        tvOptions[3] = findViewById(R.id.tvOption4_Homepage);
        tvOptions[4] = findViewById(R.id.tvOption5_Homepage);
        tvOptions[5] = findViewById(R.id.tvOption6_Homepage);
        tvOptions[6] = findViewById(R.id.tvOption7_Homepage);
        tvOptions[7] = findViewById(R.id.tvOption8_Homepage);
        tvOptions[8] = findViewById(R.id.tvOption9_Homepage);

        ivOptions = new ImageView[9];
        ivOptions[0] = findViewById(R.id.imvOption1_Homepage);
        ivOptions[1] = findViewById(R.id.imvOption2_Homepage);
        ivOptions[2] = findViewById(R.id.imvOption3_Homepage);
        ivOptions[3] = findViewById(R.id.imvOption4_Homepage);
        ivOptions[4] = findViewById(R.id.imvOption5_Homepage);
        ivOptions[5] = findViewById(R.id.imvOption6_Homepage);
        ivOptions[6] = findViewById(R.id.imvOption7_Homepage);
        ivOptions[7] = findViewById(R.id.imvOption8_Homepage);
        ivOptions[8] = findViewById(R.id.imvOption9_Homepage);

        llOptions = new LinearLayout[9];
        llOptions[0] = findViewById(R.id.llOption1_Hompage);
        llOptions[1] = findViewById(R.id.llOption2_Hompage);
        llOptions[2] = findViewById(R.id.llOption3_Hompage);
        llOptions[3] = findViewById(R.id.llOption4_Hompage);
        llOptions[4] = findViewById(R.id.llOption5_Hompage);
        llOptions[5] = findViewById(R.id.llOption6_Hompage);
        llOptions[6] = findViewById(R.id.llOption7_Hompage);
        llOptions[7] = findViewById(R.id.llOption8_Hompage);
        llOptions[8] = findViewById(R.id.llOption9_Hompage);

        for(int i=0;i<9;i++){
            if(i<optionsText.length && i<optionsImages.length) {
                tvOptions[i].setText(optionsText[i]);
                ivOptions[i].setImageResource(optionsImages[i]);
                llOptions[i].setOnClickListener(this);
            }
            else{
                tvOptions[i].setVisibility(View.GONE);
                ivOptions[i].setVisibility(View.GONE);
            }
            llOptions[i].setTag(i);
        }

        refresh();
    }

    @SuppressLint({"ShowToast", "SetTextI18n"})
    @Override
    public void onClick(View view) {
        if((int)view.getTag()==getOptionIndex("Send Money")){
            Toast.makeText(this, "Send Money clicked.", Toast.LENGTH_SHORT).show();
            Intent send = new Intent(this, SendMoneyActivity.class);
            startActivityForResult(send, REQUEST_SEND_MONEY);
        }
        else if((int)view.getTag()==getOptionIndex("Deposit Money")){
            Toast.makeText(this, "Deposit Money clicked.", Toast.LENGTH_SHORT).show();
            Intent deposit = new Intent(this, DepositMoneyActivity.class);
            startActivityForResult(deposit, REQUEST_DEPOSIT_MONEY);
        }
        else if((int)view.getTag()==getOptionIndex("Pay Bills")){
            Toast.makeText(this, "Pay Bils clicked.", Toast.LENGTH_SHORT).show();
            Intent bill = new Intent(this, PayBillsActivity.class);
            startActivityForResult(bill, REQUEST_PAY_BILL);
        }
        else if((int)view.getTag()==getOptionIndex("Mobile Load")){
            Toast.makeText(this, "Mobile Load clicked.", Toast.LENGTH_SHORT).show();
            Intent load = new Intent(this, MobileLoadActivity.class);
            startActivityForResult(load, REQUEST_MOBILE_LOAD);
        }
        else if((int)view.getTag()==getOptionIndex("My Account")){
            Toast.makeText(this, "My Account clicked.", Toast.LENGTH_SHORT).show();
            Intent acc = new Intent(this, MyAccountActivity.class);
            startActivityForResult(acc, REQUEST_MY_ACCOUNT);
        }
        else if((int)view.getTag()==getOptionIndex("History")){
            Toast.makeText(this, "History clicked.", Toast.LENGTH_SHORT).show();
            Intent history = new Intent(this, HistoryActivity.class);
            startActivityForResult(history, REQUEST_HISTORY);
        }
        else if((int)view.getTag()==getOptionIndex("Logout")){
            Toast.makeText(this, "Logout clicked.", Toast.LENGTH_SHORT).show();
            activeUser=null;
            finish();
        }
        // Listener for tvName (Tag set as 10)
        else if((int)view.getTag()==10){
            Toast.makeText(this, "Name clicked.", Toast.LENGTH_SHORT).show();
            Intent acc = new Intent(this, MyAccountActivity.class);
            startActivityForResult(acc, REQUEST_MY_ACCOUNT);
        }
        // Listener for tvBalance (Tag set as 11)
        else if((int)view.getTag()==11){
            Toast.makeText(this, "Balance clicked.", Toast.LENGTH_SHORT).show();
            Intent history = new Intent(this, HistoryActivity.class);
            startActivityForResult(history, REQUEST_HISTORY);
        }
        // Listener for ivProfilePhoto (Tag set as 12)
        else if((int)view.getTag()==12){
            Toast.makeText(this, "Profile Photo clicked.", Toast.LENGTH_SHORT).show();
            Intent acc = new Intent(this, MyAccountActivity.class);
            startActivityForResult(acc, REQUEST_MY_ACCOUNT);
        }
        // Listener for ivRefresh (Tag set as 13)
        else if((int)view.getTag()==13){
            Toast.makeText(this, "Refresh clicked.", Toast.LENGTH_SHORT).show();
            refresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public int getOptionIndex(String x){
        for(int i=0;i<9;i++){
            if(optionsText[i].equals(x))
                return i;
        }
        return -1;
    }

    @SuppressLint("SetTextI18n")
    private void refresh(){
        tvName.setText(activeUser.fName + " " + activeUser.lName);
        tvBalance.setText("Rs. " + activeUser.balance);
    }
}