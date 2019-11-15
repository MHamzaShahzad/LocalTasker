package com.example.localtasker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localtasker.R;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.models.TaskBid;
import com.example.localtasker.models.UserProfileModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterAllOffers extends RecyclerView.Adapter<AdapterAllOffers.Holder> {

    private Context context;
    private List<TaskBid> list;

    public AdapterAllOffers(Context context, List<TaskBid> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdapterAllOffers.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_offer, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllOffers.Holder holder, int position) {

        getBuyerDetails(holder, list.get(holder.getAdapterPosition()).getBidBuyerId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private CircleImageView profileImage;
        private TextView userName, userRating;
        private RatingBar ratingBar;

        public Holder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.userName);
            userRating = itemView.findViewById(R.id.userRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }

    private void getBuyerDetails(final Holder holder, String buyerId) {
        MyFirebaseDatabase.USERS_REFERENCE.child(buyerId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                        .into(holder.profileImage);

                            holder.userName.setText(userProfileModel.getUserName());
                            holder.userRating.setText(userProfileModel.getUserRating() + "(" + userProfileModel.getRatingCounts() + ")");
                            holder.ratingBar.setRating(userProfileModel.getUserRating());
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
}

