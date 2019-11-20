package com.example.localtasker.user;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.localtasker.CommonFunctionsClass;
import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.models.UserProfileModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentViewProfile extends Fragment {

    private static final String TAG = FragmentViewProfile.class.getName();
    private Context context;
    private View view;

    private CircleImageView user_profile_photo;
    private RatingBar user_profile_rating;
    private TextView user_profile_name, user_rating_counts, user_email, user_mobile, user_address, user_type, user_about;

    public FragmentViewProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_view_profile, container, false);

            initLayoutWidgets();
            getProfileData();
        }
        return view;
    }

    private void initLayoutWidgets() {

        user_profile_photo = view.findViewById(R.id.user_profile_photo);
        user_profile_rating = view.findViewById(R.id.user_profile_rating);
        user_rating_counts = view.findViewById(R.id.user_rating_counts);
        user_profile_name = view.findViewById(R.id.user_profile_name);
        user_email = view.findViewById(R.id.user_email);
        user_mobile = view.findViewById(R.id.user_mobile);
        user_address = view.findViewById(R.id.user_address);
        user_type = view.findViewById(R.id.user_type);
        user_about = view.findViewById(R.id.user_about);

    }

    private void getProfileData() {

        Bundle arguments = getArguments();
        if (arguments != null) {

            UserProfileModel userProfileModel = (UserProfileModel) arguments.getSerializable(Constants.STRING_USER_PROFILE_OBJECT);
            if (userProfileModel != null) {

                if (userProfileModel.getUserImageUrl() != null && !userProfileModel.getUserImageUrl().equals("") && !userProfileModel.getUserImageUrl().equals("null"))
                    Picasso.get()
                            .load(userProfileModel.getUserImageUrl())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .centerInside().fit()
                            .into(user_profile_photo);

                user_profile_name.setText(userProfileModel.getUserName());
                user_mobile.setText(userProfileModel.getUserMobileNumber());
                user_email.setText(userProfileModel.getUserEmailAddress());
                user_address.setText(userProfileModel.getUserAddress());
                user_type.setText(CommonFunctionsClass.getUserType(userProfileModel.getUserType()));
                user_about.setText(userProfileModel.getAbout());
                user_profile_rating.setRating(userProfileModel.getUserRating());
                user_rating_counts.setText("(" + userProfileModel.getRatingCounts() + ")");

            }

        }
    }

}
