package com.example.noteapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.adapter.NotesAdapter;
import com.example.noteapp.model.DatabaseHandler;
import com.example.noteapp.model.Folder;
import com.example.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private DatabaseHandler databaseHandler;
    private List<Note> listNote;
    private Folder folder = null;
    private LinearLayout lnExit;
    private TextView tvNameFolderNote;
    private ImageView imgCreateNoteFolder;
    private Animation animation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        initUI();

        setOnClickLnExit();
    }

    private void setOnClickLnExit() {
        lnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                finish();
            }
        });
    }

    private void initUI() {
        recyclerView = findViewById(R.id.rcv_notes);
        lnExit = findViewById(R.id.lnexitcreatenotenotes);
        tvNameFolderNote = findViewById(R.id.tvnamefoldernotes);
        animation = new AlphaAnimation(1f, 0.5f);
        animation.setDuration(200);


        databaseHandler = new DatabaseHandler(this, "dbnoteapp", null, 1);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            int idFolder = receivedBundle.getInt("idfolder", 0);
            String nameFolder = receivedBundle.getString("namefolder", null);
            folder = new Folder(idFolder, nameFolder);
            Log.d("database", String.valueOf(idFolder) + " " + nameFolder);
        }

        if(folder != null){
            tvNameFolderNote.setText(folder.getNameFolder());
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        listNote = new ArrayList<>();
        getListNote();
        if(folder != null){
            notesAdapter = new NotesAdapter(listNote, this, folder);
        }else {
            notesAdapter = new NotesAdapter(listNote, this);
        }
        recyclerView.setAdapter(notesAdapter);
    }

    private void getListNote() {
        listNote.clear();
        listNote.addAll(databaseHandler.getNotesInFolder(folder.getIdFolder()));
    }
}
