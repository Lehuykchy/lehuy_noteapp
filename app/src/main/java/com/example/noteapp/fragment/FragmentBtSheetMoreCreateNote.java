package com.example.noteapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.noteapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FragmentBtSheetMoreCreateNote extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_btshmorecreatenote, container, false);
    }
}
