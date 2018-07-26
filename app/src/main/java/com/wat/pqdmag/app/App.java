package com.wat.pqdmag.app;

import android.app.Application;

import com.karumi.dexter.Dexter;

/**
 * Created by Mouayed on 14/09/2016.
 */
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Dexter.initialize(this);

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

    }
}