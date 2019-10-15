package com.finalp.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private Button logout;
    private FloatingActionButton sent;
    private EditText input_message;

    private RecyclerView mrecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Message> mDataset;
    private ArrayList<String >mKeyset;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Messages");
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        mrecyclerView = findViewById(R.id.recycler_view);
        mDataset = new ArrayList<Message>();
        mKeyset = new ArrayList<String>();

        mDataset.add(new Message("jiten@gmail.com","Welcome to DoubtRoom"));
        mDataset.add(new Message("jiten@gmail.com","Welcome to the DoubtRoom!!"));

        mKeyset.add("Random");
        mKeyset.add("Random");

        load_chats();

        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerView.setHasFixedSize(true);
        mAdapter = new MainAdapter(mDataset, mKeyset, user);
        mrecyclerView.setLayoutManager(mLayoutManager);
        mrecyclerView.setAdapter(mAdapter);
        mrecyclerView.scrollToPosition(mDataset.size() - 1);

        input_message = findViewById(R.id.input_message);
        sent = findViewById(R.id.sent);
        logout = findViewById(R.id.logout);

        sent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(input_message.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Nothing to send",Toast.LENGTH_SHORT).show();
                    return;
                }

                String user_email = user.getEmail();

                String key = myRef.push().getKey();

                Message new_message = new Message(user_email, input_message.getText().toString());

                myRef.child(key).setValue(new_message);
                input_message.setText("");
                mAdapter.notifyDataSetChanged();
                mrecyclerView.scrollToPosition(mDataset.size()-1);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent myintent = new Intent(getApplicationContext(), Login.class);
                startActivity(myintent);
                finish();
            }
        });

    }

    public void load_chats(){

        myRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //String existing_message = dataSnapshot.getValue(String.class);

                Message existing_message = dataSnapshot.getValue(Message.class);

                mDataset.add(existing_message);
                mKeyset.add(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
                mrecyclerView.scrollToPosition(mDataset.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                int index = mKeyset.indexOf(dataSnapshot.getKey());

                mDataset.remove(index);
                mKeyset.remove(index);

                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}