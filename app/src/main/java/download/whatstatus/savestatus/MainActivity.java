package download.whatstatus.savestatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import download.whatstatus.savestatus.R;

import download.whatstatus.savestatus.activities.RequestPermissionActivity;

public class MainActivity extends AppCompatActivity {

    Dialog rateDialog;
    ImageView menuOpen;
    RelativeLayout msg_layout, status_layout, cleaner_layout, gallery_layout, share_layout, rate_layout;
    ImageView status_img, msg_img, cleaner_img, gallery_img, share_img, rate_img;
    private int MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE = 1230;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        requestStoragePermission();
        notificationbuilder();
        menuOpen = findViewById(R.id.menuOpen);
        menuOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuOpen);
                popupMenu.getMenuInflater().inflate(R.menu.set_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.nav_bug:
                                Intent email = new Intent(Intent.ACTION_SEND);
                                String to = "duskydose@gmail.com";
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                                email.putExtra(Intent.EXTRA_SUBJECT, "Bugs report");

                                email.setType("message/rfc822");

                                startActivity(Intent.createChooser(email, "Choose an Email client :"));

                                return true;
                            case R.id.nav_policy:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://whatstatusapp.blogspot.com/p/whatstatus.html"));
                                startActivity(browserIntent);
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popupMenu.show();
            }
        });

        rateDialog = new Dialog(this);

        //imageViews dec
        msg_img = findViewById(R.id.msg_img);
        status_img = findViewById(R.id.status_img);
        cleaner_img = findViewById(R.id.cleaner_img);
        gallery_img = findViewById(R.id.gallery_img);
        share_img = findViewById(R.id.share_img);
        rate_img = findViewById(R.id.rate_img);

        //RelativeLayout dec
        msg_layout = findViewById(R.id.msg_layout);
        status_layout = findViewById(R.id.saver_layout);
        cleaner_layout = findViewById(R.id.cleaner_layout);
        gallery_layout = findViewById(R.id.gallery_layout);
        share_layout = findViewById(R.id.share_layout);
        rate_layout = findViewById(R.id.rate_layout);

        msg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, MessageUnsaveActivity.class));
            }
        });

        status_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ShowAllStatusActivity.class));
            }
        });

        cleaner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, CleanActivity.class));
            }
        });

        gallery_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });

        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, RequestPermissionActivity.class));

            }
        });

        rate_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, WhatsAppDeletedMsgActivity.class));
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                checkStoragePermission();
            }
        }
    }

    private boolean checkStoragePermission() {

        return ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void notificationbuilder() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("WhatStatus", "WhatStatus", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("WhatStatus")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("subscribe horha hai");
                        }
                    }
                });
    }
}
