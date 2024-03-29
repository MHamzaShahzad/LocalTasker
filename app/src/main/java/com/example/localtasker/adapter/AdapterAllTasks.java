package com.example.localtasker.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localtasker.common.CommonFunctionsClass;
import com.example.localtasker.common.Constants;
import com.example.localtasker.R;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.models.TaskModel;
import com.example.localtasker.models.TaskBid;
import com.example.localtasker.models.UserProfileModel;
import com.example.localtasker.user.FragmentTaskDescription;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllTasks extends RecyclerView.Adapter<AdapterAllTasks.Holder> {

    private Context context;
    private List<TaskModel> list;

    private Bundle bundle;

    public AdapterAllTasks(Context context, List<TaskModel> list) {
        this.context = context;
        this.list = list;

        bundle = new Bundle();
    }

    @NonNull
    @Override
    public AdapterAllTasks.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_all_tasks, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllTasks.Holder holder, int position) {

        final TaskModel taskModel = list.get(holder.getAdapterPosition());

        holder.taskLocation.setText(taskModel.getTaskLocation());
        holder.taskStatus.setText(CommonFunctionsClass.getStringStatus(taskModel.getTaskStatus()));
        holder.taskTitle.setText(taskModel.getTaskTitle());

        getUserProfile(holder, taskModel);
        getTaskOffers(holder, taskModel.getTaskId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void getTaskOffers(final Holder holder, String taskId) {

        MyFirebaseDatabase.TASKS_REFERENCE.child(taskId).child(Constants.STRING_OFFERS_REF).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TaskBid> taskBidList = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots) {
                        if (snapshot.exists() && snapshot.getValue() != null)
                            try {
                                TaskBid taskBid = snapshot.getValue(TaskBid.class);
                                if (taskBid != null)
                                    taskBidList.add(taskBid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
                holder.taskOffers.setText(taskBidList.size() + " Offers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserProfile(final Holder holder, final TaskModel taskModel) {
        MyFirebaseDatabase.USERS_REFERENCE.child(taskModel.getTaskUploadedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {

                        final UserProfileModel userProfileModel = dataSnapshot.getValue(UserProfileModel.class);
                        if (userProfileModel != null) {
                            if (userProfileModel.getUserImageUrl() != null)
                                Picasso.get().load(userProfileModel.getUserImageUrl()).placeholder(R.drawable.image_avatar).error(R.drawable.image_avatar).centerInside().fit().into(holder.userProfilePicture);

                            holder.userRating.setText(userProfileModel.getUserRating() + "(" + userProfileModel.getRatingCounts() + ")");


                            holder.cardTask.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FragmentTaskDescription taskDescription = new FragmentTaskDescription();
                                    bundle.putString(Constants.STRING_USER_PROFILE_ID, list.get(holder.getAdapterPosition()).getTaskUploadedBy());
                                    bundle.putString(Constants.STRING_TASK_ID, list.get(holder.getAdapterPosition()).getTaskId());
                                    taskDescription.setArguments(bundle);
                                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, taskDescription).addToBackStack(null).commit();
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder {

        private CardView cardTask;
        private CircleImageView userProfilePicture;
        private TextView taskTitle, taskLocation, taskOffers, taskStatus, userRating;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardTask = itemView.findViewById(R.id.cardTask);
            userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskLocation = itemView.findViewById(R.id.taskLocation);
            taskOffers = itemView.findViewById(R.id.taskOffers);
            taskStatus = itemView.findViewById(R.id.taskStatus);
            userRating = itemView.findViewById(R.id.userRating);

        }
    }

}
