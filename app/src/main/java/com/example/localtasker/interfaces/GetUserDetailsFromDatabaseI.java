package com.example.localtasker.interfaces;

import com.example.localtasker.adapter.AdapterAllTasks;
import com.example.localtasker.models.UserProfileModel;

public interface GetUserDetailsFromDatabaseI {
    void getUserDetailsFromDatabase(UserProfileModel userProfileModel);
}
