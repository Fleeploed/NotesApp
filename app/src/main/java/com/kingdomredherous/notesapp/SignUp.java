package com.kingdomredherous.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kingdomredherous.notesapp.ApiFolder.API;
import com.kingdomredherous.notesapp.ApiFolder.APIClient;
import com.kingdomredherous.notesapp.ApiFolder.Note;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    EditText et_email, et_login, et_jmeno, et_heslo, et_heslo_znovu;
    Button buttonSignUp;
    TextView loginText;
    ProgressBar progressBar;
    API api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_jmeno = findViewById(R.id.fullname);
        et_login = findViewById(R.id.loginCreate);
        et_email = findViewById(R.id.email);
        et_heslo = findViewById(R.id.passwordCreate);
        et_heslo_znovu = findViewById(R.id.passwordAgain);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        loginText = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0, 0);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = et_login.getText().toString().trim();
                String jmeno = et_jmeno.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String heslo = et_heslo.getText().toString().trim();
                String heslo_znovu = et_heslo_znovu.getText().toString().trim();
                if (login.isEmpty()) {
                    Toast.makeText(SignUp.this, "Zadejte prosim login!", Toast.LENGTH_SHORT).show();
                } else if (heslo.isEmpty()) {
                    Toast.makeText(SignUp.this, "Zadejte prosim heslo!", Toast.LENGTH_SHORT).show();
                } else if (jmeno.isEmpty()) {
                    Toast.makeText(SignUp.this, "Zadejte prosim jmeno!", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(SignUp.this, "Zadejte prosim email!", Toast.LENGTH_SHORT).show();
                } else if (!heslo.equals(heslo_znovu)) {
                    Toast.makeText(SignUp.this, "Zadejte prosim spravnou heslo!", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(login, jmeno, email, heslo);
                }
            }
        });

    }

    private void signUp(final String login, final String jmeno, final String email, final String heslo) {
        progressBar.setVisibility(View.VISIBLE);
        api = APIClient.getClient().create(API.class);
        Call<Note> call = api.signUp(jmeno, login, email, heslo);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUp.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}