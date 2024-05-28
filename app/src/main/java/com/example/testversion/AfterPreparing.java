package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AfterPreparing extends AppCompatActivity {
    private Button btnShowBill, btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_preparing);
        btnLogOut=findViewById(R.id.logOut);
        btnShowBill=findViewById(R.id.showbill);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(AfterPreparing.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnShowBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AfterPreparing.this, NewBill.class);
                startActivity(intent);
                finish();
            }
        });
    }
}