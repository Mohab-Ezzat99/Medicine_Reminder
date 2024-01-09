package mrandroid.medicinereminder.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("Name");
        String description = intent.getStringExtra("Description");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.displayNotification(name, description);
    }
}
