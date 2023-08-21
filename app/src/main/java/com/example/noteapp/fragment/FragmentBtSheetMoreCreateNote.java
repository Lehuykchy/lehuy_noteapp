package com.example.noteapp.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.noteapp.R;
import com.example.noteapp.model.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentBtSheetMoreCreateNote extends BottomSheetDialogFragment {
    private  View view;
    private LinearLayout lnPin, lnDelete, lnLock;
    private ImageView lnPinImg, lnDeleteImg, lnLockImg;
    private TextView lnPinTv, lnDeleteTv, lnLockTv;
    private DatabaseHandler databaseHandler;
    private int idNote;
    private Boolean isCheckNote, isPin, isLock;
    private Animation animation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_btshmorecreatenote, container, false);
        initUI();

        setOnClickDelete();
        setOnClickPin();
        setOnClickLock();

        Dialog dialog = getDialog();
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);

        return view;
    }

    private void setOnClickLock() {
        lnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);

            }
        });
    }

    private void setOnClickPin() {
        lnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                if(isPin){
                    isPin = !isPin;
                    lnPinImg.setImageResource(R.drawable.pin);
                    lnPinTv.setTextColor(Color.parseColor("#ECAA18"));
                    lnPinTv.setText("Ghim");
                    databaseHandler.updateIsPinNote(idNote, isPin);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .remove(FragmentBtSheetMoreCreateNote.this).commit();
                }else {
                    isPin = !isPin;
                    lnPinImg.setImageResource(R.drawable.unpin);
                    lnPinTv.setTextColor(Color.parseColor("#ECAA18"));
                    lnPinTv.setText("Bỏ ghim");
                    databaseHandler.updateIsPinNote(idNote, isPin);
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .remove(FragmentBtSheetMoreCreateNote.this).commit();

                }
                Log.i("database", "pin"+ String.valueOf(isPin));

            }
        });

    }

    private void setOnClickDelete() {
        lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteNote(idNote);
                if (getActivity() != null) {
                    getActivity().finish();
                }

            }
        });

    }

    private void initUI() {
        lnDelete = view.findViewById(R.id.lndeletebtsheet);
        lnPin = view.findViewById(R.id.lnpinbtsheet);
        lnLock = view.findViewById(R.id.lnlockbtsheet);
        lnDeleteImg = view.findViewById(R.id.imglndeletebtsheet);
        lnPinImg = view.findViewById(R.id.imglnpinbtsheet);
        lnLockImg = view.findViewById(R.id.imglnlockbtsheet);
        lnPinTv = view.findViewById(R.id.tvlnpinbtsheet);
        lnLockTv = view.findViewById(R.id.tvlnlockbtsheet);
        lnDeleteTv = view.findViewById(R.id.tvlndeletebtsheet);

        animation = new AlphaAnimation(1f, 0.5f);
        animation.setDuration(200);

        databaseHandler = new DatabaseHandler(getActivity(), "dbnoteapp", null, 1);

        Bundle args = getArguments();
        if (args != null) {
            idNote = args.getInt("idnote", 0);
            isCheckNote = args.getBoolean("ischecknote", false);
            isPin = args.getBoolean("ispin", false);
            isLock = args.getBoolean("islock", false);
        }
        if (idNote == 0){
            int color = Color.parseColor("#A5A5A5");
            lnDelete.setEnabled(false);
            lnPin.setEnabled(false);
            lnLock.setEnabled(false);

            lnDeleteImg.setImageResource(R.drawable.trash_can_enable);
            lnDeleteTv.setTextColor(color);

            lnLockImg.setImageResource(R.drawable.lock_enable);
            lnLockTv.setTextColor(color);

            lnPinImg.setImageResource(R.drawable.pin_enable);
            lnPinTv.setTextColor(color);


        }else{
            if(isCheckNote){
                lnDelete.setEnabled(true);
                lnPin.setEnabled(true);
                lnLock.setEnabled(true);

                lnDeleteImg.setImageResource(R.drawable.trash_can);
                lnDeleteTv.setTextColor(Color.parseColor("#D1051D"));

                if(isLock){
                    lnLockImg.setImageResource(R.drawable.unlocked);
                    lnLockTv.setTextColor(Color.parseColor("#071F9C"));
                    lnLockTv.setText("Bỏ khóa");
                }else {
                    lnLockImg.setImageResource(R.drawable.lock);
                    lnLockTv.setTextColor(Color.parseColor("#071F9C"));
                    lnLockTv.setText("Khóa");
                }

                if(isPin){
                    lnPinImg.setImageResource(R.drawable.unpin);
                    lnPinTv.setTextColor(Color.parseColor("#ECAA18"));
                    lnPinTv.setText("Bỏ ghim");
                }else {
                    lnPinImg.setImageResource(R.drawable.pin);
                    lnPinTv.setTextColor(Color.parseColor("#ECAA18"));
                    lnPinTv.setText("Ghim");
                }

            }else {
                int color = Color.parseColor("#A5A5A5");
                lnDelete.setEnabled(false);
                lnPin.setEnabled(false);
                lnLock.setEnabled(false);

                lnDeleteImg.setImageResource(R.drawable.trash_can_enable);
                lnDeleteTv.setTextColor(color);

                lnLockImg.setImageResource(R.drawable.lock_enable);
                lnLockTv.setTextColor(color);

                lnPinImg.setImageResource(R.drawable.pin_enable);
                lnPinTv.setTextColor(color);
            }
        }
    }
}
