package whatsapp.whtools.statusdownloader.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import whatsapp.whtools.statusdownloader.R;
import whatsapp.whtools.statusdownloader.constants.Constants;
import whatsapp.whtools.statusdownloader.utils.FileUtils;
import whatsapp.whtools.statusdownloader.utils.StickerPacksManager;
import whatsapp.whtools.statusdownloader.whatsapp_api.Sticker;
import whatsapp.whtools.statusdownloader.whatsapp_api.StickerContentProvider;
import whatsapp.whtools.statusdownloader.whatsapp_api.StickerPack;
import whatsapp.whtools.statusdownloader.whatsapp_api.StickerPackDetailsActivity;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewStickerPackActivity extends AppCompatActivity {

    ImageAdapter imageAdapter;
    EditText nameEdit;
    EditText authorEdit;
    Button btn_save_pack;
    ImageView backicon;

    // stickers msg popup
    ImageView circle_cancel_select_popup;
    Dialog stickerDialog;
    boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sticker_pack);

    /*    AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        stickerDialog = new Dialog(this);
        stickerPopup();

        // UI references.
        nameEdit = findViewById(R.id.sticker_pack_name_edit);
        btn_save_pack = findViewById(R.id.btn_save_pack);
        btn_save_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateValues()) {
                    Toast.makeText(NewStickerPackActivity.this, "Please fill all data and attech minimum 3  images", Toast.LENGTH_SHORT).show();
                } else {
                    saveStickerPack(imageAdapter.uries, nameEdit.getText().toString(), authorEdit.getText().toString());
                }
            }
        });
        backicon = findViewById(R.id.back_btn);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        authorEdit = findViewById(R.id.sticker_pack_author_edit);
        Button btnCreate = findViewById(R.id.btn_create_pack);
        btnCreate.setOnClickListener(v -> {
            FishBun.with(NewStickerPackActivity.this)
                    .setImageAdapter(new GlideAdapter())
                    .setMaxCount(30)
                    .exceptGif(true)
                    .setActionBarColor(Color.parseColor("#128c7e"), Color.parseColor("#128c7e"), false)
                    .setMinCount(3).setActionBarTitleColor(Color.parseColor("#ffffff"))
                    .startAlbum();
        });

        GridView gridview = findViewById(R.id.sticker_pack_grid_images_preview);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener((parent, v, position, id) -> {
            Toast.makeText(NewStickerPackActivity.this, "Image removed", Toast.LENGTH_SHORT).show();
            imageAdapter.uries.remove(position);
            imageAdapter.notifyDataSetChanged();
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_sticker_pack, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_sticker_pack) {
            if (validateValues()) {
                Toast.makeText(this, "Please fill all data and attech minimum 3  images", Toast.LENGTH_SHORT).show();
            } else {
                saveStickerPack(imageAdapter.uries, nameEdit.getText().toString(), authorEdit.getText().toString());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateValues() {
        return nameEdit.getText().toString().trim().length() == 0 || authorEdit.getText().toString().trim().length() == 0 || imageAdapter.uries.size() == 0;
    }

    private void saveStickerPack(List<Uri> uries, String name, String author) {
        try {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Wait a moment while we process your stickers..."); // Setting Message
            progressDialog.setTitle("Processing images"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            new Thread(() -> {
                try {

                    Intent intent = new Intent(NewStickerPackActivity.this, StickerPackDetailsActivity.class);
                    intent.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, true);

                    String identifier = "." + FileUtils.generateRandomIdentifier();
                    StickerPack stickerPack = new StickerPack(identifier, name, author, Objects.requireNonNull(uries.toArray())[0].toString(), "", "", "", "");

                    //Save the sticker images locally and get the list of new stickers for pack
                    List<Sticker> stickerList = StickerPacksManager.saveStickerPackFilesLocally(stickerPack.identifier, uries, NewStickerPackActivity.this);
                    stickerPack.setStickers(stickerList);

                    //Generate image tray icon
                    String stickerPath = Constants.STICKERS_DIRECTORY_PATH + identifier;
                    String trayIconFile = FileUtils.generateRandomIdentifier() + ".png";
                    StickerPacksManager.createStickerPackTrayIconFile(uries.get(0), Uri.parse(stickerPath + "/" + trayIconFile), NewStickerPackActivity.this);
                    stickerPack.trayImageFile = trayIconFile;

                    //Save stickerPack created to write in json
                    StickerPacksManager.stickerPacksContainer.addStickerPack(stickerPack);
                    StickerPacksManager.saveStickerPacksToJson(StickerPacksManager.stickerPacksContainer);
                    insertStickerPackInContentProvider(stickerPack);

                    //Start new activity with stickerpack information
                    intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, stickerPack);
                    startActivity(intent);
                    NewStickerPackActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }).start();
        }catch (OutOfMemoryError e){}
    }

    private void insertStickerPackInContentProvider(StickerPack stickerPack) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("stickerPack", new Gson().toJson(stickerPack));
        getContentResolver().insert(StickerContentProvider.AUTHORITY_URI, contentValues);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Define.ALBUM_REQUEST_CODE) {

            ArrayList<Uri> uries = new ArrayList<>();
            if (resultCode == RESULT_OK) {
                uries = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                if (uries.size() > 0) {
                    imageAdapter.uries = uries;
                    imageAdapter.notifyDataSetChanged();
                    ((TextView) findViewById(R.id.stickers_selected_textview)).setText(uries.size() + " stickers selected");
                }
            }
        }
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return uries.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            SimpleDraweeView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new SimpleDraweeView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setAdjustViewBounds(true);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (SimpleDraweeView) convertView;
            }

            imageView.setImageURI(uries.get(position));
            return imageView;
        }

        // references to our images
        ArrayList<Uri> uries = new ArrayList<>();
    }

    //------------------------------start here photoPopup-------------------------------
    public void stickerPopup() {

        stickerDialog.setContentView(R.layout.sticker_msg_popup);
        ImageView circle_cancel_select_popup = (ImageView) stickerDialog.findViewById(R.id.circle_cancel_select_popup);
        circle_cancel_select_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerDialog.dismiss();
            }
        });

        stickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        stickerDialog.show();

    }

}

