package com.Application.khanapina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

public class LoginPage extends AppCompatActivity {

    //EditText And buttons declaration
     EditText PhoneNumber;
     EditText code;
    String mobilenumber;

    //Firebase Tool Declaration
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;
    FirebaseAuth firebaseAuth;

    //for step 1 we declare shared preferences to store the value and save user login session
    //step 2 is in MainActivity page
    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        PhoneNumber=(EditText)findViewById(R.id.contacnumber);
        code=(EditText)findViewById(R.id.code);
        firebaseAuth=FirebaseAuth.getInstance();
        //getting the preferences
        sharedPreferences=getSharedPreferences("user_number",MODE_PRIVATE);
        intent=new Intent(LoginPage.this,MainActivity.class);
        //check if we are logged in with mobile then it will open main page other wise login page
        if(sharedPreferences.contains("MOBILE")){
            startActivity(intent);
        }


        verificationStateChangedCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Signinwithmobile(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(LoginPage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

    }


    public void verify(View view) {
        mobilenumber=code.getText().toString()+PhoneNumber.getText().toString();
        if (TextUtils.isEmpty(PhoneNumber.getText().toString())){
            Toast.makeText(LoginPage.this,"Enter Number",Toast.LENGTH_SHORT).show();
        }
        else if(PhoneNumber.getText().toString().length()!=10){
            Toast.makeText(LoginPage.this,"Enter correct number",Toast.LENGTH_SHORT).show();
        }
        else{

            VerifyMobileNumber(mobilenumber);
        }
    }

    private void VerifyMobileNumber(String mobilenumber) {
        //firebase function phoneAuthProvider
        //verificationStateChangedCallbacks:we get to know the task was completed and
        // mobile number was verified correctly or not
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobilenumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationStateChangedCallbacks);
    }

    private void Signinwithmobile(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginPage.this, "Mobile number verified successfully.", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentuser=task.getResult().getUser();
                    String uid=currentuser.getUid();
                    String num=currentuser.getPhoneNumber() ;
                    //saving the data in shared preferences so that we can load it in other activity
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("MOBILE",num);
                    editor.commit();
                     startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginPage.this, "Error using Otp Login", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
