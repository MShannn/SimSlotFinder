package com.muhammadshan.simslotsfinder.BroadcastReceiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.muhammadshan.simslotsfinder.MainActivity;
import com.muhammadshan.simslotsfinder.R;

/**
 * Created by Shan on 7/19/2017.
 */

public class SimSlotReceiver extends BroadcastReceiver {
    NotificationManager NM;
    Context con;
    String simName;
    String sim;
    @Override
    public void onReceive(Context context, Intent intent) {
        String callingSIM = "";


        con = context;
        Bundle bundle = intent.getExtras();
        int slot = Integer.parseInt((String) intent.getExtras().get("slot"));

        Toast.makeText(context, "slot checking"+slot, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "Long"+capturedSimSlot(bundle), Toast.LENGTH_LONG).show();

        callingSIM = String.valueOf(bundle.getInt("simId", -1));
        if (callingSIM == "0") {
            simName = "SIM 1";
        } else if (callingSIM == "1") {
            simName = "SIM 2";
            Toast.makeText(context, "Sim two", Toast.LENGTH_SHORT).show();
        } else if (callingSIM == "2") {
            simName = "SIM 3";
        }
        createNotification();
    }

    public void createNotification() {

        Intent intent = new Intent(con, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(con, 0, intent,
                0);

        // Build notification
        // Actions are just fake
        Notification noti = new NotificationCompat.Builder(con)
                .setContentTitle("New Call from " + simName)

                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.sim_card).setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        NotificationManager notificationManager = (NotificationManager) con.getSystemService(Activity.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);



       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(con);
        builder.setSmallIcon(R.drawable.sim_card)

                .setContentTitle(simName)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);*/



    }


    public int capturedSimSlot(Bundle bundle){

        int whichSIM =-1;
        if (bundle.containsKey("subscription")) {
            whichSIM = bundle.getInt("subscription");
        }
        if(whichSIM >=0 & whichSIM < 5){
            /*In some device Subscription id is return as subscriber id*/
             sim = "" + whichSIM;
        }else{
            if (bundle.containsKey("simId")) {
                whichSIM = bundle.getInt("simId");
            }else if (bundle.containsKey("com.android.phone.extra.slot")) {
                whichSIM = bundle.getInt("com.android.phone.extra.slot");
            }else{
                String keyName = "";
                for(String key : bundle.keySet()){
                    if(key.contains("sim"))
                        keyName =key;
                }
                if (bundle.containsKey(keyName)) {
                    whichSIM = bundle.getInt(keyName);
                }
            }
        }
        return whichSIM;
    }

}