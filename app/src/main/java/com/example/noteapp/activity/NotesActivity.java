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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.NoteItemTouchHelperCallback;
import com.example.noteapp.R;
import com.example.noteapp.adapter.NotesAdapter;
import com.example.noteapp.fragment.FragmentBtSheetMoreCreateNote;
import com.example.noteapp.model.DatabaseHandler;
import com.example.noteapp.model.Folder;
import com.example.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    private RecyclerView recyclerView, rcvNotesPin;
    private NotesAdapter notesAdapter, notesAdapterPin;
    private DatabaseHandler databaseHandler;
    private List<Note> listNote, listNotePin;
    private Folder folder = null;
    private LinearLayout lnExit, lnPinRcv;
    private TextView tvNameFolderNote, tvRcvNameFolderNotes;
    private ImageView imgCreateNoteFolder, imgRltPinRcv;
    private Animation animation;
    private RelativeLayout rltPinRcv;
    private boolean isClick = true;

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
        setOnClickRltPinRcv();
    }

    private void setOnClickRltPinRcv() {
        rltPinRcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick){
                    isClick = !isClick;
                    imgRltPinRcv.setImageResource(R.drawable.baseline_arrow_forward_ios_24);
                    rcvNotesPin.setVisibility(View.GONE);

                }else {
                    isClick = !isClick;
                    imgRltPinRcv.setImageResource(R.drawable.down);
                    rcvNotesPin.setVisibility(View.VISIBLE);

                }
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
                if(folder != null){
                    bundle.putInt("idfolder", folder.getIdFolder());
                    bundle.putString("namefolder", folder.getNameFolder());
                }
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
        rcvNotesPin = findViewById(R.id.rcv_notespin);
        lnPinRcv = findViewById(R.id.lnpinrcv);
        rltPinRcv = findViewById(R.id.rltpinrcv);
        imgRltPinRcv = findViewById(R.id.img_rltpinrcv);
        tvRcvNameFolderNotes = findViewById(R.id.tv_rcvnamefoldernotes);

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

        listNote = new ArrayList<>();
        listNotePin = new ArrayList<>();
        getListNote(listNotePin, listNote);


        if(listNote.size() == 0){
            tvRcvNameFolderNotes.setVisibility(View.GONE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (folder != null) {
            notesAdapter = new NotesAdapter(listNote, this, folder, new NotesAdapter.IClickListenerNote() {
                @Override
                public void onClickItemNote(int position) {
                    Note note = notesAdapter.GetNoteByPosition(position);
                    setBundleNote(note);
                }
            });
        } else {
            notesAdapter = new NotesAdapter(listNote, this, new NotesAdapter.IClickListenerNote() {
                @Override
                public void onClickItemNote(int position) {
                    Note note = notesAdapter.GetNoteByPosition(position);
                    setBundleNote(note);
                }
            });
        }
        ItemTouchHelper.Callback callback = new NoteItemTouchHelperCallback(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        recyclerView.setAdapter(notesAdapter);
        touchHelper.attachToRecyclerView(recyclerView);




        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        rcvNotesPin.setLayoutManager(linearLayoutManager1);
        if (listNotePin.size()>0){
            lnPinRcv.setVisibility(View.VISIBLE);
        }
        if (folder != null) {
            notesAdapterPin = new NotesAdapter(listNotePin, this, folder, new NotesAdapter.IClickListenerNote() {
                @Override
                public void onClickItemNote(int position) {
                    Note note = notesAdapterPin.GetNoteByPosition(position);
                    setBundleNote(note);
                }
            });
        } else {
            notesAdapterPin = new NotesAdapter(listNotePin, this, new NotesAdapter.IClickListenerNote() {
                @Override
                public void onClickItemNote(int position) {
                    Note note = notesAdapterPin.GetNoteByPosition(position);
                    setBundleNote(note);
                }
            });
        }
        rcvNotesPin.setAdapter(notesAdapterPin);
        
    }

    private void setBundleNote(Note note) {
        Bundle bundle = new Bundle();
        bundle.putInt("idnote", note.getIdNote());
        bundle.putInt("idnotefolder", note.getIdFolder());
        bundle.putString("namenotes", note.getName());
        bundle.putString("notecontext", note.getContext());
        bundle.putString("notepassword", note.getPassword());
        bundle.putString("notedate", note.getDate());
        bundle.putBoolean("noteispin", note.isPin());
        bundle.putBoolean("noteislock", note.isLock());

        Intent intent = new Intent(NotesActivity.this, CreateNoteActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void getListNote(List<Note> list1, List<Note> list2) {
        list1.clear();
        list2.clear();

        List<Note> list = new ArrayList<>();
        if(folder == null){
            list = databaseHandler.getAllNote();
        }else{
            list = databaseHandler.getNotesInFolder(folder.getIdFolder());
        }
        for(int i = 0; i < list.size(); i++){
            Note note = list.get(i);
            if(note.isPin()){
                list1.add(note);
            }else {
                list2.add(note);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
            List<Note> list1 =  new ArrayList<>();
            List<Note> list2 =  new ArrayList<>();
            getListNote(list1, list2);
            notesAdapter.updateData(list2);
            notesAdapterPin.updateData(list1);
    }
}
