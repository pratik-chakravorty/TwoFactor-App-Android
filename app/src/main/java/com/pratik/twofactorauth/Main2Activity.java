package com.pratik.twofactorauth;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    public String temporary;
    public String temporary2;
    public String userid;
    public static int cx,cy ;
    private FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mUserDatabase;
    private FirebaseAuth mAuth;
    private static DatabaseReference myRef;
    private int MODE_PRIVATE;
    private EditText name;
    private ListView listViewArtist;
    private Button buttonAddArtist;

    List<Artist> list;

    //firebase stuff
    DatabaseReference databaseArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        listViewArtist = findViewById(R.id.listView);
        buttonAddArtist = findViewById(R.id.buttonAddArtist);
        name = findViewById(R.id.editText);

        list = new ArrayList<Artist>();
        databaseArtists = FirebaseDatabase.getInstance().getReference("Pratik");
        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });
        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = list.get(position);
                showUpdateDialog(artist.getArtistId(),artist.getArtistName());
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot artistSnapshot:dataSnapshot.getChildren()) {
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    list.add(artist);
                }

                ListAdapter adapter = new com.pratik.twofactorauth.ListAdapter(Main2Activity.this,list);
                listViewArtist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public void signoutbutton(View s) {
        if (s.getId() == R.id.fab) {



            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setMessage("Do you really want to Log Out ?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                Intent y = new Intent(Main2Activity.this, Choice.class);
                                y.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                y.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(y);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Confirm");
            dialog.show();


        }
    }

    private void showUpdateDialog(final String artistId, String artistName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText)dialogView.findViewById(R.id.updateDialogEdit);
        final Button updateButton = (Button) dialogView.findViewById(R.id.updateBtn);
        final Button deleteButton = (Button)dialogView.findViewById(R.id.deleteBtn);
        dialogBuilder.setTitle("Updating Task "+artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();


                if(TextUtils.isEmpty(name)) {
                    editTextName.setError("Task Name Required");
                    return;
                } else {
                    updateArtist(artistId,name);

                    alertDialog.dismiss();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }
        });


    }

    private boolean updateArtist(String id,String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pratik").child(id);

        Artist artist = new Artist(id,name);

        databaseReference.setValue(artist);

        Toast.makeText(this,"Task Updated Successfully",Toast.LENGTH_LONG).show();

        return true;
    }

    private void deleteArtist(String artistId) {
        DatabaseReference drArtist =FirebaseDatabase.getInstance().getReference("Pratik").child(artistId);

        drArtist.removeValue();

        Toast.makeText(Main2Activity.this,"Task deleted",Toast.LENGTH_SHORT).show();
    }

    private void addArtist() {
        String artistName = name.getText().toString().trim();


        if(!TextUtils.isEmpty(artistName)) {
            String id = databaseArtists.push().getKey();

            Artist artist = new Artist(id,artistName);

            databaseArtists.child(id).setValue(artist);

            name.setText("");

            Toast.makeText(getApplicationContext(),"Task Added",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),"Please Enter Task",Toast.LENGTH_SHORT).show();
        }

    }

}
