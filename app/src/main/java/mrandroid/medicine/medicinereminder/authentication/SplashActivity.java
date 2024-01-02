package mrandroid.medicine.medicinereminder.authentication;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import mrandroid.medicine.medicinereminder.R;
import mrandroid.medicine.medicinereminder.util.NotificationHelper;
import mrandroid.medicine.medicinereminder.util.TimePickerFragment;

public class SplashActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private NotificationHelper notificationHelper;
    private Button btn_notification;
    private Button btn_time;
    private TextView tvTimeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        notificationHelper = new NotificationHelper(this);
        notificationHelper.checkNotificationPermission(this);

        btn_notification = findViewById(R.id.btn_notification);
        btn_time = findViewById(R.id.btn_time);
        tvTimeValue = findViewById(R.id.tvTimeValue);

        btn_notification.setOnClickListener(view -> notificationHelper.displayNotification("My title", "test body"));
        btn_time.setOnClickListener(view -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "timePicker");
        });

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        tvTimeValue.setText(hour + ":" + minute);
    }
}