package whatsapp.whtools.statusdownloader.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import whatsapp.whtools.statusdownloader.R;
import whatsapp.whtools.statusdownloader.utils.FileUtils;
import whatsapp.whtools.statusdownloader.utils.RequestPermissionsHelper;


public class RequestPermissionActivity extends AppCompatActivity {

    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);

        FileUtils.initializeDirectories(this);
        if (RequestPermissionsHelper.verifyPermissions(this)) {
            startActivity(new Intent(this, MainStickers.class));
            finish();
        } else {
            RequestPermissionsHelper.requestPermissions(this);
        }
        findViewById(R.id.grant_permissions_button).setOnClickListener(v -> RequestPermissionsHelper.requestPermissions(this));

        ImageView backicon = findViewById(R.id.back_btn);
        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FileUtils.initializeDirectories(this);
        if (RequestPermissionsHelper.verifyPermissions(this)) {//If the app has all the required permissions we pass to MainStickers to get started
            startActivity(new Intent(this, MainStickers.class));
            finish();
        } else {
            Toast.makeText(this, "We need access to write and read files in your phone", Toast.LENGTH_SHORT).show();
        }
    }
}
