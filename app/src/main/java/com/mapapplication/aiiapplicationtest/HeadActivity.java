package com.mapapplication.aiiapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HeadActivity extends AppCompatActivity {

    public String h;
    public String namet;
    boolean samebuilding=false;

    boolean same = false;


    String head = "";
    String target = "";
    String p ="";

    boolean Tbuilding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        head=getIntent().getStringExtra("startValue");
        target =getIntent().getStringExtra("endValue");
        samebuilding=getIntent().getBooleanExtra("samebuilding",false);
//        p=getIntent().getStringExtra("Tbuilding");
//        if(p.equals("false")) same = false;
//        else same = true;

        h=head;
        MyGraph g= findpath();
        setContentView(R.layout.activity_head);
        HeadView hView = findViewById(R.id.graphview);

        hView.invalidate(h);
        Button button = findViewById(R.id.next);
//        if(same) button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!samebuilding){
                    callScene();}

                else if(samebuilding){
                    callScene2();
                }
     }
       });


    }

    public void callScene(){
        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        intent.putExtra("startValue", head);
        intent.putExtra("endValue", target);
        startActivity(intent);
    }
    public void callScene2(){
        Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
      //  intent.putExtra("startValue", head);
        intent.putExtra("target", target);
        startActivity(intent);
    }

    public MyGraph findpath(){
        Interior interior=new Interior(h);
        MyGraph g=interior.getFloor(h).Floorplan;
        namet=interior.getFloor(h).name;
        return g;
    }

}