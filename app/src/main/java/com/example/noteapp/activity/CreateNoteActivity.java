package com.example.noteapp.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.noteapp.R;
import com.example.noteapp.fragment.FragmentBtSheetMoreCreateNote;
import com.example.noteapp.model.DatabaseHandler;
import com.example.noteapp.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class CreateNoteActivity extends AppCompatActivity {

    private View contentView;
    private RichEditor mEditor;
    private Animation animation;
    private TextView mPreview, tvFinshedCreateNote;
    private ImageView imgMoreCreateNote, imgPlusCreateNote;
    private ImageButton imgBold, imgItalic, imgBullets, imgUnderline, imgListNumber, imgheading;
    private AutoCompleteTextView autoCompleteHeading;
    private Toolbar toolbarCreateNote;
    //        private boolean isImageOne = true;
    private boolean isToolbarVisible = false, isCheckedNewNote = false;
    private LinearLayout lnExitCreateNote;
    private DatabaseHandler databaseHandler;
    private String[] headings = {"Heading1", "Heading2", "Heading3"};
    private boolean isCheckNote;
    private List<Note> listNote;
    private Note note;
    private boolean isLayoutHandled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((R.layout.activity_createnote));
        initUI();
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");
        //mEditor.setInputEnabled(false);

        mPreview = (TextView) findViewById(R.id.preview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                mPreview.setText(mEditor.getHtml());
            }
        });

        setOnClickImgBold();
        setOnClickImgItalic();
        setOnClickImgUnderline();
        setOnClickImgBullets();
        setOnClickImgListNumber();
        setOnClickImgHeading();
        setOnCLickImgMoreCreateNote();
        setOnclickImgPlusCreateNote();
        setOnClickTvFinshedCreateNote();
        setOnClickLnExit();
        setEventKeyPad();


    }

    private void setOnClickLnExit() {
        lnExitCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                finish();
            }
        });
    }

    //xét sự kiện bàn phím hiện lên
    private void setEventKeyPad() {
        contentView = findViewById(android.R.id.content);
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                isLayoutHandled = true;
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getHeight();
                int keypadHeight = screenHeight - contentView.getRootView().getHeight();
                Log.d("sizekeyboard", String.valueOf(keypadHeight));
                if (keypadHeight < -500) {
                    //Nếu bàn phím hiện và có chứ thì sẽ chuyển ischeckenter để lưu note còn
                    // không sẽ không lưu hoặc xóa note hiện tại
                    tvFinshedCreateNote.setVisibility(View.VISIBLE);

                } else {
                    tvFinshedCreateNote.setVisibility(View.GONE);
                    mEditor.clearFocus();
                    Log.d("database", String.valueOf(mEditor.getHtml() == null));
                    if (mEditor.getHtml() == null) {
                        return;
                    }
                    if (mEditor.getHtml().length() != 0) {
                        if (!isCheckedNewNote) {
                            Date date = new Date();
                            date.getTime();
                            isCheckedNewNote = true;
                            note = new Note(0, 1, null, mEditor.getHtml(), false, false, false, null, String.valueOf(date));
                            Log.d("database", " ispin" + String.valueOf(note.isPin())
                                    + " islock" + String.valueOf(note.isLock())
                            );
                            int idNote = (int) databaseHandler.addNote(note);
                            note.setIdNote(idNote);
                            getListNote();
                        } else {
                            Date date = new Date();
                            date.getTime();
                            databaseHandler.updateNote(note.getIdNote(), mEditor.getHtml(), String.valueOf(date));
//                            note = databaseHandler.getNote(note.getIdNote());
                            isCheckNote = true;
                            getListNote();
                        }

                    } else {
                        if (note != null) {
                            databaseHandler.deleteNote(note.getIdNote());
                            isCheckNote = false;
                            getListNote();
                        }
                    }

                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }


    private void setOnClickTvFinshedCreateNote() {
        tvFinshedCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thu bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void setOnclickImgPlusCreateNote() {
        imgPlusCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isToolbarVisible) {
                    imgPlusCreateNote.setImageResource(R.drawable.cancel);
                    showToolbar();
                } else {
                    imgPlusCreateNote.setImageResource(R.drawable.plus);
                    hideToolbar();
                }
            }
        });
    }

    private void showToolbar() {
        ViewPropertyAnimator animator = toolbarCreateNote.animate()
                .translationX(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarCreateNote.setVisibility(View.VISIBLE);
                    }
                });
        animator.start();
        isToolbarVisible = true;
    }

    private void hideToolbar() {
        ViewPropertyAnimator animator = toolbarCreateNote.animate()
                .translationX(toolbarCreateNote.getWidth())
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarCreateNote.setVisibility(View.GONE);
                    }
                });
        animator.start();
        isToolbarVisible = false;
    }

    private void setOnCLickImgMoreCreateNote() {
        imgMoreCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note = databaseHandler.getNote(note.getIdNote());
                FragmentBtSheetMoreCreateNote bottomSheetFragment = new FragmentBtSheetMoreCreateNote();
                Bundle args = new Bundle();
                if (note != null) {
                    args.putInt("idnote", note.getIdNote());
                    args.putBoolean("ischecknote", isCheckNote);
                    args.putBoolean("ispin", note.isPin());
                    args.putBoolean("islock", note.isLock());
                }
                bottomSheetFragment.setArguments(args);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }


    private void setOnClickImgHeading() {
        autoCompleteHeading.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEditor.setHeading(position + 1);
            }
        });
    }

    private void setOnClickImgListNumber() {
        imgListNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                mEditor.setNumbers();

            }
        });

    }

    private void setOnClickImgBullets() {
        imgBullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                mEditor.setBullets();

            }
        });

    }

    private void setOnClickImgUnderline() {
        imgUnderline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                mEditor.setUnderline();

            }
        });

    }

    private void setOnClickImgItalic() {
        imgItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                mEditor.setItalic();
            }
        });

    }

    private void setOnClickImgBold() {
        imgBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                mEditor.setBold();
            }
        });

    }

    private void initUI() {
        mEditor = findViewById(R.id.editor);
        imgBold = findViewById(R.id.action_bold);
        imgItalic = findViewById(R.id.action_italic);
        imgBullets = findViewById(R.id.action_insert_bullets);
        imgUnderline = findViewById(R.id.action_underline);
        imgListNumber = findViewById(R.id.action_insert_numbers);
        imgMoreCreateNote = findViewById(R.id.imgmorecreatenote);
        imgPlusCreateNote = findViewById(R.id.imgpluscreatenote);
        toolbarCreateNote = findViewById(R.id.toolbarcratenote);
        tvFinshedCreateNote = findViewById(R.id.tvfinshedcreatenote);
        lnExitCreateNote = findViewById(R.id.lnexitcreatenote);
        autoCompleteHeading = findViewById(R.id.autohompleteheading);
        databaseHandler = new DatabaseHandler(this, "dbnoteapp", null, 1);

        animation = new AlphaAnimation(1f, 0.5f);
        animation.setDuration(200);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.dropdown_heading, headings);
        autoCompleteHeading.setAdapter(adapter);

        listNote = new ArrayList<>();
    }

    private void getListNote() {
        listNote.clear();
        listNote.addAll(databaseHandler.getAllNote());
        for (int i = 0; i < listNote.size(); i++) {
            Log.d("database", String.valueOf(listNote.size())
                    + " " + String.valueOf(listNote.get(i).getIdNote())
                    + " " + listNote.get(i).getContext()
                    + " ispin/" + String.valueOf(listNote.get(i).isPin())
                    + " islock/" + String.valueOf(listNote.get(i).isLock())
                    + " isChecked/" + String.valueOf(listNote.get(i).isCheckedNewNote())
                    + " " + listNote.get(i).getDate() + "-" + mEditor.getHtml()
                    + " " + String.valueOf(mEditor.getHtml().length()));
        }
    }

}
