package com.sanskar.tv.module.HomeModule;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sanskar.tv.R;

public class demo extends AppCompatActivity {
EditText et_message;
LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
      /*  et_message=findViewById(R.id.et_message);
        et_message.requestFocus();
        InputMethodManager imm = (InputMethodManager) et_message.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_message, InputMethodManager.SHOW_IMPLICIT);*/

    }
}
