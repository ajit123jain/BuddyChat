package com.chat.ajitrajeev.buddychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
     Button mRegButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mRegButton = (Button)findViewById(R.id.start_reg_btn);

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_Intent = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(reg_Intent);
            }
        });
    }
}
