package com.example.navigationwithtoolbar.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.navigationwithtoolbar.Constants;
import com.example.navigationwithtoolbar.R;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {
    View v ;
    TextView email;
    Constants constants;

    public PortfolioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_portfolio,null);
        email = v.findViewById(R.id.email);
        constants = new Constants(getContext());
        email.setText(constants.getEmail());
        return v;
        //return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

}
