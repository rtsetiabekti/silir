package com.kahl.silir.main.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kahl.silir.R;
import com.kahl.silir.entity.MeasurementProfile;
import com.kahl.silir.entity.User;
import com.kahl.silir.main.MainActivity;

import java.util.ArrayList;

public class ChooseProfileActivity extends AppCompatActivity {
    private ArrayList<MeasurementProfile> profilesDetail = new ArrayList<>();
    private ArrayList<String> profilesName = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private Activity activity = this;

    public static final String CHOSEN_PROFILE = "chosenProfile";
    public static final String FROM_HERE = "fromChooserActivity";

    private Spinner profileChooser;
    private Button nextButton;
    private Button newProfileButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_choose_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.choose_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        profileChooser = (Spinner) findViewById(R.id.profile_chooser);
        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NewMeasurementActivity.class);
                intent.putExtra(CHOSEN_PROFILE, profilesDetail.get(profileChooser.getSelectedItemPosition()));
                startActivity(intent);
            }
        });
        newProfileButton = (Button) findViewById(R.id.create_new_profile);
        newProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddNewProfileActivity.class);
                intent.putExtra(FROM_HERE, true);
                startActivity(intent);
            }
        });
        YoYo.with(Techniques.FadeOut).duration(10).playOn(findViewById(R.id.label));
        YoYo.with(Techniques.FadeOut).duration(10).playOn(profileChooser);
        YoYo.with(Techniques.FadeOut).duration(10).playOn(findViewById(R.id.label2));
        YoYo.with(Techniques.FadeOut).duration(10).playOn(newProfileButton);
        YoYo.with(Techniques.FadeOut).duration(10).playOn(nextButton);
        profileChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                    selectedText.setTypeface(Typeface.DEFAULT_BOLD);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.retrieving_mesurement_profiles));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        dbReference.child("users").child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        profilesName.add(user.getName());
                        profilesDetail.add(new MeasurementProfile(user));
                        dbReference.child("profiles").child(auth.getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                MeasurementProfile tempProfile = child.getValue(MeasurementProfile.class);
                                                profilesDetail.add(tempProfile);
                                                profilesName.add(tempProfile.getName());
                                            }
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                                activity, R.layout.profile_chooser_item, profilesName
                                        );
                                        profileChooser.setAdapter(adapter);
                                        profileChooser.setSelection(0);
                                        progressDialog.dismiss();
                                        YoYo.with(Techniques.FadeInUp).playOn(findViewById(R.id.label));
                                        YoYo.with(Techniques.FadeInUp).delay(100).playOn(profileChooser);
                                        YoYo.with(Techniques.FadeInUp).delay(200).playOn(findViewById(R.id.label2));
                                        YoYo.with(Techniques.FadeInUp).delay(300).playOn(newProfileButton);
                                        YoYo.with(Techniques.FadeInDown).playOn(nextButton);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(activity, "Failed to retrieve data from database.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(activity, "Failed to retrieve data from database.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (progressDialog.isShowing()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
        }
    }
}
