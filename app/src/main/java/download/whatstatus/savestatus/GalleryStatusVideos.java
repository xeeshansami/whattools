package download.whatstatus.savestatus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import download.whatstatus.savestatus.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GalleryStatusVideos extends  AppCompatActivity {


    // done popup
    Dialog doneDialog;
    ImageView status_download_btn;
    boolean firstAndSecondTime = true;
    TextView toolbar_tv;
    private VideoView mVideoView;
    MediaController mediaController;

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_status_videos);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        doneDialog = new Dialog(this);

        mVideoView = findViewById(R.id.videoview);
        mediaController = new MediaController(this);
        initializePlayer();
    }



    private void initializePlayer() {
        Bundle bundle = getIntent().getExtras();
        final String videoUrli = bundle.getString("srcVideo");
        String filename = videoUrli.substring(videoUrli.lastIndexOf('/') + 1);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar_tv = findViewById(R.id.toolbar_tv);
        toolbar_tv.setText(filename);
        status_download_btn = findViewById(R.id.status_download_btn);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backicon));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        mVideoView.setVideoURI(Uri.parse(videoUrli));
        mVideoView.setMediaController(mediaController);
        mVideoView.setOnPreparedListener(mediaPlayer ->
                mediaPlayer.setOnVideoSizeChangedListener(
                        (player, width, height) -> {
                            MediaController controller = new MediaController(this);
                            mVideoView.setMediaController(controller);
                            controller.setAnchorView(mVideoView);
                        }
                )
        );
        mediaController.setEnabled(true);
        mVideoView.start();
        status_download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup(videoUrli);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
//        releasePlayer();
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();

    }

    private void saveVideoToInternalStorage(String filePath) {
        try {
            File newfile;
            AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(Uri.parse("file://" + filePath), "r");
            FileInputStream in = videoAsset.createInputStream();
            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getPath() + "/" + "WhatstatusFolder" + "/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
            newfile = new File(dir, filename + " - " + System.currentTimeMillis() + ".mp4");
            if (newfile.exists()) newfile.delete();
            OutputStream out = new FileOutputStream(newfile);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Log.v("", "Copy file successful.");
//            Toast.makeText(this, "Video has been saved", Toast.LENGTH_SHORT).show();
            donePopup();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, Try again later", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void popup(final String filename) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Video : " + filename)
                .setMessage("Do you want to save this Video?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doFunc(filename);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void doFunc(String Filename) {
        /* Create an Intent that will start the Menu-Activity. */
        saveVideoToInternalStorage(Filename);
    }

    //------------------------------start here donePopup-------------------------------
    public void donePopup() {

        doneDialog.setContentView(R.layout.done_popup);


        doneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        doneDialog.show();

    }
}
