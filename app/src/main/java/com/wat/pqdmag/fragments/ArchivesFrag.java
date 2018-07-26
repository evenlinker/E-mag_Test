package com.wat.pqdmag.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wat.pqdmag.R;
import com.wat.pqdmag.adapter.ArchiveAdapter;
import com.wat.pqdmag.data.Mag;

import java.util.ArrayList;

/**
 * Created by Mouayed on 26/09/2016.
 */

public class ArchivesFrag extends Fragment {

    private View parentView;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private static final String TAG = TestFrag.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.pqd_frag_archives, container, false);

        initView();

        return parentView;
    }

    private void initView() {

        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.rv_archives);
        ArrayList<Mag> mags = Mag.createMagListUrl(20);
        ArchiveAdapter adapter = new ArchiveAdapter(getActivity(), mags);

        recyclerView.setAdapter(adapter);
        //recyclerView.setHasFixedSize(false);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.num_columns), 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

    }
}

