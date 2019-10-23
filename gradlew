<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:layout_margin="10dp"
    tools:context=".SignUpForm">

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:background="@color/cardview_light_background"
            android:orientation="vertical">

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/FullName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:inputType="text"
                android:hint="Full Name"
                android:textColorHint="@color/colorAccent" />

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/UserName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:inputType="text"
                android:hint="User Name"
                android:textColorHint="@color/colorAccent" />

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/Email"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:hint="Email"
                android:inputType="textEmailAddress"
                android:textColorHint="@color/colorAccent" />

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/Password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:hint="Password"
                android:inputType="textPassword"
                android:maxLength="10"
                android:textColorHint="@color/colorAccent" />

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/ConfirmPassword"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:hint="Confirm Password"
                android:inputType="textPassword"
                android:maxLength="10"
                android:textColorHint="@color/colorAccent" />

            <com.google.android.material.textfield.TextInputLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:focusable="true"
                android:focusableInTouchMode="true"
                android:padding="10dp" />

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/MobileNumber"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:ems="10"
                android:hint="Mobile Number"
                android:inputType="phone"
                android:textColorHint="@color/colorAccent" />

            <RadioGroup
                android:id="@+id/