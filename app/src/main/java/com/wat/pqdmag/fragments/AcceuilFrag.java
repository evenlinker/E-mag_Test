package com.wat.pqdmag.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.wat.pqdmag.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mouayed on 20/09/2016.
 */

public class AcceuilFrag extends Fragment {

    private View parentView;
    private Button btnRead;

    public static final String SAMPLE_FILE = "leparisien.pdf";

    private static final String TAG = TestFrag.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.pqd_fragment_acceuil, container, false);
        //parentView = inflater.inflate(R.layout.pqd_fragment_acceuil, null);

        getTest();

        return parentView;
    }

    private void getTest() {

        btnRead = (Button) parentView.findViewById(R.id.acc_btn_read);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPdfWithFragment();
            }
        });
    }


    public void openPdfWithFragment() {

        /*fragment = new PdfFragment();
        Bundle args = new Bundle();
        args.putString(FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);
        fragment.setArguments(args);

        mContent = fragment;

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();*/

        //Uri uri = Uri.parse( Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);

        //Uri uri = Uri.parse("android.resource://com.wat.pqdmag/raw/"+SAMPLE_FILE);

        //Uri uri = Uri.parse("file:///android_asset/SFTBR-04.pdf");//+SAMPLE_FILE);

        //Uri uri = Uri.parse("http://www.office.xerox.com/latest/SFTBR-04.PDF");

        Uri uri = Uri.parse(getAssetsPdfPath(getActivity(), SAMPLE_FILE));

        System.out.println("path = "+uri.toString());

        Intent intent = new Intent(getActivity(), MuPDFActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }
    public static String getAssetsPdfPath(Context context, String pdFile) {

        String filePath = context.getFilesDir() + File.separator + pdFile;

        File destinationFile = new File(filePath);

        try {
            FileOutputStream outputStream = new FileOutputStream(destinationFile);
            InputStream inputStream = context.getAssets().open(pdFile);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e(context.getClass().getSimpleName(), "Error.");
        }

        return destinationFile.getPath();
    }



}