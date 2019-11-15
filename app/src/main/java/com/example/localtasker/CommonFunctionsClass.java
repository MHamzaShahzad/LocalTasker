package com.example.localtasker;

public class CommonFunctionsClass {

    public static String getStringStatus(String status) {
        String taskStatus = "";
        if (status != null)
            switch (status) {
                case Constants.TASKS_STATUS_OPEN:
                    taskStatus = Constants.STRING_TASKS_STATUS_OPEN;
                    break;
                case Constants.TASKS_STATUS_ASSIGNED:
                    taskStatus = Constants.STRING_TASKS_STATUS_ASSIGNED;
                    break;
                case Constants.TASKS_STATUS_COMPLETED:
                    taskStatus = Constants.STRING_TASKS_STATUS_COMPLETED;
                    break;
                case Constants.TASKS_STATUS_REVIEWED:
                    taskStatus = Constants.STRING_TASKS_STATUS_REVIEWED;
                    break;
                case Constants.TASKS_STATUS_CANCELLED:
                    taskStatus = Constants.STRING_TASKS_STATUS_CANCELLED;
                    break;
                default:
                    taskStatus = "";
            }
        return taskStatus;
    }

    public static String getUserType(String val){
        String userType = "";
        if (val != null)
            switch (val) {
                case Constants.USER_PROFILE_BUYER:
                    userType = Constants.STRING_USER_PROFILE_BUYER;
                    break;
                case Constants.USER_PROFILE_SELLER:
                    userType = Constants.STRING_USER_PROFILE_SELLER;
                    break;
                case Constants.USER_PROFILE_BUYER_SELLER:
                    userType = Constants.STRING_USER_PROFILE_BUYER_SELLER;
                    break;
                default:
                    userType = "";
            }
        return userType;
    }

}
