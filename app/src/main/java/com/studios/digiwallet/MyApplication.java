package com.studios.digiwallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.studios.digiwallet.Models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class MyApplication extends Application {
     @SuppressLint("SimpleDateFormat")
     public static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
     public static User activeUser;
     public static List<User> allUsers;
     public static DatabaseReference firebase;
     public MyApplication(){
         activeUser = null;
         allUsers = new ArrayList<>();
     }
     public static void readAll(){
         firebase.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 allUsers.clear();
                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     String fn = snapshot.child("FName").getValue().toString();
                     String ln = snapshot.child("LName").getValue().toString();
                     String em = snapshot.child("Email").getValue().toString();
                     String ph = snapshot.child("Phone").getValue().toString();
                     String cn = snapshot.child("CNIC").getValue().toString();
                     String pw = snapshot.child("Password").getValue().toString();
                     float bl = Float.parseFloat(snapshot.child("Balance").getValue().toString());
                     User u = new User(fn, ln, em, ph, cn, pw, bl);
                     allUsers.add(u);
                     if(activeUser!=null && em.equals(activeUser.email))
                         activeUser=u;
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Log.d("Error", databaseError.getDetails());
             }
         });
     }

    public static void refresh() {
        String temp = firebase.push().getKey();
        firebase.child(temp).setValue(new HashMap<>().put("Resfreshing", "..."));
        firebase.child(temp).setValue(null);
    }

    public static void statusBarColor(Activity activity){
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity,R.color.my_statusbar_color));
    }
}
