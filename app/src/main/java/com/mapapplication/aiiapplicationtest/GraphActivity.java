package com.mapapplication.aiiapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

public class GraphActivity extends AppCompatActivity {
   public String t;
   public String namet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String target=getIntent().getStringExtra("target");
        t=target;
      //  MyGraph g=(MyGraph) getIntent().getParcelableExtra("TargetGraph");
       MyGraph g= findpath();
        setContentView(R.layout.activity_graph);
        MyGraphView GView = findViewById(R.id.graphview);
        GView.invalidate(t);

    }
    public MyGraph findpath(){
        Interior interior=new Interior(t);
        MyGraph g=interior.getFloor(t).Floorplan;
        namet=interior.getFloor(t).name;
        return g;
    }

}


