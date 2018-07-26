package com.wat.pqdmag.data;

import com.wat.pqdmag.R;

import java.util.ArrayList;


/**
 * Created by Mouayed on 14/09/2016.
 */
public class Mag  {

    private String mdate;
    private int mthumb;
    private String url;

    public Mag(String mdate, int mthumb) {
        this.mdate = mdate;
        this.mthumb = mthumb;
    }

    public Mag(String mdate, int mthumb, String url) {
        this.mdate = mdate;
        this.mthumb = mthumb;
        this.url = url;
    }

    public String getDate() {
        return mdate;
    }

    public int getThumb() {
        return mthumb;
    }

    public String getURL() {
        return url;
    }

    public static ArrayList<Mag> createMagsList(int numContacts) {
        ArrayList<Mag> mags = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            if(i%2 != 0)
                mags.add(new Mag("14 September 2016 ", R.drawable.thumb));
            else
                mags.add(new Mag("17 Juillet 2016 ", R.drawable.thumb_2));
        }

        return mags;
    }

    public static ArrayList<Mag> createMagListUrl(int numContacts) {
        ArrayList<Mag> mags = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            if(i%2 != 0)
                mags.add(new Mag("14 September 2016 ", R.drawable.thumb, "http://www.tuvm.com.tn/img_pdf-formation/tuvm-prog.pdf"));
            else
                mags.add(new Mag("17 Juillet 2016 ", R.drawable.thumb_2, "http://www.apefasbl.org/formapef/catalogue-formapef-2016-2017"));
        }

        return mags;
    }
}