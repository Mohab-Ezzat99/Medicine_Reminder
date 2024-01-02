package mrandroid.medicine.medicinereminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import mrandroid.medicine.medicinereminder.R;
import mrandroid.medicine.medicinereminder.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation anim_left = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_left);
        binding.ivLogo.setAnimation(anim_left);
        binding.tvDesc.setAnimation(anim_left);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
        }, 1800);

    }
}