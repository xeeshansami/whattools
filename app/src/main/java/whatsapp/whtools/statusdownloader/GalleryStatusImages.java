package whatsapp.whtools.statusdownloader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import whatsapp.whtools.statusdownloader.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GalleryStatusImages extends AppCompatActivity {

    // done popup
    Dialog doneDialog;

    ImageView srcimage;
    ImageView  status_download_btn;
    String imageSrc, from;
    TextView toolbar_tv;
    boolean firstAndSecondTime = true;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp_status_images);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        doneDialog = new Dialog(this);

        Bundle bundle = getIntent().getExtras();
        imageSrc = bundle.getString("srcimage");
        from = bundle.getString("from");
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar_tv = findViewById(R.id.toolbar_tv);
        status_download_btn = findViewById(R.id.status_download_btn);
        if (from.equalsIgnoreCase("A")) {
            status_download_btn.setVisibility(View.GONE);
        } else {
            status_download_btn.setVisibility(View.GONE);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backicon));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
//        showRewardAd("");
        srcimage = findViewById(R.id.srcimage);
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageSrc, MediaStore.Video.Thumbnails.MICRO_KIND);
        Log.i("srcImageValue", imageSrc);
        toolbar_tv.setText(imageSrc.substring(imageSrc.lastIndexOf('/') + 1));
        status_download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) srcimage.getDrawable()).getBitmap();
                popup(bitmap, imageSrc);
            }
        });
        srcimage.setImageURI(Uri.parse(imageSrc));
    }

    public void saveeFile(Bitmap bmp, String path) {
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
        path = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);

        String Foldername = "WhatstatusFolder";
        File mBaseFolderPath = new File(Environment.getExternalStorageDirectory(), Foldername);
        if (!mBaseFolderPath.exists() && mBaseFolderPath.isDirectory()) {
            new File(String.valueOf(mBaseFolderPath)).mkdir();
        }
        try {
            FileOutputStream out = new FileOutputStream(mBaseFolderPath + "/" + path + ".jpeg");
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            out.flush();
            out.close();
//            Toast.makeText(this, "Photo has been saved", Toast.LENGTH_SHORT).show();
            donePopup();
        } catch (IOException e) {
            Toast.makeText(this, "Something went wrong, Try again later", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void popup(final Bitmap bitmapAs, final String filename) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Photo : " + filename)
                .setMessage("Do you want to save this photo?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doFunc(bitmapAs, filename);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    public void doFunc(Bitmap bitmapAs, String Filename) {
        /* Create an Intent that will start the Menu-Activity. */
        saveeFile(bitmapAs, Filename);
    }

    //------------------------------start here donePopup-------------------------------
    public void donePopup() {

        doneDialog.setContentView(R.layout.done_popup);


        doneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        doneDialog.show();

    }
}
