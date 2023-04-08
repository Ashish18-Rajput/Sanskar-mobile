package com.example.appsquadz.youtubeanimation;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.appcompat.app.AppCompatActivity;

public class DragActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        DragLayout dragLayout = (DragLayout) findViewById(R.id.dragLayout);

        if(getIntent().getBooleanExtra("horizontal", false)) {
            dragLayout.setDragHorizontal(true);
        }
        if(getIntent().getBooleanExtra("vertical", false)) {
            dragLayout.setDragVertical(true);
        }
        if(getIntent().getBooleanExtra("edge", false)) {
            dragLayout.setDragEdge(true);
        }
        if(getIntent().getBooleanExtra("capture", false)) {
            dragLayout.setDragCapture(true);
        }
    }
}
