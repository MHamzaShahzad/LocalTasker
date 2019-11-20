package com.example.localtasker.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.adapter.AdapterAllOffers;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.models.TaskModel;
import com.example.localtasker.models.TaskBid;
import com.example.localtasker.models.UserProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentTaskDescription extends Fragment {

    private static final String TAG = FragmentTaskDescription.class.getName();
    private Context context;
    private View view;

    private CircleImageView profileImage;
    private Button btnMakeOffer, btnEditOffer, btnRemoveOffer, btnCompleteTask, btnIncompleteTask, btnReview;
    private TextView taskTitle, placePostedBy, placeUploadedDuration, placeTaskDescription, placeTaskLocation, viewOnMap, placeDueDate, placeBudget, taskReview;
    private RecyclerView recyclerTaskOffers;
    private LinearLayout layout_edit_remove, layout_complete_incomplete_task;

    private AdapterAllOffers adapterAllOffers;
    private List<TaskBid> taskBidList;

    private ValueEventListener taskValueEventListener, userProfileValueEventListener, bidsValueEventListener;
    private String taskId, userProfileId;

    private FirebaseUser firebaseUser;

    public FragmentTaskDescription() {
        // Required empty public constructor
        taskBidList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_task_description, container, false);

            initLayoutWidgets();
            getDetails();

        }

        return view;
    }

    private void initLayoutWidgets() {

        layout_edit_remove = view.findViewById(R.id.layout_edit_remove);
        layout_complete_incomplete_task = view.findViewById(R.id.layout_complete_incomplete_task);
        taskTitle = view.findViewById(R.id.taskTitle);
        profileImage = view.findViewById(R.id.profileImage);
        placePostedBy = view.findViewById(R.id.placePostedBy);
        placeUploadedDuration = view.findViewById(R.id.placeUploadedDuration);
        placeTaskDescription = view.findViewById(R.id.placeTaskDescription);
        placeTaskLocation = view.findViewById(R.id.placeTaskLocation);
        placeDueDate = view.findViewById(R.id.placeDueDate);
        placeBudget = view.findViewById(R.id.placeBudget);
        taskReview = view.findViewById(R.id.taskReview);

        viewOnMap = view.findViewById(R.id.viewOnMap);
        btnMakeOffer = view.findViewById(R.id.btnMakeOffer);
        btnEditOffer = view.findViewById(R.id.btnEditOffer);
        btnRemoveOffer = view.findViewById(R.id.btnRemoveOffer);
        btnCompleteTask = view.findViewById(R.id.btnCompleteTask);
        btnIncompleteTask = view.findViewById(R.id.btnIncompleteTask);
        btnReview = view.findViewById(R.id.btnReview);

        recyclerTaskOffers = view.findViewById(R.id.recyclerTaskOffers);
    }

    private void setRecyclerView(TaskModel taskModel) {

        recyclerTaskOffers.setLayoutManager(new LinearLayoutManager(context));
        recyclerTaskOffers.setHasFixedSize(true);
        adapterAllOffers = new AdapterAllOffers(context, taskModel, taskBidList);
        recyclerTaskOffers.setAdapter(adapterAllOffers);

        getTaskOffers(taskModel.getTaskId());
    }

    private void getDetails() {

        Bundle arguments = getArguments();
        if (arguments != null) {

            taskId = arguments.getString(Constants.STRING_TASK_ID);
            userProfileId = arguments.getString(Constants.STRING_USER_PROFILE_ID);

            if (taskId != null) {
                loadTaskModelData(taskId);
            }

            if (userProfileId != null) {
                loadTaskUploadedByProfileData(userProfileId);
            }

        }

    }

    private void loadTaskModelData(final String taskId) {
        taskValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setDefaultButtonsVisibility();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {

                        TaskModel taskModel = dataSnapshot.getValue(TaskModel.class);

                        if (taskModel != null) {

                            setRecyclerView(taskModel);

                            taskTitle.setText(taskModel.getTaskTitle());
                            placeTaskLocation.setText(taskModel.getTaskLocation());
                            placeBudget.setText(taskModel.getTaskBudget());
                            placeDueDate.setText(taskModel.getTaskDueDate());
                            placeUploadedDuration.setText(taskModel.getTaskUploadedOn());
                            placeTaskDescription.setText(taskModel.getTaskDescription());

                            setViewOnMap(taskModel.getTaskLocation(), getTaskLatitude(taskModel.getTaskLatLng()), getTaskLongitude(taskModel.getTaskLatLng()));

                            /*if (isOutdated(taskModel.getTaskDueDate()) && taskModel.getTaskStatus().equals(Constants.TASKS_STATUS_OPEN)){
                                MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(TaskModel.STRING_TASK_STATUS_REF).setValue(Constants.TASKS_STATUS_CANCELLED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Task Cancelled due to Out of date!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            if (isOutdated(taskModel.getTaskDueDate()) && taskModel.getTaskStatus().equals(Constants.TASKS_STATUS_ASSIGNED)){
                                MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(TaskModel.STRING_TASK_STATUS_REF).setValue(Constants.TASKS_STATUS_UNCOMPLETED).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Task Incomplete due to Out of date!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }*/

                            if (taskModel.getTaskUploadedBy().equals(firebaseUser.getUid())) {
                                switch (taskModel.getTaskStatus()) {
                                    case Constants.TASKS_STATUS_OPEN:
                                    case Constants.TASKS_STATUS_CANCELLED:
                                        editDeleteTask(taskModel);
                                        break;
                                    case Constants.TASKS_STATUS_ASSIGNED:
                                        completeInCompleteTask(taskModel);
                                        break;
                                    case Constants.TASKS_STATUS_COMPLETED:
                                    case Constants.TASKS_STATUS_UNCOMPLETED:
                                        reviewTaskBySeller(taskModel);
                                        break;
                                    case Constants.TASKS_STATUS_REVIEWED:
                                        taskReview.setVisibility(View.VISIBLE);
                                        taskReview.setText(taskModel.getTaskReviewBySeller());
                                        break;
                                }

                            } else
                                setBtnMakeOffer(taskModel);

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
        MyFirebaseDatabase.TASKS_REFERENCE.child(taskId).addValueEventListener(taskValueEventListener);
    }

    private void reviewTaskBySeller(final TaskModel taskModel) {

        btnReview.setVisibility(View.VISIBLE);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentReviewOnTask fragmentReviewOnTask = new FragmentReviewOnTask();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STRING_TASK_OBJECT, taskModel);
                fragmentReviewOnTask.setArguments(bundle);
                fragmentReviewOnTask.show(((FragmentActivity) context).getSupportFragmentManager(), "Review Task");
            }
        });

    }

    private void setDefaultButtonsVisibility() {
        taskReview.setVisibility(View.INVISIBLE);
        btnMakeOffer.setVisibility(View.INVISIBLE);
        layout_complete_incomplete_task.setVisibility(View.INVISIBLE);
        layout_edit_remove.setVisibility(View.INVISIBLE);
        btnReview.setVisibility(View.INVISIBLE);
    }

    private void completeInCompleteTask(final TaskModel taskModel) {

        layout_complete_incomplete_task.setVisibility(View.VISIBLE);

        btnCompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(TaskModel.STRING_TASK_STATUS_REF).setValue(Constants.TASKS_STATUS_COMPLETED).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(context, "Successfully!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Un-Successful!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnIncompleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(TaskModel.STRING_TASK_STATUS_REF).setValue(Constants.TASKS_STATUS_UNCOMPLETED).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(context, "Successful!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Un-Successful!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }


    private void loadTaskUploadedByProfileData(String userProfileId) {
        userProfileValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        UserProfileModel userProfileModel = dataSnapshot.getValue(UserProfileModel.class);
                        if (userProfileModel != null) {
                            if (userProfileModel.getUserImageUrl() != null && !userProfileModel.getUserImageUrl().equals("") && !userProfileModel.getUserImageUrl().equals("null"))
                                Picasso.get()
                                        .load(userProfileModel.getUserImageUrl())
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_background)
                                        .centerInside().fit()
                                        .into(profileImage);

                            placePostedBy.setText(userProfileModel.getUserName());
                            seeUserProfile(userProfileModel);
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
        MyFirebaseDatabase.USERS_REFERENCE.child(userProfileId).addListenerForSingleValueEvent(userProfileValueEventListener);
    }

    private double getTaskLatitude(String latLng) {
        if (latLng != null) {
            if (latLng.contains("-")) {
                return Double.parseDouble(latLng.split("-")[0]);
            }
        }
        return 0.0;
    }

    private double getTaskLongitude(String latLng) {
        if (latLng != null) {
            if (latLng.contains("-")) {
                return Double.parseDouble(latLng.split("-")[1]);
            }
        }
        return 0.0;
    }

    private void getTaskOffers(String taskId) {
        bidsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                taskBidList.clear();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots) {
                        if (snapshot.exists() && snapshot.getValue() != null)
                            try {
                                TaskBid taskBid = snapshot.getValue(TaskBid.class);
                                taskBidList.add(taskBid);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
                adapterAllOffers.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        MyFirebaseDatabase.TASKS_REFERENCE.child(taskId).child(Constants.STRING_OFFERS_REF).addValueEventListener(bidsValueEventListener);
    }

    private void setViewOnMap(final String locationAddress, final double latitude, final double longitude) {
        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMapLocationForTask mapLocationForTask = new FragmentMapLocationForTask();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.STRING_LOCATION_ADDRESS, locationAddress);
                bundle.putDouble(Constants.STRING_LOCATION_LATITUDE, latitude);
                bundle.putDouble(Constants.STRING_LOCATION_LONGITUDE, longitude);
                mapLocationForTask.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, mapLocationForTask).addToBackStack(null).commit();
            }
        });
    }

    private void seeUserProfile(final UserProfileModel profileModel) {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentViewProfile viewProfile = new FragmentViewProfile();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STRING_USER_PROFILE_OBJECT, profileModel);
                viewProfile.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, viewProfile).addToBackStack(null).commit();

            }
        });
    }

    private void setBtnMakeOffer(final TaskModel taskModel) {

        btnMakeOffer.setVisibility(View.VISIBLE);
        MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(Constants.STRING_OFFERS_REF).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getValue() != null)
                    btnMakeOffer.setEnabled(false);
                else {
                    btnMakeOffer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                                        MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).child(Constants.STRING_OFFERS_REF).child(firebaseUser.getUid()).setValue(buildBidInstance("")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                    Toast.makeText(context, "Offer Sent Successfully!", Toast.LENGTH_LONG).show();
                                                else
                                                    Toast.makeText(context, "Offer Could't be sent!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, new FragmentCreateEditProfile()).addToBackStack(null).commit();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private TaskBid buildBidInstance(String message) {
        return new TaskBid(
                firebaseUser.getUid(),
                message
        );
    }

    private void editDeleteTask(final TaskModel taskModel) {

        layout_edit_remove.setVisibility(View.VISIBLE);

        btnEditOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentEditTaskDetails fragmentEditTaskDetails = new FragmentEditTaskDetails();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STRING_TASK_OBJECT, taskModel);
                fragmentEditTaskDetails.setArguments(bundle);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().add(R.id.home_fragment, fragmentEditTaskDetails).addToBackStack(null).commit();
            }
        });

        btnRemoveOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(context)
                        .setMessage("Are you sure , you want to remove this task ?")
                        .setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyFirebaseDatabase.TASKS_REFERENCE.child(taskModel.getTaskId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Task removed successfully!", Toast.LENGTH_LONG).show();
                                            ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                                        } else
                                            Toast.makeText(context, "Task could't be removed!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });

    }

    private boolean isOutdated(String dueDate) {
        return true;
    }

    private void removeTaskEventListener() {
        if (taskValueEventListener != null && taskId != null)
            MyFirebaseDatabase.TASKS_REFERENCE.child(taskId).removeEventListener(taskValueEventListener);

    }

    private void removeBidsEventListener() {
        if (bidsValueEventListener != null && taskId != null)
            MyFirebaseDatabase.TASKS_REFERENCE.child(taskId).child(Constants.STRING_OFFERS_REF).removeEventListener(bidsValueEventListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeTaskEventListener();
        removeBidsEventListener();
    }

}
