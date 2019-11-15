package com.example.localtasker.communicate;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.localtasker.R;


public class FragmentContactUs extends Fragment {

    private static final String TAG = FragmentContactUs.class.getName();
    private Context context;
    private View view;

    public FragmentContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_contact_us, container, false);


        }
        return view;
    }

}
