package com.mapapplication.aiiapplicationtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.*;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.*;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;

public class MyGraph implements Parcelable {

    ArrayList<classroom> adjList;
    int count;

    MyGraph(ArrayList<classroom> a) {
        adjList = a;
        count = 1;
    }

    protected MyGraph(Parcel in) {
        count = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyGraph> CREATOR = new Creator<MyGraph>() {
        @Override
        public MyGraph createFromParcel(Parcel in) {
            return new MyGraph(in);
        }

        @Override
        public MyGraph[] newArray(int size) {
            return new MyGraph[size];
        }
    };

    public void AddVertex(classroom c) {
        adjList.add(c);
        count++;
    }

    public void AddEdge(classroom c1, classroom c2) {
        if (adjList.contains(c1) && adjList.contains(c2)) {
            c1.adjacent.add(c2);
            c2.adjacent.add(c1);
        }
    }

    public void shortestPath(String l1, String l2, String floordetail) {

        int[] prev = new int[adjList.size()];
        Queue<classroom> Q = new ArrayDeque<classroom>();
        boolean[] visited = new boolean[adjList.size()];
        int i = 0;
        int d = -1;
        boolean r = false;
        String s = "";
        for (i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).name.equals(l1))
                break;
        }
        if (i < adjList.size()) {
            classroom v = adjList.get(i);
            Q.add(v);
            visited[i] = true;
            prev[i] = -1;
            while (!Q.isEmpty()) {
                v = Q.remove();
                int p = adjList.indexOf(v);
                LinkedList<classroom> l = v.adjacent;
                for (int j = 0; j < l.size(); j++) {
                    classroom b = l.get(j);
                    int m = adjList.indexOf(b);
                    if (!visited[m]) {
                        Q.add(b);
                        visited[m] = true;
                        prev[m] = p;
                    }
                    if (b.name.equals(l2)) {
                        r = true;
                        d = adjList.indexOf(b);
                        break;
                    }
                }
                if (r == true)
                    break;
            }
            if (r == false) {
                System.out.println("Path does not exist");
                return;
            } else {
                int k = d;
                s = adjList.get(k).name;
                k = prev[k];
                while (k != -1) {
                    s = adjList.get(k).name + " -> " + s;
                    k = prev[k];
                }
                // Highlight the path on the screen
                Canvas canvas = new Canvas();
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);
                while (k != -1) {
                    classroom current = adjList.get(d);
                    classroom parent = adjList.get(prev[d]);
                    canvas.drawLine(current.x, current.y, parent.x, parent.y, paint);
                    d = prev[d];
                }
            }
        } else {
            System.out.println("Source does not exist");
            return;
        }
    }
}