package com.junt.xedittext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.junt.xedit.XEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XEditText xEditText=findViewById(R.id.xEditText1);

        xEditText.setOnXEditListener(new XEditText.OnXEditListener() {
            @Override
            public void onSrcClick(XEditText xEditText, XEditText.SrcLoc srcLoc) {
                if (srcLoc== XEditText.SrcLoc.LEFT){
                    //do sth.
                }else {
                    //do sth.
                }
            }

            @Override
            public void onFocusChange(XEditText xEditText, boolean hasFocus) {
                if (hasFocus){
                    //do sth.
                }else {
                    //do sth.
                }
            }
        });

        xEditText.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //get text
        String s=xEditText.getText();

        //do more configuration
        EditText editText = xEditText.getEditText();
        editText.requestFocus();
    }
}