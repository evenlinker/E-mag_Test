package com.wat.pqdmag;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wat.pqdmag.adapter.MagAdapter;
import com.wat.pqdmag.data.Mag;
import com.wat.pqdmag.fragments.AcceuilFrag;
import com.wat.pqdmag.fragments.ArchivesFrag;
import com.wat.pqdmag.fragments.TestFrag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;

    private String mTitle;
    private Fragment mContent;
    private FragmentTransaction transaction;

    private boolean mTwoPane;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pqd_activity_main);

        setUpToolbar();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        if (findViewById(R.id.drawer_layout) != null) {
            //Phone layout
            mTwoPane = false;
            setUpNavDrawer();
        } else {
            //Tablet layout
            mTwoPane = true;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        RecyclerView mRecyclerView = (RecyclerView) navigationView.findViewById(R.id.rv_mag);
        ArrayList<Mag> mags = Mag.createMagsList(20);
        final MagAdapter adapter = new MagAdapter(this, mags);

        adapter.setOnItemClickListener(new MagAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.d(TAG, "onItemClick position: " + adapter.getItem(position).toString());

                Uri uri = Uri.parse( AcceuilFrag.getAssetsPdfPath(MainActivity.this, AcceuilFrag.SAMPLE_FILE));

                System.out.println("path = "+uri.toString());

                Intent intent = new Intent(MainActivity.this, MuPDFActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);

                if (!mTwoPane) {
                    if (mDrawerLayout != null) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "onItemLongClick pos = " +position);
            }
        });

        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

       // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        if (savedInstanceState != null) {

            mTitle = savedInstanceState.getString("savText");
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }

        if (mContent == null) {
           mContent = new AcceuilFrag();
            mTitle = getResources().getString(R.string.app_name);
        }

        bindViews();

        requestPermissionsWrapper();

        TextView btnHome = (TextView) navigationView.findViewById(R.id.nav_home);
        TextView btnArchives = (TextView) navigationView.findViewById(R.id.nav_archives);
        TextView btnParam = (TextView) navigationView.findViewById(R.id.nav_param);

        btnHome.setOnClickListener(this);
        btnArchives.setOnClickListener(this);
        btnParam.setOnClickListener(this);

        //openPdfWithFragment();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.nav_home) {
            addFragment(new AcceuilFrag(), "Acceuil");
        } else if (id == R.id.nav_archives) {
            addFragment(new ArchivesFrag(), "Archives");
        } else if (id == R.id.nav_param) {
            addFragment(new TestFrag(), "Réglages");
        }

        if (!mTwoPane) {
            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }

    private void requestPermissionsWrapper(){
        Dexter.checkPermissions(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
            {
                token.continuePermissionRequest();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void setUpToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNavDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void bindViews() {

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mContent);
        transaction.commit();

        /*if(getSupportActionBar() != null) getSupportActionBar().setTitle(mTitle);

        View header = navigationView.getHeaderView(0);

        userImage = (CircleImageView) header.findViewById(R.id.nav_profile_image);
        userName = (TextView) header.findViewById(R.id.nav_profile_name);
        userMail = (TextView) header.findViewById(R.id.nav_profile_mail);
        tvbuild = (TextView) header.findViewById(R.id.nav_profile_build);

        tvbuild.setText("build: "+version);
        unitUserProfile();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        /*int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        outState.putString("savText", mTitle);
        super.onSaveInstanceState(outState);
    }

    public void addFragment(Fragment fragment, String title) {

        String tag = fragment.getClass().toString();

        if (!tag.contentEquals(mContent.getClass().toString())) {

            transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            mContent = fragment;

            toolbar.setTitle(title);
            mTitle = title;
        }

        if (!mTwoPane) {
            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawers();
            }
        }
    }

    private static final String SAMPLE_FILE = "leparisien.pdf";
    private static final String FILE_PATH = "filepath";
    private static final String SEARCH_TEXT = "text";

    public void openPdfWithFragment() {

        /*fragment = new PdfFragment();
        Bundle args = new Bundle();
        args.putString(FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);
        fragment.setArguments(args);

        mContent = fragment;

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();*/

        Uri uri = Uri.parse( Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);

        Intent intent = new Intent(this,MuPDFActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }


}
