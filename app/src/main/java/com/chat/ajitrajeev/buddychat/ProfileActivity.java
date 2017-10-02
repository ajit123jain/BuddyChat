package com.chat.ajitrajeev.buddychat;

import android.app.ProgressDialog;
import android.provider.ContactsContract;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mProfileFriendCount;
    private Button mProfileSendRequestBtn,mProfileDeclineRequestBtn;
    private ProgressDialog mProgressDialog;

    private String mCurrent_state;

    private DatabaseReference mUserDatabaseReference;
    private DatabaseReference mFriendReqDatabaseReference;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mFriendDatabase;
    private FirebaseUser mCurrent_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final String user_id = getIntent().getStringExtra("user_id");
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mProfileImage = (ImageView)findViewById(R.id.profile_image);
        mProfileName = (TextView)findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView)findViewById(R.id.profile_status);
        mProfileFriendCount = (TextView)findViewById(R.id.profile_friendCount);
        mProfileSendRequestBtn = (Button)findViewById(R.id.profile_sendRequest);
        mProfileDeclineRequestBtn = (Button)findViewById(R.id.profile_declineRequest);


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
                //------Friend List /Request Feature
                mFriendReqDatabaseReference.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if (req_type.equals("received")){
                                mCurrent_state = "req_received";
                                mProfileSendRequestBtn.setText("Accept Friend Request");
                            }
                            else
                                if (req_type.equals("sent")){
                                    mCurrent_state = "req_sent";
                                    mProfileSendRequestBtn.setText("Cancel Request");
                                }
                            mProgressDialog.dismiss();
                        }
                        else {
                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user_id)){
                                        mCurrent_state = "friends";
                                        mProfileSendRequestBtn.setText("UnFriend this person");

                                        mProfileDeclineRequestBtn.setVisibility(View.INVISIBLE);
                                        mProfileSendRequestBtn.setEnabled(false);

                                    }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






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
                                  //  mProfileSendRequestBtn.setEnabled(true);
                                    HashMap<String,String> notificationData = new HashMap<String, String>();
                                    notificationData.put("from",mCurrent_user.getUid());
                                    notificationData.put("type","request");

                                    mNotificationDatabase.child(user_id).push().setValue(notificationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mCurrent_state = "req_sent";
                                            mProfileSendRequestBtn.setText("Cancel Friend Request");
                                            Toast.makeText(ProfileActivity.this, "Request Sucessfully Sent", Toast.LENGTH_SHORT).show();

                                            mProfileDeclineRequestBtn.setVisibility(View.INVISIBLE);
                                            mProfileSendRequestBtn.setEnabled(false);
                                        }
                                    });

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

                            mProfileDeclineRequestBtn.setVisibility(View.INVISIBLE);
                            mProfileSendRequestBtn.setEnabled(false);
                        }
                    });
                        }
                    });
                }
                // -----THis is for request received state
                if (mCurrent_state.equals("req_received")){
                    final String current_date = DateFormat.getDateTimeInstance().format(new Date());
                   mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).setValue(current_date).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).setValue(current_date).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                                   mFriendReqDatabaseReference.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           mFriendReqDatabaseReference.child(user_id).child(mCurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   mProfileSendRequestBtn.setEnabled(true);
                                                   mCurrent_state = "friends";
                                                   mProfileSendRequestBtn.setText("UnFriend this person");

                                                   mProfileDeclineRequestBtn.setVisibility(View.INVISIBLE);
                                                   mProfileSendRequestBtn.setEnabled(false);
                                               }
                                           });
                                       }
                                   });


                               }
                           });




                       }
                   });
                }

            }
        });

}
}