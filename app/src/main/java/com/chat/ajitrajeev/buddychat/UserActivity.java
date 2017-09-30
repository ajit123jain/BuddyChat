package com.chat.ajitrajeev.buddychat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    RecyclerView mUsersList;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mToolbar = (Toolbar)findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = (RecyclerView)findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(UserActivity.this));

        Log.d("Tag","On Create Method Called");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Tag","On Start Method Called");
        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(Users.class,R.layout.single_user_layout,
                UsersViewHolder.class,mUserDatabase) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                              viewHolder.setName(model.getName());
                              viewHolder.setStatus(model.getStatus());
                              viewHolder.setUserImage(model.thumb_image,UserActivity.this);
                              final String user_id = getRef(position).getKey();
                              viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Intent profileIntent = new Intent(getApplicationContext(),ProfileActivity.class);
                                      profileIntent.putExtra("user_id",user_id);
                                      startActivity(profileIntent);
                                  }
                              });


            }
        };
        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{
             View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView =itemView;
        }
        public void setName(String name){
            TextView userNameView = (TextView)mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);
          }
        public void setStatus(String status){
            TextView userStatusView = (TextView)mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);
            Log.d("TAG","Status Called"+status);
        }
        public void setUserImage(String thumb_image,Context ctx){
            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.profile).into(userImageView) ;
        }



    }
}
