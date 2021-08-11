package whatsapp.whtools.statusdownloader.Fragment;


import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import whatsapp.whtools.statusdownloader.Adapters.AllVideoSongsAdapter;
import whatsapp.whtools.statusdownloader.Model.VideoSongs;
import whatsapp.whtools.statusdownloader.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WhatsappFragmentVideos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatsappFragmentVideos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout image_linearlayout;
    RecyclerView recyclerView;
    ImageButton status_download_btn;
    private int MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE = 1230;
    AllVideoSongsAdapter videoSongsAdapter;
    ArrayList<VideoSongs> videoSongsList = new ArrayList<VideoSongs>();
    public static List<String> list2 = null;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ProgressDialog progressDialog;
    int REQUEST_ID_MULTIPLE_PERMISSIONS = 123;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView missedcalled_recyler;

    public WhatsappFragmentVideos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatsappFragmentVideos newInstance(String param1, String param2) {
        WhatsappFragmentVideos fragment = new WhatsappFragmentVideos();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        image_linearlayout = view.findViewById(R.id.image_linearlayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        recyclerView = view.findViewById(R.id.videoSearchrecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
       recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (checkStoragePermission()) {
                try {
                    getData();
                } catch (Exception d) {
                }
            } else {
                requestStoragePermission();
            }
        } else {
            getData();
        }

        return view;
    }

    public void getData() {
        if (getFilePaths().size() == 0) {
            image_linearlayout.setVisibility(View.VISIBLE);
        } else {
            videoSongsAdapter = new AllVideoSongsAdapter(getActivity(), getFilePaths(), "");
            recyclerView.setAdapter(videoSongsAdapter);
            videoSongsAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<VideoSongs> getFilePaths() {
        ArrayList<VideoSongs> resultIAV = new ArrayList<VideoSongs>();
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses");
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
                    return (name.endsWith(".mp4")
                            || name.endsWith(".3gp")
                            || name.endsWith(".mpeg"));
                }
            });
            for (File imagePath : allFiles) {
                VideoSongs path = new VideoSongs();
                path.setData(imagePath.getAbsolutePath());
                resultIAV.add(path);
            }
        } catch (NullPointerException e) {
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getData();
            } else {
                checkStoragePermission();
            }
        }
    }

    private boolean checkStoragePermission() {

        return ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READANDWRITE_EXTERNAL_STORAGE);


    }
}
