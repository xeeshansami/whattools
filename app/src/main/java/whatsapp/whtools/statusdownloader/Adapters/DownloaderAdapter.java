package whatsapp.whtools.statusdownloader.Adapters;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import whatsapp.whtools.statusdownloader.BuildConfig;
import whatsapp.whtools.statusdownloader.GalleryStatusImages;
import whatsapp.whtools.statusdownloader.GalleryStatusVideos;
import whatsapp.whtools.statusdownloader.Model.Images;
import whatsapp.whtools.statusdownloader.Model.VideoSongs;
import whatsapp.whtools.statusdownloader.R;
import whatsapp.whtools.statusdownloader.onItemClickListener;

import java.io.File;
import java.util.ArrayList;

public class DownloaderAdapter extends RecyclerView.Adapter<DownloaderAdapter.myView> {
    public static ArrayList<Images> listData;
    public static ArrayList<Images> mFilteredList = new ArrayList<Images>();
    private final boolean clear_cache;
    private LayoutInflater inflater;
    private Context context;
    VideoSongs songsClass;
    ArrayList<Images> list;
    static String nameofSOngs;
    static Bitmap sourceofImage;
    static int pos;
    File file;
    private int lastPosition = -1;
    boolean multiple = false;
    ArrayList<Integer> strings;
    String from;
    String adding = "false";
    String insert_query, select_query, delete_query;
    Cursor c;
    // DataBaseManager db;
    String root;
    int folderposition;
    static int onevideo;
    private SharedPreferences mSharedPrefs = null;
    View v;
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;

    public DownloaderAdapter(Context context, ArrayList<Images> arraylist, String from) {
        this.from=from;
        inflater = LayoutInflater.from(context);
        this.list = arraylist;
        this.context = context;
        this.listData = arraylist;
        this.mFilteredList = arraylist;

        if (!from.equalsIgnoreCase("search")) {

            for (int k = 0; k < listData.size(); k++) {
                if (k % 9 == 0) {

                    if (k != 0) {
                    }
                }
            }
        }


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        clear_cache = sharedPreferences.getBoolean(context.getResources().getString(R.string.clear_cache_key), true);
        System.out.println("MainStickers Switch State" + clear_cache);
    }

