package com.mapapplication.aiiapplicationtest;

import java.util.ArrayList;
import java.util.LinkedList;
import android.app.Activity;
import android.content.Intent;

public class Floor {

    String name;
    MyGraph Floorplan;



    Floor(String d) {
        name = d;
    }
    public void findpath(String target, boolean b){
        //System.out.println(name+" "+target);

        if(b) Floorplan.shortestPath("ENTRANCE "+name,target,name);
        else {Floorplan.shortestPath(target, "ENTRANCE "+name,name);}
    }


}
