package com.kahl.silir.main.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kahl.silir.R;
import com.kahl.silir.adapter.ProfileListAdapter;
import com.kahl.silir.entity.MeasurementProfile;
import com.kahl.silir.entity.User;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;

public class MeasurementProfilesActivity extends AppCompatActivity {
    public static final String PROFILE_EXTRA = "profil_extra";
    public static final String KEY_EXTRA = "key_extra";

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Activity activity = this;

    private ArrayList<String> keys = new ArrayList<>();
    ArrayList<MeasurementProfile> measurementProfiles = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_profiles);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("retrieving data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        setUpDefaultProfile();
        setUpProfileList();
    }

    private void setUpProfileList() {
        final ListView profileList = (ListView) findViewById(R.id.profile_list);
        FloatingActionButton addNewProfile = (FloatingActionButton) findViewById(R.id.add_new_profile);

        final Drawable addIcon = MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_MULTIPLE_PLUS)
                .setColor(Color.WHITE).setSizeDp(25).build();

        addNewProfile.setImageDrawable(addIcon);

        databaseReference.child("profiles").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                keys.add(data.getKey());
                                MeasurementProfile profile = data.getValue(MeasurementProfile.class);
                                measurementProfiles.add(profile);
                            }
                            MeasurementProfile[] profiles = new MeasurementProfile[measurementProfiles.size()];
                            profiles = measurementProfiles.toArray(profiles);
                            profileList.setAdapter(new ProfileListAdapter(getApplicationContext(),
                                    profiles));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Failed to load data from database.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
        addNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewProfileActivity.class));
            }
        });
        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, EditProfileActivity.class);
                intent.putExtra(PROFILE_EXTRA, measurementProfiles.get(position));
                intent.putExtra(KEY_EXTRA, keys.get(position));
                startActivity(intent);
            }
        });
    }

    private void setUpDefaultProfile() {
        final Drawable male = MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.GENDER_MALE)
                .setColor(getResources().getColor(R.color.colorPrimary)).build();
        final Drawable female = MaterialDrawableBuilder.with(this)
                .setIcon(MaterialDrawableBuilder.IconValue.GENDER_FEMALE)
                .setColor(getResources().getColor(R.color.colorPrimary)).build();

        final TextView name = (TextView) findViewById(R.id.profile_name);
        final TextView weight = (TextView) findViewById(R.id.weight_value);
        final TextView height = (TextView) findViewById(R.id.height_value);
        final TextView age = (TextView) findViewById(R.id.age);
        final MaterialIconView gender = (MaterialIconView) findViewById(R.id.gender_icon);

        databaseReference.child("users").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        name.setText("(Default) " + user.getName());
                        height.setText(String.format("Height: %d cm", user.getHeight()));
                        weight.setText(String.format("Weight: %d kg", user.getWeight()));
                        age.setText(String.format("%d", user.getAge()));
                        gender.setImageDrawable(user.getGender().equals("Male") ? male : female);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),
                                "Unable to load data from cloud.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
