package com.example.localtasker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.localtasker.admin.FragmentTaskCategoriesAdmin;
import com.example.localtasker.communicate.FragmentContactUs;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.interfaces.FragmentInteractionListenerInterface;
import com.example.localtasker.models.UserProfileModel;
import com.example.localtasker.user.FragmentAllTasksHome;
import com.example.localtasker.user.FragmentCreateEditProfile;
import com.example.localtasker.user.FragmentMyAllTasks;
import com.example.localtasker.user.FragmentMyBids;
import com.example.localtasker.user.FragmentUploadTask;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentInteractionListenerInterface {

    private static final String TAG = HomeDrawerActivity.class.getName();
    private Context context;
    private FirebaseUser firebaseUser;
    private ValueEventListener userProfileValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        setContentView(R.layout.activity_home_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentAllTasksHome()).commit();
        loadUserProfileDataInNavigationHeader(navigationView.getHeaderView(0));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null)
            FirebaseMessaging.getInstance().subscribeToTopic(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Log.d(TAG, "Topic subscribed!");
                    else
                        Log.e(TAG, "Can't subscribe to topic");
                }
            });
        else
            SignOut();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                super.onBackPressed();
            else
                alertDialogOnBackPress();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_category_admin) {
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentTaskCategoriesAdmin()).addToBackStack(null).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        clearFragmentBackStack();

        if (id == R.id.nav_home) {

            // Handle the camera action

        } else if (id == R.id.nav_upload_task) {

            goToUploadTaskIfUserExists();

        } else if (id == R.id.nav_my_tasks) {

            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentMyAllTasks()).addToBackStack(null).commit();

        } else if (id == R.id.nav_my_bids) {

            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentMyBids()).addToBackStack(null).commit();

        } else if (id == R.id.nav_profile) {

            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentCreateEditProfile()).addToBackStack(null).commit();

        } else if (id == R.id.nav_logout) {

            SignOut();

        } else if (id == R.id.nav_contact_us) {

            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentContactUs()).addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void clearFragmentBackStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++)
            getSupportFragmentManager().popBackStack();
    }

    private void SignOut() {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    moveToMain();
                }
            }
        });
    }

    private void moveToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void goToUploadTaskIfUserExists() {

        MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentUploadTask()).addToBackStack(null).commit();
                } else
                    showDialogIfAccountHaveToBeCreated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogIfAccountHaveToBeCreated() {

        new AlertDialog.Builder(context)
                .setMessage("Submit your profile first!")
                .setTitle("Profile Submission Alert")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentCreateEditProfile()).addToBackStack(null).commit();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();

    }

    @Override
    public void onFragmentInteraction(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    private void alertDialogOnBackPress() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to quit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void loadUserProfileDataInNavigationHeader(View headerView) {
        final ImageView headerImageUser = headerView.findViewById(R.id.userProfileImageView);
        final TextView headerUserName = headerView.findViewById(R.id.userNameText);
        final TextView headerUserEmail = headerView.findViewById(R.id.userEmailText);
        userProfileValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {

                        UserProfileModel userProfileModel = dataSnapshot.getValue(UserProfileModel.class);
                        if (userProfileModel != null) {
                            if (userProfileModel.getUserImageUrl() != null && !userProfileModel.getUserImageUrl().equals("null") && !userProfileModel.getUserImageUrl().equals(""))
                                Picasso.get()
                                        .load(userProfileModel.getUserImageUrl())
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_background)
                                        .centerInside().fit()
                                        .into(headerImageUser);

                            headerUserName.setText(userProfileModel.getUserName());
                            headerUserEmail.setText(userProfileModel.getUserMobileNumber());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getUid()).addValueEventListener(userProfileValueEventListener);
    }
    private void removeUserProfileEventListener(){
        if (userProfileValueEventListener != null){
            MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getUid()).removeEventListener(userProfileValueEventListener);
        }
    }


    @Override
    protected void onDestroy() {
        removeUserProfileEventListener();
        super.onDestroy();
    }
}
