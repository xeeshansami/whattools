package download.whatstatus.savestatus;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import download.whatstatus.savestatus.Adapters.AppAdapter;
import download.whatstatus.savestatus.Adapters.CircleImagesAdapter;
import download.whatstatus.savestatus.DBHelper.DBHelper;
import download.whatstatus.savestatus.Model.Images;

import download.whatstatus.savestatus.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class ShowAllStatusActivity extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    public  static final int RequestPermissionCode  = 1 ;
    private static final int SELECT_PHOTO = 7777;
    private static final int CAM_REQUEST = 1313;
    Bitmap bitmap;
    private Uri filePath = null;
    DBHelper dbHelper;
    ImageView invisible_imageView;
    ArrayList<Images> resultIAV;
    ImageView bottomPlus,profileImg;
    LinearLayout bottomOption;
    LinearLayout minimizeView;
    RecyclerView recyclerView;
    ImageView back_btn;
    LinearLayout business_click,simple_what_click;
    ImageView cameraImg;
    private InterstitialAd interstitial;


    // select photo popup
    ImageView circle_cancel_select_popup;
    Button gallery_btn,camera_btn;
    Dialog photoDialog;

    ImageView downloderIMG,settingIMG,historyIMG;
    ArrayList<Images> imagesList = new ArrayList<Images>();
    CircleImagesAdapter videoSongsAdapter;
    TextView status_not_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            setContentView(R.layout.activity_show_all_status);
        }
        catch (Exception e)
        {
            Log.i("mytag",e.getMessage());
        }

//        IntersitialAds();
       resultIAV = new ArrayList<>();
        status_not_show = findViewById(R.id.status_not_show);
        downloderIMG = findViewById(R.id.downloderIMG);
        settingIMG = findViewById(R.id.settingIMG);
        historyIMG = findViewById(R.id.historyIMG);
        settingIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllStatusActivity.this,SettingActivity.class));
            }
        });
        downloderIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllStatusActivity.this,DownloaderActivity.class));
            }
        });

        historyIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllStatusActivity.this,HistoryActivity.class));
            }
        });


        photoDialog = new Dialog(this);
        profileImg = findViewById(R.id.profileImg);

        try{
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            String bitImage = sharedPreferences.getString("profileImage", "");

            byte [] encodeByte= Base64.decode(bitImage,Base64.DEFAULT);
            Bitmap dp= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            profileImg.setImageBitmap(dp);

        }catch(Exception e){
            e.getMessage();

        }


        EnableRuntimePermission();


        cameraImg = findViewById(R.id.camera_img);
        cameraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPopup();

            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              PhotoPopup();
            }
        });

        simple_what_click = findViewById(R.id.whatsApp_layout);
        simple_what_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllStatusActivity.this,WhatsappStatusActivity.class));
            }
        });

        business_click = findViewById(R.id.whatsApp_bus_layout);
        business_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ShowAllStatusActivity.this,WhatsappBusinessStatusActivity.class));
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        minimizeView = findViewById(R.id.minimizeView);
        recyclerView = findViewById(R.id.recyclerView);
        bottomPlus = findViewById(R.id.bottomPlus);
        bottomOption = findViewById(R.id.bottomOption);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        loadImages();
        try {
            if (getFilePaths().size() == 0) {

                status_not_show.setVisibility(View.VISIBLE);

            } else {
                videoSongsAdapter = new CircleImagesAdapter(getApplicationContext(), resultIAV, "B");
                recyclerView.setAdapter(videoSongsAdapter);
                videoSongsAdapter.notifyDataSetChanged();
            }

        }catch (Exception e){}
        // slide-up animation
        final Animation slideUp      = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        final Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        minimizeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomOption.setVisibility(View.GONE);
                bottomOption.startAnimation(slideDown);
                bottomPlus.setVisibility(View.VISIBLE);
            }
        });
        bottomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomOption.setVisibility(View.VISIBLE);
                bottomOption.startAnimation(slideUp);
                bottomPlus.setVisibility(View.GONE);
            }
        });


       new LoadApplications();
    }


    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            getFilePaths();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            progress.dismiss();
            videoSongsAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
           super.onPreExecute();
        }
    }

    public void EnableRuntimePermission(){


            ActivityCompat.requestPermissions(ShowAllStatusActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

       }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
              } else {

                    Toast.makeText(ShowAllStatusActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }





    //------------------------------start here photoPopup-------------------------------
    @Keep
    public void PhotoPopup() {

        photoDialog.setContentView(R.layout.select_popup);
        ImageView circle_cancel_select_popup = (ImageView) photoDialog.findViewById(R.id.circle_cancel_select_popup);
        circle_cancel_select_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog.dismiss();
            }
        });
        gallery_btn = (Button) photoDialog.findViewById(R.id.gallerybtn);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkcamerapermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECT_PHOTO);
                    photoDialog.dismiss();
                }
                else
                {
                    requestcameraPermission();


                }
            }
        });
        camera_btn = (Button) photoDialog.findViewById(R.id.camerabtn);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intentt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentt.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION);*/

              if(checkcamerapermission()) {
                  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                  startActivityForResult(intent, CAM_REQUEST);
                  photoDialog.dismiss();
              }
              else
              {
                  requestcameraPermission();
              }

            }
        });

        photoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        photoDialog.show();

    }

    @Keep
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] arr=baos.toByteArray();
                String result=Base64.encodeToString(arr, Base64.DEFAULT);

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("profileImage", result);
                editor.apply();

                profileImg.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null ) {

            bitmap  = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] arr=baos.toByteArray();
            String result=Base64.encodeToString(arr, Base64.DEFAULT);

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profileImage", result);
            editor.apply();
            profileImg.setImageBitmap(bitmap);

        }
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


    public ArrayList<Images> getFilePaths() {

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");

        try {
            File[] allFiles = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {

                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });
            for (File imagePath : allFiles) {
                Images path = new Images();
                path.setData(imagePath.getAbsolutePath());
                resultIAV.add(path);
            }
        }catch (NullPointerException e){}

        return resultIAV;
    }


    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    private boolean checkcamerapermission() {
        return ActivityCompat.checkSelfPermission(ShowAllStatusActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestcameraPermission() {

        ActivityCompat.requestPermissions(ShowAllStatusActivity.this,
                new String[]{Manifest.permission.CAMERA},
               1313);


    }


    /*public void IntersitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(ShowAllStatusActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getResources().getString(R.string.intersitial_as_unit_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        });
    }*/



}
