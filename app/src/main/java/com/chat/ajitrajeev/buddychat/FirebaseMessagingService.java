package com.chat.ajitrajeev.buddychat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ajit on 14/10/17.
 */

public class FirebaseMessagingService  extends
        com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String Notification_title = remoteMessage.getNotification().getTitle();
        String Notiifcation_body = remoteMessage.getNotification().getBody();
        String click_action = remoteMessage.getNotification().getClickAction();
        String from_user_id = remoteMessage.getData().get("from_user_id");

        int mNotificationId = (int)System.currentTimeMillis();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.noti)
                        .setContentTitle("New Friend Request")
                        .setContentText("You have a new Friend Request");
        Intent resultIntent =new Intent(click_action);
        resultIntent.putExtra("user_id",from_user_id);

        PendingIntent pendingIntent =PendingIntent.getActivity(getApplicationContext(),0,
                resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);


        // Sets an ID for the notification

// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
}
