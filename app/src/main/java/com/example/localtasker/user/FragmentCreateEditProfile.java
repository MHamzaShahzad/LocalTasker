package com.example.localtasker.user;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localtasker.CommonFunctionsClass;
import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.adapter.AdapterForServicesList;
import com.example.localtasker.controllers.MyFirebaseDatabase;
import com.example.localtasker.controllers.MyFirebaseStorage;
import com.example.localtasker.interfaces.OnServiceSelectedI;
import com.example.localtasker.models.TaskCat;
import com.example.localtasker.models.UserProfileModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class FragmentCreateEditProfile extends Fragment implements View.OnClickListener, OnServiceSelectedI {

    private static final String TAG = FragmentCreateEditProfile.class.getName();
    private static final int GALLERY_REQUEST_CODE = 189;
    private Context context;
    private View view;

    private CircleImageView user_profile_photo;
    private ImageButton btn_update_profile;
    private RatingBar user_profile_rating;
    private Button btn_submit_profile;
    private TextView user_profile_name, user_rating_counts, user_email, user_mobile, user_address, user_category, user_type, user_about;
    private RadioGroup group_user_type;
    private RadioButton btn_buyer, btn_seller, btn_buyer_seller;

    private UserProfileModel userProfileModelPrevious;

    private Uri imageUri;
    private ProgressDialog progressDialog;
    private BottomSheetDialog mBottomSheetDialog;

    private BottomSheetBehavior sheetBehavior;
    private String selectedServiceId;
    private LatLng taskLocationLatLng;

    private FirebaseUser firebaseUser;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 108;

    public FragmentCreateEditProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_create_edit_profile, container, false);


            initLayoutWidgets();
            initProgressDialog();
            initSheetBehaviour();
            initBottomSheetDialog();
            getProfileData();
        }
        return view;
    }

    private void initLayoutWidgets() {

        user_profile_photo = view.findViewById(R.id.user_profile_photo);
        btn_update_profile = view.findViewById(R.id.btn_update_profile);

        user_profile_rating = view.findViewById(R.id.user_profile_rating);
        user_rating_counts = view.findViewById(R.id.user_rating_counts);
        user_profile_name = view.findViewById(R.id.user_profile_name);
        user_email = view.findViewById(R.id.user_email);
        user_mobile = view.findViewById(R.id.user_mobile);
        user_address = view.findViewById(R.id.user_address);
        user_category = view.findViewById(R.id.user_category);
        user_type = view.findViewById(R.id.user_type);
        user_about = view.findViewById(R.id.user_about);
        btn_submit_profile = view.findViewById(R.id.btn_submit_profile);

        group_user_type = view.findViewById(R.id.group_user_type);
        btn_buyer = view.findViewById(R.id.btn_buyer);
        btn_seller = view.findViewById(R.id.btn_seller);
        btn_buyer_seller = view.findViewById(R.id.btn_buyer_seller);

        initOnClickListener();

    }

    private void initOnClickListener() {
        user_category.setOnClickListener(this);
        user_address.setOnClickListener(this);
        btn_update_profile.setOnClickListener(this);
        btn_submit_profile.setOnClickListener(this);
    }

    private void getProfileData() {

        Bundle arguments = getArguments();
        if (arguments != null) {

            userProfileModelPrevious = (UserProfileModel) arguments.getSerializable(Constants.STRING_USER_PROFILE_OBJECT);

            if (userProfileModelPrevious != null) {

                if (userProfileModelPrevious.getUserImageUrl() != null && !userProfileModelPrevious.getUserImageUrl().equals("") && !userProfileModelPrevious.getUserImageUrl().equals("null"))
                    Picasso.get()
                            .load(userProfileModelPrevious.getUserImageUrl())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .centerInside().fit()
                            .into(user_profile_photo);

                user_profile_name.setText(userProfileModelPrevious.getUserName());
                user_mobile.setText(userProfileModelPrevious.getUserMobileNumber());
                user_email.setText(userProfileModelPrevious.getUserEmailAddress());
                user_address.setText(userProfileModelPrevious.getUserAddress());
                user_type.setText(CommonFunctionsClass.getUserType(userProfileModelPrevious.getUserType()));
                user_about.setText(userProfileModelPrevious.getUserAddress());
                user_profile_rating.setRating(userProfileModelPrevious.getUserRating());
                user_rating_counts.setText("(" + userProfileModelPrevious.getRatingCounts() + ")");
                setUserType(userProfileModelPrevious.getUserType());

            }

        }
    }

    private void initSheetBehaviour() {
        sheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        // callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    public void initBottomSheetDialog() {

        View dialogView = getLayoutInflater().inflate(R.layout.services_bottom_up_sheet, null);

        mBottomSheetDialog = new BottomSheetDialog(context,
                R.style.MaterialDialogSheet);

        RecyclerView recyclerView = dialogView.findViewById(R.id.listServices);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setHasFixedSize(true);

        loadServicesList(recyclerView);

        mBottomSheetDialog.setContentView(dialogView);
        mBottomSheetDialog.setCancelable(true);
        if (mBottomSheetDialog.getWindow() != null) {
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void loadServicesList(final RecyclerView recyclerView) {
        MyFirebaseDatabase.TASKS_CAT.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<TaskCat> list = new ArrayList<>();
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : snapshots) {

                        try {

                            TaskCat model = snapshot.getValue(TaskCat.class);
                            if (model != null)
                                list.add(model);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                recyclerView.setAdapter(new AdapterForServicesList(FragmentCreateEditProfile.this, context, list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.user_category:
                if (mBottomSheetDialog != null)
                    mBottomSheetDialog.show();
                break;
            case R.id.user_address:
                selectLocationDialog();
                break;
            case R.id.btn_update_profile:
                loadImageFromGallery();
                break;
            case R.id.btn_submit_profile:
                if (isFormValid())
                    setUpdateProfileData();
                break;
        }
    }

    private boolean isFormValid() {
        return true;
    }

    private void loadImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void selectLocationDialog() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build((Activity) context), PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Place place = PlacePicker.getPlace(data, context);
            if (place != null) {

                if (place.getLatLng() != null) {

                    Log.e(TAG, "onActivityResult: " + place.getLatLng());

                    taskLocationLatLng = place.getLatLng();
                }
                user_address.setText(place.getName() + "-" + place.getAddress());

            }

        } else {
            Toast.makeText(context, "You haven't picked an address!", Toast.LENGTH_LONG).show();
        }

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            user_profile_photo.setImageURI(imageUri);
        } else {
            Toast.makeText(context, "You haven't picked Image!", Toast.LENGTH_LONG).show();
        }

    } // onActivityResult Ended...

    private void setUpdateProfileData() {
        showProgressDialog();
        if (imageUri != null)
            uploadImageAndData();
        else
            uploadData(null);
    }

    private void uploadImageAndData() {
        MyFirebaseStorage.USER_PROFILE_PICS.child(imageUri.getLastPathSegment() + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadData(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                e.printStackTrace();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                        .getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            }
        });
    }

    private void uploadData(String imageUrl) {
        MyFirebaseDatabase.USERS_REFERENCE.child(firebaseUser.getUid()).setValue(getUserProfileInstance(imageUrl)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if (task.isSuccessful())
                    ((FragmentActivity) context).getSupportFragmentManager().popBackStack();
                else
                    Toast.makeText(context, "Uploading Failed : " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getUserType() {
        if (btn_buyer.isChecked())
            return Constants.USER_PROFILE_BUYER;
        else if (btn_seller.isChecked())
            return Constants.USER_PROFILE_SELLER;
        else if (btn_buyer_seller.isChecked())
            return Constants.USER_PROFILE_BUYER_SELLER;
        else return null;
    }

    private void setUserType(String userType) {
        switch (userType) {
            case Constants.USER_PROFILE_BUYER:
                btn_buyer.setChecked(true);
            case Constants.USER_PROFILE_SELLER:
                btn_seller.setChecked(true);
            case Constants.USER_PROFILE_BUYER_SELLER:
                btn_buyer_seller.setChecked(true);
        }
    }

    private UserProfileModel getUserProfileInstance(String imageUrl) {
        return new UserProfileModel(
                user_profile_name.getText().toString(),
                (imageUrl == null) ? ((userProfileModelPrevious == null) ? null : userProfileModelPrevious.getUserImageUrl()) : imageUrl,
                user_email.getText().toString().trim(),
                user_mobile.getText().toString(),
                user_address.getText().toString(),
                (taskLocationLatLng == null) ? (userProfileModelPrevious == null) ? null : userProfileModelPrevious.getUserAddressLatLng() : taskLocationLatLng.latitude + "-" + taskLocationLatLng.longitude,
                getUserType(),
                (selectedServiceId == null) ? (userProfileModelPrevious == null) ? null : userProfileModelPrevious.getUserServiceCatId() : selectedServiceId,
                user_about.getText().toString(),
                (userProfileModelPrevious == null) ? 0 : userProfileModelPrevious.getUserRating(),
                (userProfileModelPrevious == null) ? 0 : userProfileModelPrevious.getRatingCounts()

        );
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onServiceSelected(String serviceId, String serviceName) {
        user_category.setText(serviceName);
        selectedServiceId = serviceId;
        if (mBottomSheetDialog != null)
            mBottomSheetDialog.dismiss();
    }

}
