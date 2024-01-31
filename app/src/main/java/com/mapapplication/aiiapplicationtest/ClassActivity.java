package com.mapapplication.aiiapplicationtest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.RelativeLayout;
import com.mapapplication.aiiapplicationtest.ClassView;

public class ClassActivity extends AppCompatActivity {
   public String t;
   public String h;
   public String namet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String target=getIntent().getStringExtra("endValue");
        String head = getIntent().getStringExtra("startValue");
        t=target;
        h = head;
        String[] s = new String[2];
        s[0] = h;
        s[1] = t;
      //  MyGraph g=(MyGraph) getIntent().getParcelableExtra("TargetGraph");
       MyGraph g= findpath();
        setContentView(R.layout.activity_class);
        ClassView classView = findViewById(R.id.graphview);
        classView.invalidate(s);

    }
    public MyGraph findpath(){
        Interior interior=new Interior(t);
        MyGraph g=interior.getFloor(t).Floorplan;
        namet=interior.getFloor(t).name;
        return g;
    }

}


