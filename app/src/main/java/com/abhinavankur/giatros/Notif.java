package com.abhinavankur.giatros;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Abhinav Ankur on 5/13/2016.
 */
public class Notif {

    String id, user;
    NotificationCompat.Builder notification;
    Context context;
    NotificationsFinder finder;

    public Notif(String user, String id, Context context) {
        this.user = user;
        this.id = id;
        this.context = context;
        /*if (notifInterface instanceof SymptomsActivity){
            this.notifInterface = (SymptomsActivity) notifInterface;
        }*/
        notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
    }

    public void find(){
        finder = new NotificationsFinder(id, user, this);
        finder.execute();
    }
    public void setDoctor(String appointmentsCount, String symptomsCount){
        if (!symptomsCount.equals("0")){
            notification.setSmallIcon(android.R.drawable.stat_notify_chat);
            notification.setTicker("Click to map symptoms");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Symptoms");
            notification.setContentText("You have "+ symptomsCount + " symptoms to map.");

        /*Whenever it is clicked, MainActivity is called.*/
            Intent intent = new Intent(context, NewSymptomsListActivity.class);
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingintent);

        /*Builds notification and issuing it.*/
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(12345, notification.build());
        }

        if (!appointmentsCount.equals("0")){
            notification.setSmallIcon(android.R.drawable.stat_notify_chat);
            notification.setTicker("Click to view appointments");
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle("Appointments");
            notification.setContentText("You have "+ appointmentsCount + " appointments to attend to.");

        /*Whenever it is clicked, MainActivity is called.*/
            Intent intent = new Intent(context, AppointmentActivity.class);
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingintent);

        /*Builds notification and issuing it.*/
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(12346, notification.build());
        }
    }

    public void setPatient(String symptom, int uniqueId){
        notification.setSmallIcon(android.R.drawable.stat_notify_sdcard);
        notification.setTicker("Symptom "+ symptom + " associated to a disease.");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(symptom);
        notification.setContentText("Click to find diseases now!");

        /*Whenever it is clicked, MainActivity is called.*/
        Intent intent = new Intent(context, SymptomsActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingintent);

        /*Builds notification and issuing it.*/
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueId, notification.build());
    }
}
