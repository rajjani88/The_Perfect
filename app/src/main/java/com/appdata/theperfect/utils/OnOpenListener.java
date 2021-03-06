package com.appdata.theperfect.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

abstract class OnOpenListener implements View.OnTouchListener, AdapterView.OnItemSelectedListener {

    public OnOpenListener(Spinner spinner) {
        spinner.setOnTouchListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            onOpen();
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        onClose();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        onClose();
    }

    abstract public void onOpen();

    abstract public void onClose();
}