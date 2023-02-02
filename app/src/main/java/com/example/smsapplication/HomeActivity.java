package com.example.smsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        findViewById(R.id.bt_sign).setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
        });

        findViewById(R.id.bt_login).setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(this,"Lütfen Bekleyiniz", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
        }





    }
}