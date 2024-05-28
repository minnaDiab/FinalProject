package com.example.testversion;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ClientsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout etPhone;
    private Button btnShow,btnNew;
    Firebase fbUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);
        initViews();
        btnShow.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Signing out...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();

                fbUser.userSignout();
                startActivity(new Intent(ClientsActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void initViews() {
        etPhone=findViewById(R.id.etPhone);
        btnNew=findViewById(R.id.btnNewClient);
        btnShow=findViewById(R.id.btnShowclient);
        fbUser=new Firebase();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        String clientPhone=etPhone.getEditText().getText().toString();

        if(view.getId()==R.id.btnShowclient){
            if(!clientPhone.isEmpty()) {
                intent.putExtra("Phone", clientPhone);
                startActivity(intent);
            }
            else{
                etPhone.setError("must fillin phone number");
                return;
            }
        }
        else {
            intent.putExtra("Phone","");
            startActivity(intent);
        }
    }
}