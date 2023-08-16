package com.example.noteapp.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.noteapp.R;
import com.example.noteapp.fragment.FragmentBtSheetMoreCreateNote;

import jp.wasabeef.richeditor.RichEditor;

public class CreateNoteActivity extends AppCompatActivity {


        private RichEditor mEditor;
        private TextView mPreview;
        private ImageView imgMoreCreateNote, imgPlusCreateNote;
        private ImageButton imgBold, imgItalic, imgBullets, imgUnderline,imgListNumber, imgheading;
        private Toolbar toolbarCreateNote;
//        private boolean isImageOne = true;
        private boolean isToolbarVisible = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView((R.layout.activity_createnote));
            
            initUI();
            mEditor.setEditorHeight(200);
            mEditor.setEditorFontSize(22);
            mEditor.setEditorFontColor(Color.RED);
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
                    mPreview.setText(text);
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
                FragmentBtSheetMoreCreateNote bottomSheetFragment = new FragmentBtSheetMoreCreateNote();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }


    private void setOnClickImgHeading() {
            imgheading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setHeading(1);
                }
            });
    }

    private void setOnClickImgListNumber() {
        imgListNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

    }

    private void setOnClickImgBullets() {
       imgBullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

    }

    private void setOnClickImgUnderline() {
            imgUnderline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.setUnderline();
                }
            });

    }

    private void setOnClickImgItalic() {
        imgItalic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

    }

    private void setOnClickImgBold() {
        imgBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

    }

    private void initUI() {
        mEditor = (RichEditor) findViewById(R.id.editor);
        imgBold = findViewById(R.id.action_bold);
        imgItalic = findViewById(R.id.action_italic);
        imgBullets =  findViewById(R.id.action_insert_bullets);
        imgUnderline = findViewById(R.id.action_underline);
        imgListNumber = findViewById(R.id.action_insert_numbers);
        imgheading = findViewById(R.id.action_heading);
        imgMoreCreateNote= findViewById(R.id.imgmorecreatenote);
        imgPlusCreateNote = findViewById(R.id.imgpluscreatenote);
        toolbarCreateNote = findViewById(R.id.toolbarcratenote);
    }

}
