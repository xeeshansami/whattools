package download.whatstatus.savestatus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import download.whatstatus.savestatus.Adapters.StatusAdapter;
import download.whatstatus.savestatus.Model.StatusModel;

import download.whatstatus.savestatus.R;

import java.util.ArrayList;

public class DownloadStatusActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_status);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);




        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        setView();
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

    private void setView() {
        ArrayList<StatusModel> albums= new ArrayList<>();
        albums.add(new StatusModel(R.drawable.splash));
        albums.add(new StatusModel(R.drawable.splash));
        albums.add(new StatusModel(R.drawable.splash));
        albums.add(new StatusModel(R.drawable.splash));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new StatusAdapter(albums,getApplicationContext()));


    }
}
