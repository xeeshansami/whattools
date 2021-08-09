package download.whatstatus.savestatus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import download.whatstatus.savestatus.Adapters.AllVideoSongsAdapter;
import download.whatstatus.savestatus.Adapters.ViewPagerAdapter;
import download.whatstatus.savestatus.Fragment.WhatsappFragmentImages;
import download.whatstatus.savestatus.Fragment.WhatsappFragmentVideos;
import download.whatstatus.savestatus.Model.VideoSongs;

import download.whatstatus.savestatus.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WhatsappStatusActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AllVideoSongsAdapter videoSongsAdapter;
    ArrayList<VideoSongs> videoSongsList = new ArrayList<VideoSongs>();
    public static List<String> list2 = null;
    public int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    ProgressDialog progressDialog;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    WhatsappFragmentImages imagesFrafments;
    WhatsappFragmentVideos videosFragments;

    AdView mAdView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whatsapp_status_activity);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        imagesFrafments = new WhatsappFragmentImages();
        videosFragments = new WhatsappFragmentVideos();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        addTabs(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.backicon));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        loadStatusVideoAndImages();
       /* MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
        mAdView = (AdView) findViewById(R.id.adView_banner);
        adRequest = new AdRequest.Builder()
                .addTestDevice("5505CD905B33F3EF3FF56B95BE08D014")
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });*/
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new WhatsappFragmentVideos(), "Videos");
        adapter.addFrag(new WhatsappFragmentImages(), "Images");
        viewPager.setAdapter(adapter);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.videofolder_menu, menu);
        return true;
    }*/

    @Keep
    public void loadStatusVideoAndImages() {
        AppClass.folder.clear();

        Iterator it = AppClass.dirList.entrySet().iterator();

        System.out.println("if condition true video " + AppClass.dirList.size());

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            System.out.println("if condition true video2 " + pairs.getValue().toString() + " " + pairs.getKey().toString());
            if (pairs.getValue().toString().contains(".Statuses")) {
                FolderEntity entity = new FolderEntity();
                entity.setName(pairs.getValue().toString());
                entity.setPath(pairs.getKey().toString());
                AppClass.folder.add(entity);
            }
        }
        // Collections.sort(StorageUtil.folder,null);
        Collections.sort(AppClass.folder, new Comparator<FolderEntity>() {
            public int compare(FolderEntity obj1, FolderEntity obj2) {
                // ## Ascending order
                return obj1.name.compareToIgnoreCase(obj2.name); // To compare string values
                // return Integer.valueOf(obj1.empId).compareTo(obj2.empId); // To compare integer values

                // ## Descending order
                // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                // return Integer.valueOf(obj2.empId).compareTo(obj1.empId); // To compare integer values
            }
        });
//        folderAdapter = new FolderAdapter(this, AppClass.folder);
//        foldersrecycler.setAdapter(folderAdapter);
//        folderAdapter.notifyDataSetChanged();
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_whatsapp_refresh:
                try {
                    startActivity(new Intent(this, WhatsappStatusActivity.class));
                    finish();
//                    loadStatusVideoAndImages();
//                    StatusFolder statusFolder = new StatusFolder();
//                    statusFolder.new GetStatusAllData().execute();
                } catch (OutOfMemoryError e) {
                    Log.i("outOfMemoryError", e.getMessage());
                } catch (Exception e) {
                }
        }
        return true;
    }
*/

}
