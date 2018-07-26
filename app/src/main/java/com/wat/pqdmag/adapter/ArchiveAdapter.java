package com.wat.pqdmag.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artifex.mupdfdemo.AsyncTask;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.wat.pqdmag.R;
import com.wat.pqdmag.data.Mag;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by Mouayed on 27/09/2016.
 */

public class ArchiveAdapter extends RecyclerView.Adapter<ArchiveAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumb;
        private ImageView downloadBadge;
        private TextView tvDate;

        private ViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.item_mag_thumb);
            downloadBadge = (ImageView) itemView.findViewById(R.id.item_mag_download);
            tvDate = (TextView) itemView.findViewById(R.id.item_mag_date);
        }

        private void setOnClickListener(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }

    }

    private List<Mag> mMags;
    private Context mContext;
    private File mFile;

    // Pass in the contact array into the constructor
    public ArchiveAdapter(Context context, List<Mag> mags) {
        mMags = mags;
        mContext = context;
        mFile = MagDir(mContext);
    }

    @Override
    public ArchiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.pqd_archive_item, parent, false);
        return new ViewHolder(itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArchiveAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Mag mag = mMags.get(position);

        ImageView imageView = viewHolder.thumb;
        imageView.setImageResource(mag.getThumb());
        TextView textView = viewHolder.tvDate;
        textView.setText(mag.getDate());

        ImageView downloadBadge = viewHolder.downloadBadge;
        if(isFileExiste(getFileNameFormUrl(mag.getURL())))
            downloadBadge.setVisibility(View.GONE);
        else
            downloadBadge.setVisibility(View.VISIBLE);

        viewHolder.setOnClickListener(new onClickListener(mag));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mMags.size();
    }

    private class onClickListener implements View.OnClickListener {

        Mag data;

        private onClickListener(Mag data) {
            this.data = data;
        }

        @Override
        public void onClick(View v) {

            final String fileName = getFileNameFormUrl(data.getURL());
            Log.d("test", fileName);
            if(isFileExiste(getFileNameFormUrl(data.getURL()))) {

                File pdfFile = new File(MagDir(mContext).getAbsolutePath() + "/" + fileName);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFile);

                Intent intent = new Intent(mContext, MuPDFActivity.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(path);
                mContext.startActivity(intent);
            }else {

                new DownloadFile().execute(data.getURL(), fileName);
            }
        }
    }


    ProgressDialog mProgressDialog;

    public static final double SPACE_KB = 1024;
    public static final double SPACE_MB = 1024 * SPACE_KB;
    public static final double SPACE_GB = 1024 * SPACE_MB;
    public static final double SPACE_TB = 1024 * SPACE_GB;

    private class DownloadFile extends AsyncTask<String, String, String> {

        private static final int MEGABYTE = 1024;
        String fileName = "";
        long  downloadedSize = 0;
        long  lenghtOfFile = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Téléchargement en cours ...");
            mProgressDialog.setIndeterminate(true);
            //mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setProgressNumberFormat ("");
            //mProgressDialog.setProgressNumberFormat((bytes2String(downloadedsize)) + "/" + (bytes2String(filesize)));
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            int count;
            try {
                String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
                fileName = strings[1];  // -> maven.pdf

                URL url = new URL(fileUrl);
                URLConnection conection = url.openConnection();
                conection.connect();

                lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(MagDir(mContext).getAbsolutePath() +"/"+ fileName);

                byte data[] = new byte[MEGABYTE];

                //long total = 0;

                mProgressDialog.setMax((int)lenghtOfFile);

                Log.i("lenghtOfFile: ", ""+lenghtOfFile);
                while ((count = input.read(data)) != -1) {

                    downloadedSize += count;
                    publishProgress(""+downloadedSize);

                    output.write(data, 0, count);

                    Log.i("progress: ", ""+downloadedSize);

                    if (isCancelled()) break;
                }

                Log.i("progress: ", "END");

                output.flush();

                output.close();
                input.close();

            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressNumberFormat((bytes2String(downloadedSize)) + "/" + (bytes2String(lenghtOfFile)));
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (mProgressDialog != null) mProgressDialog.dismiss();

            File pdfFile = new File(MagDir(mContext).getAbsolutePath() +"/"+ fileName);  // -> filename = maven.pdf
            Uri path = Uri.fromFile(pdfFile);

            //Uri uri = Uri.parse( AcceuilFrag.getAssetsPdfPath(mContext, "27092016.pdf"));
            Intent intent = new Intent(mContext, MuPDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(path);
            mContext.startActivity(intent);

            notifyDataSetChanged();
        }

        @Override
        protected void onCancelled() {
            if(isFileExiste(fileName)) {
                new File(MagDir(mContext).getAbsolutePath() + "/" + fileName).delete();
            }
        }

    }

    public static String bytes2String(long sizeInBytes) {

        NumberFormat nf = new DecimalFormat("##.##");
        nf.setMaximumFractionDigits(2);

        try {
            if ( sizeInBytes < SPACE_KB ) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if ( sizeInBytes < SPACE_MB ) {
                return nf.format(sizeInBytes/SPACE_KB) + " KB";
            } else if ( sizeInBytes < SPACE_GB ) {
                return nf.format(sizeInBytes/SPACE_MB) + " MB";
            } else if ( sizeInBytes < SPACE_TB ) {
                return nf.format(sizeInBytes/SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes/SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }

    }
    private static File MagDir(Context c) {

        File mydir;

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            mydir = new File(android.os.Environment.getExternalStorageDirectory(), "Android/data/com.wat.pqdmag/journaux");
        else
            mydir = new File(c.getFilesDir(), "journaux");

        if (!mydir.exists()) {
            mydir.mkdirs();
        }

        return mydir;
    }

    private  boolean isFileExiste(String fileName){

        return new File(MagDir(mContext).getAbsolutePath() +"/"+ fileName).exists();
    }

    private String getFileNameFormUrl(String url){
        String baseName = FilenameUtils.getBaseName(url);
        String extension = FilenameUtils.getExtension(url);

        return baseName+"."+extension;
    }

}
