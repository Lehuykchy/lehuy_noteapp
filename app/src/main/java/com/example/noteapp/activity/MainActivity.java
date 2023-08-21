package com.example.noteapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.adapter.FolderAdapter;
import com.example.noteapp.fragment.FragmentBtSheetMoreCreateNote;
import com.example.noteapp.model.DatabaseHandler;
import com.example.noteapp.model.Folder;
import com.example.noteapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imgCreateFolder, imgCreateNote;
    private List<Folder> listFolder;
    private RecyclerView rcvFolder;
    private FolderAdapter folderAdapter;
    private DatabaseHandler databaseHandler;
    private FragmentBtSheetMoreCreateNote fragmentBtSheetMoreCreateNote;
    private ImageView imgMoreCreateNote;
    private TextView tcCountNoteFolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        imgCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCLickCreateFolder();
            }
        });

        imgCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateNote();
            }
        });
    }

    private void onClickCreateNote() {
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        startActivity(intent);
    }

    private void onCLickCreateFolder() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_newfolder);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);

        TextView save = dialog.findViewById(R.id.tv_savecreatefolder);
        TextView destroy = dialog.findViewById(R.id.tv_destroycreatefolder);
        EditText editText = dialog.findViewById(R.id.edtnamelayoutcreate);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int color = Color.parseColor("#81A34D");
                    save.setEnabled(true);
                    save.setTextColor(color);
                } else {
                    int color = Color.parseColor("#A5A5A5");
                    save.setEnabled(false);
                    save.setTextColor(color);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        destroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check = false;
                for(int i=0; i<listFolder.size(); i++){
                    if (listFolder.get(i).getNameFolder().equals(editText.getText().toString())){
                        check = true;
                        break;
                    }
                }
                if(check == false){
                    Folder folder = new Folder(0, editText.getText().toString());
                    databaseHandler.addFolder(folder);
                    getListFolder();
                    folderAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }else {
                    Toast.makeText(MainActivity.this, "Không được trùng tên", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();

    }

    private void initUI() {
        imgCreateFolder = findViewById(R.id.imgcreatefolder);
        imgCreateNote = findViewById(R.id.imgcreatenote);
        rcvFolder = findViewById(R.id.rcv_folder);
        tcCountNoteFolder = findViewById(R.id.tvcountnotefolder);
        fragmentBtSheetMoreCreateNote = new FragmentBtSheetMoreCreateNote();
        databaseHandler = new DatabaseHandler(this, "dbnoteapp", null, 1);

        listFolder = new ArrayList<>();
        getListFolder();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvFolder.setLayoutManager(linearLayoutManager);
        folderAdapter = new FolderAdapter(listFolder, this, new FolderAdapter.IClickListenerFolder() {
            @Override
            public void onClickItemFolder(int position) {
                Folder folder = folderAdapter.GetFolderByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("idfolder", folder.getIdFolder());
                bundle.putString("namefolder", folder.getNameFolder());

                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        rcvFolder.setAdapter(folderAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Note> lists = new ArrayList<>();
                lists.addAll(databaseHandler.getAllNote());
                tcCountNoteFolder.setText(String.valueOf(lists.size()));
                for(int i=0; i<listFolder.size(); i++){
                    List<Note> list = new ArrayList<>();
                    list.addAll(databaseHandler.getNotesInFolder(listFolder.get(i).getIdFolder()));

                }
            }
        }).start();

    }

    private void getListFolder() {
        listFolder.clear();
        listFolder.addAll(databaseHandler.getAllFolder());
        for(int i=0; i<listFolder.size(); i++){
            Log.d("database", String.valueOf(listFolder.get(i).getIdFolder())+ " "
                    + listFolder.get(i).getNameFolder());
        }
    }
}
