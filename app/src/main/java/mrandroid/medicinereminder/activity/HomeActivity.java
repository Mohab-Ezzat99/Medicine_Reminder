package mrandroid.medicinereminder.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import mrandroid.medicinereminder.adapter.MedicineAdapter;
import mrandroid.medicinereminder.databinding.ActivityHomeBinding;
import mrandroid.medicinereminder.model.MedicineModel;

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
        binding.fabAdd.setOnClickListener(view -> {
            startActivity(new Intent(getBaseContext(), AddMedicineActivity.class));
        });
    }

    @Override
    public void onItemDelete(MedicineModel model) {

    }

    @Override
    public void onItemCall(MedicineModel model) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + model.getPhone()));
        startActivity(intent);
    }
}