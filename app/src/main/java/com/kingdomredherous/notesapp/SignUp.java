package com.kingdomredherous.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFullname, textInputEditTextCreateLogin, textInputEditTextEmail,
            textInputEditTextPassword, textInputEditTextPasswordAgain;
    Button buttonSignUp;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextFullname = findViewById(R.id.fullname);
        textInputEditTextCreateLogin = findViewById(R.id.loginCreate);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextPassword = findViewById(R.id.passwordCreate);
        textInputEditTextPasswordAgain = findViewById(R.id.passwordAgain);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        loginText = findViewById(R.id.loginText);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(intent, 0);
                overridePendingTransition(0,0);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String fullname, login, email, password, passwordAgain;
                fullname = String.valueOf(textInputEditTextFullname.getText());
                login = String.valueOf(textInputEditTextCreateLogin.getText());
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                passwordAgain = String.valueOf(textInputEditTextPasswordAgain.getText());
                final ProgressBar progressBar = findViewById(R.id.progress);


                if (!fullname.equals("") && !login.equals("") && !email.equals("")
                        && !password.equals("") && password.equals(passwordAgain)) {
                    progressBar.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "jmeno";
                            field[1] = "login";
                            field[2] = "email";
                            field[3] = "heslo";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = fullname;
                            data[1] = login;
                            data[2] = email;
                            data[3] = password;
                            PutData putData = new PutData("https://alisheribrayev.000webhostapp.com/NotesApp/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    System.out.println(result);
                                    if (result.equals("Super! Muzete se prihlasit!")) {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
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