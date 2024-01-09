package mrandroid.medicinereminder.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mrandroid.medicinereminder.adapter.MedicineAdapter;
import mrandroid.medicinereminder.databinding.ActivityHomeBinding;
import mrandroid.medicinereminder.model.MedicineModel;
import mrandroid.medicinereminder.util.AlarmReceiver;

public class HomeActivity extends AppCompatActivity implements MedicineAdapter.OnItemClickListener {

    private ActivityHomeBinding binding;
    private MedicineAdapter medicineAdapter = new MedicineAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        medicineAdapter.setListener(this);
        binding.rvMedicines.setAdapter(medicineAdapter);
        getAllMedicines();

        binding.fabAdd.setOnClickListener(view -> {
            startActivity(new Intent(getBaseContext(), AddMedicineActivity.class));
        });
    }

    private void getAllMedicines() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference serverRef = FirebaseDatabase.getInstance().getReference().child(userId);

        serverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<MedicineModel> allMedicines = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    MedicineModel medicineModel = childSnapshot.getValue(MedicineModel.class);
                    allMedicines.add(medicineModel);
                }
                medicineAdapter.setList(allMedicines);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
                Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemDelete(MedicineModel model) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference serverRef = FirebaseDatabase.getInstance().getReference().child(userId);

        serverRef.child(model.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cancelAlarm(model);
            } else {
                // Handle the error
                Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemCall(MedicineModel model) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + model.getPhone()));
        startActivity(intent);
    }

    private void cancelAlarm(MedicineModel medicineModel) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, medicineModel.getRequest(), intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }
}