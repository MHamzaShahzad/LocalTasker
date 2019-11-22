package com.example.localtasker.communicate;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.localtasker.Constants;
import com.example.localtasker.R;
import com.example.localtasker.interfaces.FragmentInteractionListenerInterface;
import com.google.android.material.textfield.TextInputEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentContactUs extends Fragment {

    private static final String TAG = FragmentContactUs.class.getName();
    private Context context;
    private View view;

    private TextInputEditText inputFeedbackComplain;
    private Button btnSubmitFeedBack;

    private FragmentInteractionListenerInterface mListener;

    public FragmentContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        if (mListener != null)
            mListener.onFragmentInteraction(Constants.TITLE_CONTACT_US);
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contact_us, container, false);

            inputFeedbackComplain = view.findViewById(R.id.inputFeedbackComplain);
            btnSubmitFeedBack = view.findViewById(R.id.btnSubmitFeedBack);

            btnSubmitFeedBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputFeedbackComplain.length() == 0) {
                        inputFeedbackComplain.setError("Field is required!");
                    } else
                        sendEmail(inputFeedbackComplain.getText().toString());
                }
            });

        }
        return view;
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
        if (mListener != null)
            mListener.onFragmentInteraction(Constants.TITLE_CONTACT_US);
    }

    protected void sendEmail(String message) {
        Log.i(TAG, "Send email");

        String[] TO = {"localtaskerdevs@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Complain / Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i(TAG, "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
