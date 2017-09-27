package com.chat.ajitrajeev.buddychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout mDisplayName,mEmail,mPassword;
    private Button mCreateButton;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayName = (TextInputLayout)findViewById(R.id.reg_name);
        mEmail = (TextInputLayout)findViewById(R.id.reg_email);
        mPassword = (TextInputLayout)findViewById(R.id.reg_password);
        mCreateButton = (Button) findViewById(R.id.reg_create_btn);
        mToolbar = (Toolbar)findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(this);

        mCreateButton.setOnClickListener(new View.OnClickListener() { //create Account button action
            @Override
            public void onClick(View view) {
                String displayName = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                Log.d("Username ",displayName);
                Log.d("Email ",email);
                Log.d("Password",password);
                register_user(email,password);
                
            }
        });
    }
    private void register_user(String email ,String password){
          mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()){
                      mRegProgress.dismiss();
                      Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                      startActivity(intent);
                      finish();
                  }
                  else {
                      mRegProgress.hide();
                      Toast.makeText(getApplicationContext(),"Can not Sign in chek the form and try again ",Toast.LENGTH_LONG).show();
                  }
              }
          });

    }

}
