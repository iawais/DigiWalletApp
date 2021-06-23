package com.studios.digiwallet.Models;

import java.util.ArrayList;

public class User {
    public String fName, lName, email, phone, cnic, pass;
    public ArrayList<Transaction> history;
    public float balance;

    public User(){

    }

    public User(String fn, String ln, String em, String ph, String cn, String pa){
        fName = fn;
        lName = ln;
        email = em;
        phone = ph;
        cnic  = cn;
        pass  = pa;
        balance = 0;
        history = new ArrayList<>();
    }

    public User(String fn, String ln, String em, String ph, String cn, String pa, float bl){
        fName = fn;
        lName = ln;
        email = em;
        phone = ph;
        cnic  = cn;
        pass  = pa;
        balance = bl;
        history = new ArrayList<>();
    }

    public void addTransaction(Transaction t){
        history.add(t);
    }

    public static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
