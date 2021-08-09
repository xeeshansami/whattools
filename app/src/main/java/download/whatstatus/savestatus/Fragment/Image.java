package download.whatstatus.savestatus.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import download.whatstatus.savestatus.Adapters.GalleryImagesAdapter;
import download.whatstatus.savestatus.AppClass;
import download.whatstatus.savestatus.Model.Images;
import download.whatstatus.savestatus.R;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Image extends  Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout image_linearlayout;
    RecyclerView recyclerView;
    GalleryImagesAdapter videoSongsAdapter;
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
   ProgressBar progress;
    public Image() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentImages.
     */
    // TODO: Rename and change types and number of parameters
    public static Image newInstance(String param1, String param2) {
        Image fragment = new Image();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static Image instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.gallery_image, container, false);
        image_linearlayout = view.findViewById(R.id.image_linearlayout);
        progress = view.findViewById(R.id.progressBar1);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        recyclerView = view.findViewById(R.id.videoSearchrecycler);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        loadImages();
        if (getFilePaths().size() == 0) {
            image_linearlayout.setVisibility(View.VISIBLE);
        } else {
            AsyncLoadVideos asyncLoadVideosAndFolder = new AsyncLoadVideos();
            asyncLoadVideosAndFolder.execute();
        }
        return view;
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
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Whatsapp/Media/Whatsapp Images/");
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
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
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

    private class AsyncLoadVideos extends AsyncTask<Boolean, Integer, Void> {
        boolean isRefresh;
        String sorter;
        ArrayList<Images> img ;
        private AsyncLoadVideos() {
            progress.setVisibility(View.VISIBLE);
        }

        protected void onPreExecute() {
//            progressDialog.setVisibility(View.VISIBLE);
//            emptyTextview.setVisibility(View.GONE);
            img = new ArrayList<>();
            progress.setVisibility(View.VISIBLE);
        }

        @SuppressLint("LongLogTag")
        protected Void doInBackground(Boolean... args) {

            try {
                img=   getFilePaths();


            } catch (RuntimeException e) {
            }
            return null;
        }


        @SuppressLint("LongLogTag")
        protected void onPostExecute(Void result) {
            progress.setVisibility(View.GONE);
            videoSongsAdapter = new GalleryImagesAdapter(getActivity(), img, "B");
            recyclerView.setAdapter(videoSongsAdapter);
            videoSongsAdapter.notifyDataSetChanged();

        }
    }

}
