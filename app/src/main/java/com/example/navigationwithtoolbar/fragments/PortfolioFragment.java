package com.example.navigationwithtoolbar.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.navigationwithtoolbar.R;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends Fragment {


    public PortfolioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

}
