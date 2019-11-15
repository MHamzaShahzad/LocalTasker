package com.example.localtasker.user;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.localtasker.R;
import com.example.localtasker.adapter.AdapterAllTasks;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.models.TaskModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentAllTasksHome extends Fragment {

    private static final String TAG = FragmentAllTasksHome.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerAllTasks;
    private AdapterAllTasks adapterAllTasks;

    private List<TaskModel> taskModelList;

    public FragmentAllTasksHome() {
        // Required empty public constructor
        taskModelList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        adapterAllTasks = new AdapterAllTasks(context, taskModelList);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_all_tasks_home, container, false);

            recyclerAllTasks = view.findViewById(R.id.recyclerAllTasks);
            recyclerAllTasks.setHasFixedSize(true);
            recyclerAllTasks.setLayoutManager(new LinearLayoutManager(context));
            recyclerAllTasks.setAdapter(adapterAllTasks);


            loadAllTasks();
        }
        return view;
    }

    private void loadAllTasks() {
        MyFirebaseDatabase.TASKS_REFERENCE.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskModelList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                        for (DataSnapshot snapshot : snapshots) {
                            TaskModel taskModel = snapshot.getValue(TaskModel.class);
                            if (taskModel != null)
                                taskModelList.add(taskModel);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapterAllTasks.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
