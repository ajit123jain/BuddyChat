package com.chat.ajitrajeev.buddychat;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mProfileFriendCount;
    private Button mProfileSendRequestBtn;
    private ProgressDialog mProgressDialog;

    private String mCurrent_state;

    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mFriendReqDatabaseReference;
    private FirebaseUser mCurrent_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String user_id = getIntent().getStringExtra("user_id");
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mProfileImage = (ImageView)findViewById(R.id.profile_image);
        mProfileName = (TextView)findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView)findViewById(R.id.profile_status);
        mProfileFriendCount = (TextView)findViewById(R.id.profile_friendCount);
        mProfileSendRequestBtn = (Button)findViewById(R.id.profile_sendRequest);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mCurrent_state = "not_friends";

        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading User Data ... ");
        mProgressDialog.setMessage("Please wait while we load the user data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();



        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String displayName = dataSnapshot.child("name").getValue().toString();
                String displayStatus = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                mProfileName.setText(displayName);
                mProfileStatus.setText(displayStatus);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile).into(mProfileImage);
                mProgressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mProfileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSendRequestBtn.setEnabled(false);

                //-----------This is for Sending Request
                if (mCurrent_state.equals("not_friends")){
                mFriendReqDatabaseReference.child(mCurrent_user.getUid()).child(user_id).child("request_type").
                        setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mFriendReqDatabaseReference.child(user_id).child(mCurrent_user.getUid()).child("request_type").
                                    setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendRequestBtn.setEnabled(true);
                                    mCurrent_state = "req_sent";
                                    mProfileSendRequestBtn.setText("Cancel Friend Request");
                                    Toast.makeText(ProfileActivity.this, "Request Sucessfully Sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(ProfileActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
                //----------This is for Canceling the request .....
                if (mCurrent_state.equals("req_sent")){
                    mFriendReqDatabaseReference.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                    mFriendReqDatabaseReference.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           mProfileSendRequestBtn.setEnabled(true);
                           mCurrent_state = "not_friends";
                           mProfileSendRequestBtn.setText("Send Friend Request");
                        }
                    });
                        }
                    });
                }

            }
        });

}
}