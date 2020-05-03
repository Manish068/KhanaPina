package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity  {

    TextView Backbtn,Savebtn;
    EditText RegUserName,RegEmail,RegPassword;


    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID,email,password;
    SharedPreferences passwordsharedPreferences,sharedPreferences;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();


        Backbtn =  findViewById(R.id.backButton);
        Savebtn =  findViewById(R.id.savebutton);
        RegUserName = findViewById(R.id.registerUserName);
        RegEmail =  findViewById(R.id.registerEmail);
        RegPassword = findViewById(R.id.registerPassword);

        passwordsharedPreferences=getSharedPreferences("PASSWORD",MODE_PRIVATE);
        sharedPreferences=getSharedPreferences("user_number",MODE_PRIVATE);
        intent=new Intent(RegisterPage.this,LoginPage.class);


        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
            }
        });

        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkAccount();
            }
        });

    }

    private void linkAccount() {
        // Get email and password from form
        email = RegEmail.getText().toString();
        password = RegPassword.getText().toString();


        // Create EmailAuthCredential with email and password
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        // [START link_credential]
        fAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            SharedPreferences.Editor passwordeditor=passwordsharedPreferences.edit();
                            passwordeditor.putString("PASSWORD",password);
                            passwordeditor.apply();
                            Registeruser();
                        } else {
                            Toast.makeText(RegisterPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void Registeruser(){
        if(RegUserName.getText().toString().isEmpty()|| RegEmail.getText().toString().isEmpty()){
            Toast.makeText(RegisterPage.this, "Fill the required Details", Toast.LENGTH_SHORT).show();
            return;
        }
        DocumentReference docRef = fStore.collection("users").document(userID);
        Map<String,Object> user = new HashMap<>();
        user.put("Username",RegUserName.getText().toString());
        user.put("Email",RegEmail.getText().toString());
        //add user to database
        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error storing information",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
