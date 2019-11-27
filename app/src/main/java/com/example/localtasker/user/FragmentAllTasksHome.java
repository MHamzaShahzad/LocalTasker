package com.example.localtasker.user;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.localtasker.CommonFunctionsClass;
import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.adapter.AdapterAllTasks;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.controllers.SendPushNotificationFirebase;
import com.example.localtasker.interfaces.FragmentInteractionListenerInterface;
import com.example.localtasker.models.TaskModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentAllTasksHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = FragmentAllTasksHome.class.getName();
    private Context context;
    private View view;

    private RecyclerView recyclerAllTasks;
    private static AdapterAllTasks adapterAllTasks;

    private List<TaskModel> taskModelList;
    private ValueEventListener allTasksValueEventListener;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FragmentInteractionListenerInterface mListener;


    public FragmentAllTasksHome() {
        // Required empty public constructor
        taskModelList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mListener != null)
            mListener.onFragmentInteraction(Constants.TITLE_HOME);
        adapterAllTasks = new AdapterAllTasks(context, taskModelList);
        context = container.getContext();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_all_tasks_home, container, false);

            recyclerAllTasks = view.findViewById(R.id.recyclerAllTasks);
            recyclerAllTasks.setHasFixedSize(true);
            recyclerAllTasks.setLayoutManager(new LinearLayoutManager(context));
            recyclerAllTasks.setAdapter(adapterAllTasks);

            initSwipeRefreshLayout();
            loadAllTasks();

        }
        return view;
    }

    private void initSwipeRefreshLayout() {
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        /*
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                startRefreshing();
                // Fetching data from server
                loadAllTasks();
            }
        });
    }

    private void loadAllTasks() {
        removeAllTasksEventListener();
         allTasksValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot );
                taskModelList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                        for (DataSnapshot snapshot : snapshots) {
                            TaskModel taskModel = snapshot.getValue(TaskModel.class);
                            if (taskModel != null) {

                                if (!taskModel.getTaskStatus().equals(Constants.TASKS_STATUS_CANCELLED))
                                    if (CommonFunctionsClass.isOutdated(taskModel.getTaskDueDate()) && taskModel.getTaskStatus().equals(Constants.TASKS_STATUS_OPEN))
                                        makeTaskCancelIfOutDate(taskModel);
                                    else
                                        taskModelList.add(taskModel);

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.e(TAG, "onDataChange: " + taskModelList.size() );
                adapterAllTasks = new AdapterAllTasks(context, taskModelList);
                recyclerAllTasks.setAdapter(adapterAllTasks);
                stopRefreshing();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                stopRefreshing();
            }
        };
        MyFirebaseDatabase.TASKS_REFERENCE.addValueEventListener(allTasksValueEventListener);
    }

    private void makeTaskCancelIfOutDate(final TaskModel taskModel) {
        MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(TaskModel.STRING_TASK_STATUS_REF).setValue(Constants.TASKS_STATUS_CANCELLED).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SendPushNotificationFirebase.buildAndSendNotification(context,
                        taskModel.getTaskUploadedBy(),
                        "Task Cancelled!",
                        "Your task has been cancelled due to Outdated"
                );
            }
        });
    }

    private void removeAllTasksEventListener() {
        if (allTasksValueEventListener != null)
            MyFirebaseDatabase.TASKS_REFERENCE.removeEventListener(allTasksValueEventListener);
    }

    private void startRefreshing(){
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(true);
    }

    private void stopRefreshing(){
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeAllTasksEventListener();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentInteractionListenerInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentInteractionListenerInterface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
        if (mListener != null)
            mListener.onFragmentInteraction(Constants.TITLE_HOME);
    }

    @Override
    public void onRefresh() {
        loadAllTasks();
    }
}
