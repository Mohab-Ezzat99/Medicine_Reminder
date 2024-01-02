package mrandroid.medicinereminder.activity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import mrandroid.medicinereminder.databinding.ActivityRegisterBinding;
import mrandroid.medicinereminder.util.LoadingDialog;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadingDialog = new LoadingDialog(this);

        binding.btnRegister.setOnClickListener(view -> {
            boolean isValid = checkValidation();
            if (isValid) registerUser();
        });

        binding.tvLogin.setOnClickListener(view -> {
            finish();
        });
    }

    private boolean checkValidation() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfPassword.getText().toString().trim();

        boolean isEmpty = email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty();
        if (isEmpty) {
            Toast.makeText(this, "Data is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password not matches!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All is fine
        return true;
    }

    private void registerUser() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        loadingDialog.display();
        auth.fetchSignInMethodsForEmail(email) // check if exist (success)
                .addOnSuccessListener(signInMethodQueryResult -> createNewAccount(email, password))
                .addOnFailureListener(e -> {
                    loadingDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Error! " + e.getCause(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createNewAccount(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        loadingDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Signup successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Error! " + task1.getException(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}