package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile_activity extends AppCompatActivity {

    public static final int IMAGE_CODE = 1;
    CircleImageView circleImage;
    ImageView imageView, logoutbutton;
    Uri imageuri;
    SharedPreferences passwordsharedPreferences, sharedPreferences;
    Intent intent;
    TextView tvUserName, tvEmail, tvPhoneNumber;
    ImageView editEmail, editPhoneNumber;
    EditText updateemail, verifytext;
    ImageButton saveUserDataButton, cancelUserDataButton;

    String mUserName, mEmail, mPhoneNumber;

    View popupInputDialogView;
    //firebasefirestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String new_email, saved_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        sharedPreferences = getSharedPreferences("user_number", MODE_PRIVATE);
        passwordsharedPreferences = getSharedPreferences("PASSWORD", MODE_PRIVATE);
        intent = new Intent(profile_activity.this, LoginPage.class);


        //assigning to ids's or implementation of button,textview,editview,imageview etc
        RelativeLayout rr = findViewById(R.id.relativeLayout);
        circleImage = rr.findViewById(R.id.profile);
        imageView = findViewById(R.id.editprofile);
        logoutbutton = findViewById(R.id.logout);
        tvUserName = findViewById(R.id.username);
        tvEmail = findViewById(R.id.email);
        tvPhoneNumber = findViewById(R.id.phonenumber);
        editEmail = findViewById(R.id.edit_email);
        editPhoneNumber = findViewById(R.id.edit_phonenumber);


        //implementation of firebase firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser mFirebaseUser = fAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            userID = mFirebaseUser.getUid(); //Do what you need to do with the id
        }


        //working it on this later
       editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a AlertDialog Builder.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(profile_activity.this);
                //initdailogviewcontrol();
                // Set the inflated layout view object to the AlertDialog builder.
                alertDialogBuilder.setView(popupInputDialogView);

                // Create AlertDialog and show.
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                saveUserDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new_email = updateemail.getText().toString();
                        saved_password = passwordsharedPreferences.getString("PASSWORD", null);

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Get auth credentials from the user for re-authentication

                        AuthCredential re_auth_credential = EmailAuthProvider.getCredential(new_email, saved_password);

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(re_auth_credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                user.updateEmail(new_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //update_email(new_email);
                                            alertDialog.cancel();
                                        } else {
                                            Toast.makeText(getApplicationContext(), " Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                }
                            }
                        });
                    }
                });


                cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                        Toast.makeText(getApplicationContext(), "cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openimageform();
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });

        //initialize the navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.profile);

        //get the item selected
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        startActivity(new Intent(getApplicationContext(), Favdish.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(), cart.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.editlocation:
                        startActivity(new Intent(getApplicationContext(), location.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        ShowPhoneNumber();
    }

    //working on it this later
   /* public void update_email(String newemail) {
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference docRef = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("Email", newemail);
        docRef.set(user, SetOptions.merge());
    }

    private void initdailogviewcontrol() {

        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(profile_activity.this);

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.email_dialogbox, null);

        // Get user input edittext and button ui controls in the popup dialog.

        updateemail = popupInputDialogView.findViewById(R.id.update_email);
        saveUserDataButton = popupInputDialogView.findViewById(R.id.yes_button);
        cancelUserDataButton = popupInputDialogView.findViewById(R.id.cancel_button);

    }*/


    private void ShowPhoneNumber() {
        DocumentReference docRef = fStore.collection("users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mUserName = documentSnapshot.getString("Username");
                    mEmail = documentSnapshot.getString("Email");
                    mPhoneNumber = fAuth.getCurrentUser().getPhoneNumber();


                    tvPhoneNumber.setText(mPhoneNumber);
                    tvUserName.setText(mUserName);
                    tvEmail.setText(mEmail);

                } else {
                    Toast.makeText(getApplicationContext(), "Error! showing details..", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void openimageform() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {

            imageuri = data.getData();
            circleImage.setImageURI(imageuri);
        }
    }
}
