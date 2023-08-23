package com.example.noteapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
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
        imgCreateNoteFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                setOnClickImgCreateNoteFolder();
            }
        });
    }

    private void setOnClickImgCreateNoteFolder() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_newnote);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);

        TextView continueCreateNote = dialog.findViewById(R.id.tv_continuecreatenote);
        EditText editText = dialog.findViewById(R.id.edtnamecreatenote);


        continueCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("namenote", editText.getText().toString());
                bundle.putInt("idfolder", folder.getIdFolder());
                bundle.putString("namefolder", folder.getNameFolder());
                Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.d("databasemain", editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
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
        imgCreateNoteFolder = findViewById(R.id.imgcreatenotenotes);

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

        if (folder != null) {
            tvNameFolderNote.setText(folder.getNameFolder());
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        listNote = new ArrayList<>();
        getListNote();
        if (folder != null) {
            notesAdapter = new NotesAdapter(listNote, this, folder);
        } else {
            notesAdapter = new NotesAdapter(listNote, this);
        }
        recyclerView.setAdapter(notesAdapter);
    }

    private void getListNote() {
        listNote.clear();
        listNote.addAll(databaseHandler.getNotesInFolder(folder.getIdFolder()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.updateData(databaseHandler.getNotesInFolder(folder.getIdFolder()));
    }
}
