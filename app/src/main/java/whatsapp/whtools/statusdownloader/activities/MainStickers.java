package whatsapp.whtools.statusdownloader.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import whatsapp.whtools.statusdownloader.R;
import whatsapp.whtools.statusdownloader.backgroundRemover.CutOut;
import whatsapp.whtools.statusdownloader.identities.StickerPacksContainer;
import whatsapp.whtools.statusdownloader.utils.StickerPacksManager;
import whatsapp.whtools.statusdownloader.whatsapp_api.AddStickerPackActivity;
import com.sangcomz.fishbun.define.Define;


import java.util.Objects;


public class MainStickers extends AddStickerPackActivity {
    private MyStickersFragment myStickersFragment;
    private ExploreFragment exploreFragment;
    private CreateFragment createFragment;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_stickers);

        /*AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

//        IntersitialAds();

        Fresco.initialize(this);
//        this.initBottomNavigation();
        this.setupFragments();
        ImageView  backicon = findViewById(R.id.back_btn);

        backicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setFragmento(myStickersFragment);
        StickerPacksManager.stickerPacksContainer = new StickerPacksContainer("", "", StickerPacksManager.getStickerPacks(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_home:
                    setFragmento(myStickersFragment);
                    return true;
                /*case R.id.menu_explore:
                    setFragmento(exploreFragment);
                    return true;*/
              /*  case R.id.menu_create:
                    setFragmento(createFragment);
                    return true;*/
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CutOut.CUTOUT_ACTIVITY_REQUEST_CODE || requestCode == Define.ALBUM_REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_principal);
            Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initBottomNavigation() {
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setupFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        myStickersFragment = new MyStickersFragment();
        exploreFragment = new ExploreFragment();
        createFragment = new CreateFragment();
        fragmentTransaction.add(R.id.frame_principal, myStickersFragment);
        fragmentTransaction.add(R.id.frame_principal, exploreFragment);
        fragmentTransaction.add(R.id.frame_principal, createFragment);
        fragmentTransaction.hide(exploreFragment);
        fragmentTransaction.hide(createFragment);
        fragmentTransaction.commit();
    }

    private void setFragmento(Fragment fragmento) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (fragmento == myStickersFragment) {
            fragmentTransaction.hide(exploreFragment);
            fragmentTransaction.hide(createFragment);
        } else if (fragmento == exploreFragment) {
            fragmentTransaction.hide(myStickersFragment);
            fragmentTransaction.hide(createFragment);
        } else if (fragmento == createFragment) {
            fragmentTransaction.hide(myStickersFragment);
            fragmentTransaction.hide(exploreFragment);
        }
        fragmentTransaction.show(fragmento);
        fragmentTransaction.commit();
    }

    /*public void IntersitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainStickers.this);
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
