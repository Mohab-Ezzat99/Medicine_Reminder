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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

import mrandroid.medicinereminder.R;
import mrandroid.medicinereminder.databinding.ActivityAddMedicineBinding;
import mrandroid.medicinereminder.databinding.ActivityHomeBinding;
import mrandroid.medicinereminder.model.MedicineModel;
import mrandroid.medicinereminder.util.AlarmReceiver;
import mrandroid.medicinereminder.util.LoadingDialog;
import mrandroid.medicinereminder.util.NotificationHelper;
import mrandroid.medicinereminder.util.TimePickerFragment;

public class AddMedicineActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private ActivityAddMedicineBinding binding;
    private LoadingDialog loadingDialog;
    private MedicineModel medicineModel;
    private NotificationHelper notificationHelper;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMedicineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = new LoadingDialog(this);
        medicineModel = new MedicineModel();

        notificationHelper = new NotificationHelper(this);
        notificationHelper.checkNotificationPermission(this);

        binding.btnSelectTime.setOnClickListener(view -> {
            DialogFragment timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "timePicker");
        });

        binding.btnSave.setOnClickListener(view -> {
            boolean isValid = checkValidation();
            if (isValid) saveMedicine();
        });
    }

    private boolean checkValidation() {
        String name = binding.etName.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String phone = binding.etDescription.getText().toString().trim();
        String time = binding.tvTimeValue.getText().toString().trim();

        boolean isEmpty = name.isEmpty() || description.isEmpty() || phone.isEmpty() || time.isEmpty();
        if (isEmpty) {
            Toast.makeText(this, "Data is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All is fine
        return true;
    }

    private void saveMedicine() {
        // get values from screen
        String name = binding.etName.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();

        // set server
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference serverRef = FirebaseDatabase.getInstance().getReference().child(userId);

        // set values
        medicineModel.setId(serverRef.push().getKey());
        medicineModel.setName(name);
        medicineModel.setDescription(description);
        medicineModel.setPhone(phone);
        medicineModel.setRequest(new Random().nextInt());

        // save values to server
        loadingDialog.display();
        serverRef.child(medicineModel.getId()).setValue(medicineModel).addOnCompleteListener(task -> {
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        startAlarm(calendar);
                        finish();
                    } else {
                        // Handle the error
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        binding.tvTimeValue.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
    }

    private void startAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Name",medicineModel.getName());
        intent.putExtra("Description",medicineModel.getDescription());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, medicineModel.getRequest(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1); // if less then next day
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}