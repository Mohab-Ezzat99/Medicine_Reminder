package mrandroid.medicinereminder.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

import mrandroid.medicinereminder.R;
import mrandroid.medicinereminder.databinding.ActivityAddMedicineBinding;
import mrandroid.medicinereminder.databinding.ActivityHomeBinding;
import mrandroid.medicinereminder.util.AlarmReceiver;
import mrandroid.medicinereminder.util.NotificationHelper;
import mrandroid.medicinereminder.util.TimePickerFragment;

public class AddMedicineActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private NotificationHelper notificationHelper;
    private ActivityAddMedicineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notificationHelper = new NotificationHelper(this);
        notificationHelper.checkNotificationPermission(this);

        binding.btnTime.setOnClickListener(view -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "timePicker");
        });

        binding.btnCancelAlarm.setOnClickListener(view -> {
            cancelAlarm();
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        binding.tvTimeValue.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
        startAlarm(calendar);
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1); // if less then next day
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }
}