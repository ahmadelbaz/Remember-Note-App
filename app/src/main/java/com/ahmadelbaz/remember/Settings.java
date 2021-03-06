package com.ahmadelbaz.remember;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    TextView welcome_textView;

    LinearLayout setting_layout;

    SharedPreferences prefs;

    boolean NightModeIsOn;

    Switch nightMode_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(R.string.settings);

        welcome_textView = findViewById(R.id.welcome_textView);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get username
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            DatabaseReference UNameRef = mDatabase.child("users").child("username").child(currentUser.getUid() + "");

            ValueEventListener eventUserListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    welcome_textView.setText(getString(R.string.welcome) + " " + dataSnapshot.getValue().toString() + " !");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            UNameRef.addValueEventListener(eventUserListener);
        } else {
            welcome_textView.setText(R.string.welcome_guest);
        }

        setting_layout = findViewById(R.id.setting_layout);
        nightMode_switch = findViewById(R.id.nightMode_switch);

        prefs = this.getSharedPreferences("NightModeKey", Context.MODE_PRIVATE);

        NightModeIsOn = prefs.getBoolean("NightModeIsOn", false);

        if (NightModeIsOn) {
            setting_layout.setBackgroundColor(Color.BLACK);
            nightMode_switch.setTextColor(Color.WHITE);
            welcome_textView.setTextColor(Color.WHITE);
        } else {
            setting_layout.setBackgroundColor(getResources().getColor(R.color.bright_color));
            nightMode_switch.setTextColor(Color.BLACK);
            welcome_textView.setTextColor(Color.BLACK);
        }

        nightMode_switch.setChecked(NightModeIsOn);

        nightMode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                prefs.edit().putBoolean("NightModeIsOn", b).commit();
                if (b) {
                    setting_layout.setBackgroundColor(Color.BLACK);
                    nightMode_switch.setTextColor(Color.WHITE);
                    welcome_textView.setTextColor(Color.WHITE);
                } else {
                    setting_layout.setBackgroundColor(getResources().getColor(R.color.bright_color));
                    nightMode_switch.setTextColor(Color.BLACK);
                    welcome_textView.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Settings.this, ListNotesActivity.class);
        startActivity(intent);
        finish();
    }
}