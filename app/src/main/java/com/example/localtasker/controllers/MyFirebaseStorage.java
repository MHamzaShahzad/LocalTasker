package com.example.localtasker.controllers;

import com.example.localtasker.common.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyFirebaseStorage {

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    public static final StorageReference USER_PROFILE_PICS = storage.getReference().child(Constants.STRING_PROFILE_PIC_STORAGE);
    public static final StorageReference TASKS_CAT_PICS = storage.getReference().child(Constants.STRING_TASKS_CAT_PIC_STORAGE);
    public static final StorageReference TASKS_PICS = storage.getReference().child(Constants.STRING_TASKS_PIC_STORAGE);

}
