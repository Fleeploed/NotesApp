package com.kingdomredherous.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {
    TextInputEditText textInputEditTextLogin, textInputEditTextPassword;
    Button buttonSignIn;
    TextView createText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextLogin = findViewById(R.id.login);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonSignIn = findViewById(R.id.btnLogin);
        createText = findViewById(R.id.createText);

        createText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login, password;
                login = String.valueOf(textInputEditTextLogin.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                final ProgressBar progressBar = findViewById(R.id.progressLogin);

                if (!login.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String [] field = new String[2];
                            field[0] = "login";
                            field[1] = "heslo";
                            String[]data =  new String[2];
                            data[0] = login;
                            data[1] = password;
                            PutData putData = new PutData("https://alisheribrayev.000webhostapp.com/NotesApp/signin.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    System.out.println(result);
                                    if (result.equals("Sign up Success!")) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Chyby v pole, zadejte znovu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}