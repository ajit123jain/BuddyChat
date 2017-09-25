package com.chat.ajitrajeev.buddychat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout mDisplayName,mEmail,mPassword;
    private Button mCreateButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayName = (TextInputLayout)findViewById(R.id.reg_name);
        mEmail = (TextInputLayout)findViewById(R.id.reg_email);
        mPassword = (TextInputLayout)findViewById(R.id.reg_password);
        mCreateButton = (Button) findViewById(R.id.reg_create_btn);

        mAuth = FirebaseAuth.getInstance();

        mCreateButton.setOnClickListener(new View.OnClickListener() { //create Account button action
            @Override
            public void onClick(View view) {
                String displayName = mDisplayName.getEditText().toString();
                String email = mEmail.getEditText().toString();
                String password = mPassword.getEditText().toString();
            }
        });
    }
    private void register_user(String name , String email ,String password){
          mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()){
                      Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                      startActivity(intent);
                      finish();
                  }
                  else {
                      Toast.makeText(getApplicationContext(),"Your have some problem",Toast.LENGTH_LONG).show();
                  }
              }
          });

    }

}
