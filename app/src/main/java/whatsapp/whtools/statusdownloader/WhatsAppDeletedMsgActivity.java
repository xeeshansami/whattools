package whatsapp.whtools.statusdownloader;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import whatsapp.whtools.statusdownloader.Adapters.MessageAdapter;
import whatsapp.whtools.statusdownloader.DBHelper.Database;

import whatsapp.whtools.statusdownloader.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
@Keep
public class WhatsAppDeletedMsgActivity extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    RecyclerView missedcalled_recyler;
    ArrayList<Message> missedCallList;
    MessageAdapter callLogsRecyclerAdapter;
//    private TextView tvMsg;
    private ReceiveBroadcastReceiver imageChangeBroadcastReceiver;
    private AlertDialog enableNotificationListenerAlertDialog;
    Database database;
    Cursor c;
    TextView noDataText;
    ImageView back_btn;
    AdView mAdView;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_deleted_msg);

//        IntersitialAds();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        noDataText = findViewById(R.id.noDataText);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        database=new Database(this);

        missedCallList = new ArrayList<>();
        missedcalled_recyler = findViewById(R.id.recyclerView);
        missedcalled_recyler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        c = database.getAllNumber();
        try {
            if (c.getCount() != 0) {
                if (c.moveToFirst()) {
                    do {
                        String title = c.getString(1);
                        String date = c.getString(2);
                        String sms = c.getString(3);
                        Message callLogsModel = new Message();
                        callLogsModel.setTitle(title);
                        callLogsModel.setDate(date);
                        callLogsModel.setSms(sms);
                        missedCallList.add(callLogsModel);
                    } while (c.moveToNext());
                    callLogsRecyclerAdapter = new MessageAdapter(getApplicationContext(), missedCallList);
                    missedcalled_recyler.setAdapter(callLogsRecyclerAdapter);
                    callLogsRecyclerAdapter.notifyDataSetChanged();
                }
                c.close();
            }else
            {
                noDataText.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){}

        if (!isNotificationServiceEnabled()) {
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received
        imageChangeBroadcastReceiver = new ReceiveBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("whatsapp.whtools.statusdownloader");
        registerReceiver(imageChangeBroadcastReceiver, intentFilter);

    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Receive Broadcast Receiver.
     */
    @Keep
    public class ReceiveBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int receivedNotificationCode = intent.getIntExtra("Notification Code", -1);
            String packages = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");

            if (text != null) {

                if (!text.contains("new messages") && !text.contains("WhatsApp Web is currently active") && !text.contains("WhatsApp Web login")) {

                    String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String devicemodel = android.os.Build.MANUFACTURER + android.os.Build.MODEL + android.os.Build.BRAND + android.os.Build.SERIAL;

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String date = df.format(Calendar.getInstance().getTime());

//                    tvMsg.setText("Notification : " + receivedNotificationCode + "\nPackages : " + packages + "\nTitle : " + title + "\nText : " + text + "\nId : " + date + "\nandroid_id : " + android_id + "\ndevicemodel : " + devicemodel);
                    /**
                     Log.d("DetailsEzraatext2 :", "Notification : " + receivedNotificationCode + "\nPackages : " + packages + "\nTitle : " + title + "\nText : " + text + "\nId : " + date+ "\nandroid_id : " + android_id+ "\ndevicemodel : " + devicemodel);
                     */
//                    Toast.makeText(context, ""+date, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * Build Notification Listener Alert Dialog.
     */
    private AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return (alertDialogBuilder.create());
    }


    private boolean isAccessibilityOn(Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();

                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /*public void IntersitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(WhatsAppDeletedMsgActivity.this);
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
