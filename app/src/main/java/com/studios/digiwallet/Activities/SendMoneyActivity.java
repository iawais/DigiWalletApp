package com.studios.digiwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.studios.digiwallet.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.studios.digiwallet.MyApplication.statusBarColor;

public class SendMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout[] llOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        init();
        statusBarColor(this);
    }

    private void init(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar4);
        setSupportActionBar(myToolbar);
        setTitle("");
        llOptions = new LinearLayout[]{findViewById(R.id.llOption1_SendMoney), findViewById(R.id.llOption2_SendMoney)};
        llOptions[0].setTag("Mobile");
        llOptions[1].setTag("Bank");
        for(int i=0;i<llOptions.length;i++){
            llOptions[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getTag().toString().equals("Mobile")){
            Intent mobile = new Intent(this, SendMoneyToMobileAccountActivity.class);
            startActivityForResult(mobile, 1);
        }
        else if(view.getTag().toString().equals("Bank")){
            Intent bank = new Intent(this, SendMoneyToBankAccountActivity.class);
            startActivityForResult(bank, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
        }
    }
}