    @Override
    public DownloaderAdapter.myView onCreateViewHolder(ViewGroup parent, int viewType) {


        v = null;
        RecyclerView.ViewHolder vh = null;
        v = inflater.inflate(R.layout.downloader_card, parent, false);
        //vh = new ContentViewHolder(v);
        strings = new ArrayList<>();

        /*
        db = new DataBaseManager(v.getContext());
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        root = Environment.getExternalStorageDirectory().toString();


        return new DownloaderAdapter.myView(v);

    }


    @Override
    public void onBindViewHolder(final DownloaderAdapter.myView holder, final int position) {
        try {
//            Animation animation = AnimationUtils.loadAnimation(context,
//                    (position > lastPosition) ? R.anim.up_from_bottom
//                            : R.anim.down_from_top);
//            holder.itemView.startAnimation(animation);
            lastPosition = position;
            final Images songsClass = list.get(position);

            if (getItemViewType(position) == AD_TYPE) {


            } else {
                Log.d(getClass().getSimpleName(), "#" + position);
                holder.bind(position);

//                holder.title.setText(songsClass.getName());
//                holder.sizeofVideo.setText(songsClass.getSize());
                String path = Environment.getExternalStorageDirectory().getPath() + "/.thumbs/";
                path = path + songsClass.getName() + ".png";

                try {
                    //    Picasso.with(context).load("file://" + path).skipMemoryCache().fit().placeholder(R.drawable.head).error(R.drawable.head).centerInside().into(holder.proimage);

                    /*Glide.with(context)
                            .load("file://" + songsClass.getData()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.video_icon)
                            .into(holder.proimage);*/

                    Glide.with(context)
                            .load(songsClass.getData())
                            .thumbnail(0.2f)
                            .into(holder.proimage);

                    String nameThumb = songsClass.getData();
//                    holder.imageName.setText(nameThumb);
                    String filenameArray[] = nameThumb.split("\\.");
                    String extension = filenameArray[filenameArray.length-1];
                    System.out.println(extension);
                    holder.imageType.setText("File type \n"+"."+extension);
                    holder.imageName .setText("File name \n"+nameThumb.substring(nameThumb.lastIndexOf('/') + 1));
                } catch (Exception fddd) {

                }
//                holder.duration.setText(songsClass.getDuration());
            }

            holder.setItemClickListener(new onItemClickListener() {
                @Override
                public void onClick(View view, int itemPos) {

                    String nameThumb = songsClass.getData();

                    if (nameThumb.endsWith(".jpg") || nameThumb.endsWith(".jpeg")|| nameThumb.endsWith(".png"))
                    {
                       Intent intent = new Intent(context, GalleryStatusImages.class);
                    intent.putExtra("srcimage", songsClass.getData());
                    intent.putExtra("from", from);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    }else   if (nameThumb.endsWith(".mp4") || nameThumb.endsWith(".3gp")|| nameThumb.endsWith(".mpeg"))
                    {
                        Intent intent = new Intent(context, GalleryStatusVideos.class);
                        intent.putExtra("srcVideo", songsClass.getData());
                        intent.putExtra("from", from);

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }
            });
        } catch (IndexOutOfBoundsException e) {
        } catch (RuntimeException e) {
            Log.i("RuntimeException", e.getMessage());
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        int a = 0;
        try {
            a = listData.size();
        } catch (IndexOutOfBoundsException e) {
        }
        return a;
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null)
            return AD_TYPE;
        return CONTENT_TYPE;
    }


    public class myView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, duration, artist, id, sizeofVideo;
        String insert_query, select_query, delete_query;
        Cursor c;
        //DataBaseManager db;

        //        TextView proname;
        ImageView proimage;
        TextView imageType,imageName;
        onItemClickListener itemClickListener;

        public myView(final View itemView) {
            super(itemView);

            final ContentResolver contentResolver = itemView.getContext().getContentResolver();

          /*
            db = new DataBaseManager(itemView.getContext());

            try {
                db.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

            */


//            title = (TextView) itemView.findViewById(R.id.last_text);
//            duration = (TextView) itemView.findViewById(R.id.last_text_time);
//            sizeofVideo = (TextView) itemView.findViewById(R.id.sizeOfSons);

            proimage = (ImageView) itemView.findViewById(R.id.status_downIMG);
            imageName = (TextView) itemView.findViewById(R.id.imageName);
            imageType =  (TextView) itemView.findViewById(R.id.imageType);
            itemView.setOnClickListener(this);
        }


        void bind(int listIndex) {
//            title.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getLayoutPosition());
        }


        public void setItemClickListener(onItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public void shareVideoWhatsApp(String v) {
            File file = new File(v);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.setPackage("com.whatsapp");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM, uri);
            itemView.getContext().startActivity(videoshare);
        }
    }

    @Override
    public void onViewDetachedFromWindow(DownloaderAdapter.myView holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class MediaFileFunctions {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public boolean deleteViaContentProvider(Context context, String fullname) {
            Uri uri = getFileUri(context, fullname);

            if (uri == null) {
                return false;
            }

            try {
                ContentResolver resolver = context.getContentResolver();

                // change type to image, otherwise nothing will be deleted
                ContentValues contentValues = new ContentValues();
                int media_type = 1;
                contentValues.put("media_type", media_type);
                resolver.update(uri, contentValues, null, null);

                return resolver.delete(uri, null, null) > 0;
            } catch (Throwable e) {
                return false;
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private Uri getFileUri(Context context, String fullname) {
            // Note: check outside this class whether the OS version is >= 11
            Uri uri = null;
            Cursor cursor = null;
            ContentResolver contentResolver = null;

            try {
                contentResolver = context.getContentResolver();
                if (contentResolver == null)
                    return null;

                uri = MediaStore.Files.getContentUri("external");
                String[] projection = new String[2];
                projection[0] = "_id";
                projection[1] = "_data";
                String selection = "_data = ? ";    // this avoids SQL injection
                String[] selectionParams = new String[1];
                selectionParams[0] = fullname;
                String sortOrder = "_id";
                cursor = contentResolver.query(uri, projection, selection, selectionParams, sortOrder);

                if (cursor != null) {
                    try {
                        if (cursor.getCount() > 0) // file present!
                        {
                            cursor.moveToFirst();
                            int dataColumn = cursor.getColumnIndex("_data");
                            String s = cursor.getString(dataColumn);
                            if (!s.equals(fullname))
                                return null;
                            int idColumn = cursor.getColumnIndex("_id");
                            long id = cursor.getLong(idColumn);
                            uri = MediaStore.Files.getContentUri("external", id);
                        } else // file isn't in the media database!
                        {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("_data", fullname);
                            uri = MediaStore.Files.getContentUri("external");
                            uri = contentResolver.insert(uri, contentValues);
                        }
                    } catch (Throwable e) {
                        uri = null;
                    } finally {
                        cursor.close();
                    }
                }
            } catch (Throwable e) {
                uri = null;
            }
            return uri;
        }
    }

}

