package com.example.localtasker.user;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.adapter.AdapterMyTasks;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.models.TaskModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyAllTasks extends Fragment {

    private static final String TAG = FragmentMyAllTasks.class.getName();
    private Context context;
    private View view;

    private TabLayout tabMyTasks;
    private RecyclerView recyclerMyTasks;
    private AdapterMyTasks adapterMyTasks;
    private List<TaskModel> taskModelList, taskModelListTemp;

    private ValueEventListener tasksValueEventListener;

    private FirebaseUser firebaseUser;

    public FragmentMyAllTasks() {
        // Required empty public constructor
        taskModelList = new ArrayList<>();
        taskModelListTemp = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        adapterMyTasks = new AdapterMyTasks(context, taskModelListTemp);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_tasks, container, false);

            initLayoutWidgets();
            initTasksValueEventListener();
        }
        return view;
    }

    private void initLayoutWidgets() {
        tabMyTasks = view.findViewById(R.id.tabMyTasks);
        recyclerMyTasks = view.findViewById(R.id.recyclerMyTasks);

        setTabMyTasks();
        setRecyclerMyTasks();
    }

    private void setRecyclerMyTasks() {
        recyclerMyTasks.setHasFixedSize(true);
        recyclerMyTasks.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerMyTasks.setAdapter(adapterMyTasks);
    }

    private void setTabMyTasks() {
        tabMyTasks.addTab(tabMyTasks.newTab().setText("Open"), true);
        tabMyTasks.addTab(tabMyTasks.newTab().setText("Assigned"));
        tabMyTasks.addTab(tabMyTasks.newTab().setText("Completed"));
        tabMyTasks.addTab(tabMyTasks.newTab().setText("Reviewed"));

        setTabSelectedListener();
    }

    private void loadTasksBasedOnTabSelected(int position){
        switch (position) {
            case 0:
                getMyTasks(Constants.TASKS_STATUS_OPEN);
                break;
            case 1:
                getMyTasks(Constants.TASKS_STATUS_ASSIGNED);
                break;
            case 2:
                getMyTasks(Constants.TASKS_STATUS_COMPLETED);
                break;
            case 3:
                getMyTasks(Constants.TASKS_STATUS_REVIEWED);
                break;
            default:
        }
    }

    private void setTabSelectedListener(){
        tabMyTasks.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadTasksBasedOnTabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getMyTasks(String status) {
        taskModelListTemp.clear();
        for (TaskModel taskModel : taskModelList)
            if (taskModel != null && taskModel.getTaskStatus() != null && taskModel.getTaskStatus().equals(status))
                taskModelListTemp.add(taskModel);
        adapterMyTasks.notifyDataSetChanged();
    }

    private void initTasksValueEventListener() {
        tasksValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {

                    taskModelList.clear();
                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots) {
                        try {
                            TaskModel taskModel = snapshot.getValue(TaskModel.class);
                            if (taskModel != null && taskModel.getTaskUploadedBy().equals(firebaseUser.getUid()))
                                taskModelList.add(taskModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    loadTasksBasedOnTabSelected(tabMyTasks.getSelectedTabPosition());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        MyFirebaseDatabase.TASKS_REFERENCE.addValueEventListener(tasksValueEventListener);
    }

    private void removeTasksValueEventListener() {
        if (tasksValueEventListener != null)
            MyFirebaseDatabase.TASKS_REFERENCE.removeEventListener(tasksValueEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTabSelectedListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeTasksValueEventListener();
    }

}
