package whatsapp.whtools.statusdownloader.activities;

import android.net.Uri;
import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import whatsapp.whtools.statusdownloader.R;
import whatsapp.whtools.statusdownloader.identities.StickerPacksContainer;
import whatsapp.whtools.statusdownloader.utils.StickerPacksManager;


public class AddToStickerPackActivity extends AppCompatActivity {

    Uri stickerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_sticker_pack);
        this.stickerUri = this.getIntent().getData();
        StickerPacksManager.stickerPacksContainer = new StickerPacksContainer("", "", StickerPacksManager.getStickerPacks(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, stickerUri.getPath(), Toast.LENGTH_LONG).show();
    }
}