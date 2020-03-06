package com.example.employefy.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.employefy.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String CHANNEL_ID="ID";
    private static final String CHANNEL_NAME="NAME";
    private static final String CHANNEL_DESC="DESC";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESC);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        try
        {
            JSONObject jsonObject =new JSONObject(remoteMessage.getData());
            JSONObject jsonObject1=jsonObject.getJSONObject("data");
            String message=jsonObject1.getString("title");
            showNotification("EmployeFy",message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void showNotification(String heading,String message)
    {
        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.icon1)
                .setContentTitle(heading)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(1,nBuilder.build());
    }


}
