package com.wat.pqdmag.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wat.pqdmag.R;
import com.wat.pqdmag.adapter.MagAdapter;
import com.wat.pqdmag.data.Mag;

import java.util.ArrayList;


/**
 * Created by Mouayed on 16/06/2016.
 */
public class TestFrag extends Fragment  {

    private View parentView;

    private static final String TAG = TestFrag.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.pqd_content_main, container, false);

        getTest();

        return parentView;
    }

    private void getTest() {

    }
}
