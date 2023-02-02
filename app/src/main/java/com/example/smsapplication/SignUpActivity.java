package com.example.smsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText et_Mail;
    EditText et_Password;
    Button bt_signUp;
    Button bt_reLogin;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bt_reLogin = findViewById(R.id.bt_newlogin);
        bt_reLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        et_Mail = findViewById(R.id.signup_mail);
        et_Password = findViewById(R.id.signup_password);
        bt_signUp = findViewById(R.id.bt_newsignup);

        mAuth = FirebaseAuth.getInstance();

        bt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_Mail.getText().toString();
                String password = et_Password.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Lütfen tüm boş alanları doldurun", Toast.LENGTH_SHORT).show();
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Kayıt Başarıyla Gerçekleşti", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(SignUpActivity.this, "Kayıt Başarısız Oldu", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}