package mrandroid.medicine.medicinereminder.util;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import java.util.Random;

import mrandroid.medicine.medicinereminder.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "123";
    public static final String channelName = "Medicine Channel";
    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(com.google.android.material.R.color.design_default_color_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel1);
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        return notificationManager;
    }

    public void checkNotificationPermission(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                activity.requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    public void displayNotification(String title, String message) {
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_alarm)
                .setChannelId(channelID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build();

        int id = new Random().nextInt();
        getNotificationManager().notify(id, notification);
    }
}
