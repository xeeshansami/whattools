package whatsapp.whtools.statusdownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import whatsapp.whtools.statusdownloader.Adapters.HistoryAdapter;
import whatsapp.whtools.statusdownloader.Fragment.Image;
import whatsapp.whtools.statusdownloader.Model.Images;

import whatsapp.whtools.statusdownloader.R;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    ImageView back_btn;
    RecyclerView recyclerView;

    LinearLayout image_linearlayout;
    HistoryAdapter videoSongsAdapter;
    ArrayList<Images> imagesList = new ArrayList<Images>();
    public static List<String> list2 = null;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ProgressDialog progressDialog;
    ImageButton status_download_btn;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static Image inst;
    RecyclerView missedcalled_recyler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_history);

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        recyclerView = findViewById(R.id.videoSearchrecycler);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        image_linearlayout = findViewById(R.id.image_linearlayout);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        recyclerView = findViewById(R.id.videoSearchrecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
//        loadImages();
        if (getFilePaths().size() == 0) {
            image_linearlayout.setVisibility(View.VISIBLE);
        } else {
            videoSongsAdapter = new HistoryAdapter(getApplicationContext(), getFilePaths(), "B");
            recyclerView.setAdapter(videoSongsAdapter);
            videoSongsAdapter.notifyDataSetChanged();
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

    public void loadImages() {
        ArrayList<Images> images = null;
        try {

            imagesList.clear();
            Map<String, String> snapshot = null;
            synchronized (AppClass.mVideoPathCache) {
                snapshot = AppClass.mVideoPathCache.snapshot();
            }
            String whatsapp = "/storage/emulated/0/WhatsApp/Media/.Statuses";
            for (String id : snapshot.keySet()) {
                Log.i("Allfoldersname", id + "");
                Object myObject = AppClass.mVideoPathCache.get(id);
                if (whatsapp.equalsIgnoreCase(myObject.toString())) {
                    if (id.endsWith(".jpg")) {
                        Images images1 = new Images();
                        images1.setData(id);
                        Log.i("tage", myObject + " = " + AppClass.folder.get(0).getPath());
                        images1.setName(id.substring(id.lastIndexOf("/") + 1, id.length() - 4));
                        images1.setSize(getFileSize(new File(id).length()));
                        imagesList.add(images1);
                        //   getFiles(id);
                    }
                }
            }
        } catch (Exception e) {
            Log.i("exceptionVideos", e.getMessage());
        }
        Log.i("vidoes", imagesList.size() + "");

    }

    public ArrayList<Images> getFilePaths() {
        ArrayList<Images> resultIAV = new ArrayList<Images>();
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatstatusFolder");
//        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FreeVideoDownloader");
//        int songNumb = folder.listFiles(new FileFilter() {
//
//            @Override
//            public boolean accept(File file) {
//                if (file.isFile()) {
//                    if (file.getName().equals(".nomedia"))
//                        return false;
//                } else if (file.isDirectory()) {
//                    return false;
//                } else if (file.isHidden()) {
//                    return true;
//                } else
//                    return false;
//                return true;
//            }
//
//        }).length;


//                    folder.mkdirs();
        try {
            File[] allFiles = folder.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
//                if (dir.isHidden()) {
//                    return true;
//                }
//                return true;
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".mp4")
                            || name.endsWith(".3gp")
                            || name.endsWith(".mpeg"));
                }
            });
            for (File imagePath : allFiles) {
                Images path = new Images();
                path.setData(imagePath.getAbsolutePath());
                resultIAV.add(path);
            }
        }catch (NullPointerException e){}

//        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Images.ImageColumns.DATA};
//        Cursor c = null;
//        SortedSet<String> dirList = new TreeSet<String>();

//        String selection = "images"+ " like ?";
//        String[] selectionArgs = new String[]{"%FreeVideoDownloader%"};
//        String[] directories = null;
//        if (u != null) {
//            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
//            c = getActivity().getContentResolver().query(u, projection, selection, selectionArgs, orderBy + " DESC");
//        }
//
//        if ((c != null) && (c.moveToFirst())) {
//            do {
//                String tempDir = c.getString(0);
//                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
//                try {
//                    dirList.add(tempDir);
//                } catch (Exception e) {
//                }
//            }
//            while (c.moveToNext());
//            directories = new String[dirList.size()];
//            dirList.toArray(directories);
//
//        }
//
//        for (int i = 0; i < dirList.size(); i++) {
//            File imageDir = new File(directories[i]);
//            File[] imageList = imageDir.listFiles();
//            if (imageList == null)
//                continue;
//            for (File imagePath : imageList) {
//                try {
//
//                    if (imagePath.isDirectory()) {
//                        imageList = imagePath.listFiles();
//
//                    }
//                    if (imagePath.getName().contains(".jpg") || imagePath.getName().contains(".JPG")
//                            || imagePath.getName().contains(".jpeg") || imagePath.getName().contains(".JPEG")
//                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
//                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
//                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
//                            ) {
//
//                        Images path = new Images();
//                        path.setData(imagePath.getAbsolutePath());
//                        resultIAV.add(path);
//                    }
//                }
//                //  }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return resultIAV;
    }


    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
