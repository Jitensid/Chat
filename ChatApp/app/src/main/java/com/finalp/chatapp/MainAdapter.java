package com.finalp.chatapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Message> mDataset;
    private ArrayList<String> mKeyset;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("Messages");

    private FirebaseUser user;

    public MainAdapter(ArrayList<Message> dataset, ArrayList<String> keyset, FirebaseUser user) {
        mDataset = dataset;
        mKeyset = keyset;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Message temp = mDataset.get(position);

        holder.mTitle.setText(temp.getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                Message message = mDataset.get(position);

                final String message_key = mKeyset.get(position);

                if(!message.getAuthor().equals(user.getEmail())){
                    Toast.makeText(view.getContext(),"Only Sender can delete a message!!",Toast.LENGTH_LONG).show();
                    return true;
                }

                final AlertDialog show = new AlertDialog.Builder(view.getContext())
                        .setTitle("Erase Message")
                        .setMessage("Do you really want to delete the message ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                myRef.child(message_key).removeValue();

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle, msender, mdate, mtime;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            mTitle = itemView.findViewById(R.id.title);

        }
    }
}
