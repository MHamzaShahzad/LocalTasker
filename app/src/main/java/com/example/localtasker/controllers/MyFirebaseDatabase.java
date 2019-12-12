package com.example.localtasker.controllers;

import com.example.localtasker.common.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyFirebaseDatabase {

    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference USERS_REFERENCE = database.getReference(Constants.STRING_USERS_REF);
    public static final DatabaseReference TASKS_REFERENCE = database.getReference(Constants.STRING_TASKS_REF);
    public static final DatabaseReference TASKS_CAT = database.getReference(Constants.STRING_TASKS_CAT);

}
