package whatsapp.whtools.statusdownloader;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    private static MyApplication singleton;

    public MyApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.app_id));

    }
}
