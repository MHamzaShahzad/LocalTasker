package com.example.localtasker.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public static String getUserType(String val) {
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

    public static boolean isOutdated(String dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(dueDate);
            if (strDate != null)
                if (System.currentTimeMillis() > strDate.getTime()) {
                    return true;
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCityFromLatLng(Context context, double LATITUDE, double LONGITUDE) {
        String cityName = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                cityName = returnedAddress.getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("@ErrorInAAddress", "My Current location address Cannot get Address!");
        }
        return cityName;
    }

}
