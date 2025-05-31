package com.example.budgetmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    // Called when the login button is clicked
    public void onLoginClicked(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if ("admin".equals(username) && "password".equals(password)) {
            // Successful login, navigate to MainActivity
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Create an Intent to navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();  // Close LoginActivity so the user can't return to it by pressing back
        } else {
            // Invalid credentials
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
