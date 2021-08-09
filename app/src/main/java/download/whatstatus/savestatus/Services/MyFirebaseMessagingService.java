package download.whatstatus.savestatus.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import download.whatstatus.savestatus.MainActivity;
import download.whatstatus.savestatus.Model.Mysingleton;
import download.whatstatus.savestatus.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    RemoteViews remoteViews;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title, msg, imageuri=null;


        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("title");
            msg = remoteMessage.getData().get("msg");
            imageuri = remoteMessage.getData().get("image");

            //   if(imageuri.contains(".jpeg")||imageuri.contains(".png")||imageuri.contains(".jpg"))

           if(imageuri.contains(""))
           {shownotification(title,msg);
               System.out.println("if wala imageuri "+imageuri);

           }

           if (imageuri.contains("https"))
           {
               System.out.println("else wala imageuri "+imageuri);

               shownotificationwithImage(title,msg,imageuri);

           }



        }
    }




private void shownotificationwithImage(String title,String msg,String imageuri)
{
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

    PendingIntent pendIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"WhatStatus");
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.statusdowloder)
                .setContentIntent(pendIntent).build();
/*
        remoteViews=new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notify_icon,R.mipmap.statusdowloder);
        remoteViews.setImageViewResource(R.id.big_notifyicon,R.mipmap.statusdowloder);
        remoteViews.setTextViewText(R.id.title_notify,title);
        remoteViews.setTextViewText(R.id.msg_notify,msg);

        builder.setCustomBigContentView(remoteViews);
                builder.setSmallIcon(R.mipmap.statusdowloder);
                        builder.setAutoCancel(true);
                        builder.setContentIntent(pendIntent);
*/


        ImageRequest imageRequest = new ImageRequest(imageuri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,builder.build());
            }
        }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Mysingleton.getInstance(this).addtorequestqueue(imageRequest);

    }

    else {

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"WhatStatus");
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.statusdowloder)
        .setContentIntent(pendIntent);

/*
        remoteViews=new RemoteViews(getPackageName(),R.layout.custom_notification);
        remoteViews.setImageViewResource(R.id.notify_icon,R.mipmap.statusdowloder);
        remoteViews.setImageViewResource(R.id.big_notifyicon,R.mipmap.statusdowloder);
        remoteViews.setTextViewText(R.id.title_notify,title);
        remoteViews.setTextViewText(R.id.msg_notify,msg);
        builder.setCustomBigContentView(remoteViews);
                builder.setSmallIcon(R.mipmap.statusdowloder);
                        builder.setAutoCancel(true);
                        builder.setContentIntent(pendIntent);

*/

        ImageRequest imageRequest = new ImageRequest(imageuri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));

                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MyFirebaseMessagingService.this);
                managerCompat.notify(999,builder.build());

            }
        }, 0, 0, null,Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Mysingleton.getInstance(this).addtorequestqueue(imageRequest);

    }


}

    public void shownotification(String titile, String messages)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"WhatStatus");

        /*    remoteViews=new RemoteViews(getPackageName(),R.layout.custom_notification);
            remoteViews.setImageViewResource(R.id.notify_icon,R.mipmap.statusdowloder);
            remoteViews.setImageViewResource(R.id.big_notifyicon,R.mipmap.statusdowloder);
            remoteViews.setTextViewText(R.id.title_notify,titile);
            remoteViews.setTextViewText(R.id.msg_notify,messages);
*/
         //   builder.setCustomBigContentView(remoteViews).setSmallIcon(R.mipmap.statusdowloder).setAutoCancel(true).setContentIntent(pendIntent);
            builder.setContentTitle(titile);
            builder.setContentText(messages);
            builder.setSmallIcon(R.mipmap.statusdowloder)
                    .setContentIntent(pendIntent);
            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,builder.build());
        }
        else
        {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "WhatStatus");
            builder.setContentTitle(titile);
            builder.setContentText(messages);
            builder.setSmallIcon(R.mipmap.statusdowloder)
            .setContentIntent(pendIntent);

          /*  NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder.build());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "WhatStatus");

            remoteViews=new RemoteViews(getPackageName(),R.layout.custom_notification);
            remoteViews.setImageViewResource(R.id.notify_icon,R.mipmap.statusdowloder);
            remoteViews.setImageViewResource(R.id.big_notifyicon,R.mipmap.statusdowloder);
            remoteViews.setTextViewText(R.id.title_notify,titile);
            remoteViews.setTextViewText(R.id.msg_notify,messages);

            builder.setCustomBigContentView(remoteViews).setSmallIcon(R.mipmap.statusdowloder).setAutoCancel(true).setContentIntent(pendIntent);
*/

            NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder.build());


        }
    }
}











