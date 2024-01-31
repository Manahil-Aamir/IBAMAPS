package com.mapapplication.aiiapplicationtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MyGraphView extends View {
    private Paint circlePaint;
    private float circleRadius;
    private ArrayList<classroom> adjList;
    private MyGraph TargetGraph;
    String t;
    private int centerX;
    private int centerY;
    boolean onlytarget=false;


    public MyGraphView(Context context) {
        super(context);
        //  adjList=TargetGraph.adjList;
        init();

    }

    public MyGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // adjList=TargetGraph.adjList;
        init();
    }

    public MyGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // adjList=TargetGraph.adjList;
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE); // Set circle color here
        circleRadius = 200f; // Set circle radius here
    }



    public void setTGraph(MyGraph g,String target) {
        TargetGraph = g;
        t=target;
        adjList = TargetGraph.adjList;
        onlytarget=true;
    }

    private float mPosX;
    private float mPosY;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mPosX, mPosY);

        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        // canvas.drawCircle(centerX, centerY, circleRadius, circlePaint);


        Paint vertexPaint = new Paint();
        vertexPaint.setColor(Color.GRAY);
        vertexPaint.setStyle(Paint.Style.FILL);

        Paint edgePaint = new Paint();
        edgePaint.setColor(Color.BLACK);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(2);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(20);
        String display="";
        // Draw the vertices
        for (int index = 0; index < adjList.size(); index++) {
            classroom c = adjList.get(index);
            canvas.drawCircle(c.x, c.y, 20, vertexPaint);
            if(c.name.contains("ENTRANCE")){
                display=c.name;
                display = display.replace("ENTRANCE", "");
            }

            // Adjust the y-coordinate for text based on the circle position
            String n = c.name.replace("AMANFACULTYOFFICE", "")
                    .replace("TABBAAFACULTYOFFICE", "")
                    .replace("AMANCED", "")
                    .replace("AMAN", "");
            if (n.contains("STAIRS")) {
                n = "STAIRS";
            }
            if (n.contains("ENTRANCE")) {
                n = "ENTRANCE";
            }

            // Calculate the text position
            Rect bounds = new Rect();
            textPaint.getTextBounds(n, 0, n.length(), bounds);
            float textX = c.x - bounds.width() / 2f;
            float textY;

            if (index % 2 == 0) {
                // Place the text below the circle
                textY = c.y + bounds.height() * 2;
            } else {
                // Place the text above the circle
                textY = c.y - bounds.height();
            }

            // Adjust the text position horizontally to prevent overlap with circles
            if (textX < 0) {
                textX = 0;
            } else if (textX + bounds.width() > canvas.getWidth()) {
                textX = canvas.getWidth() - bounds.width();
            }

            // Check if the text overlaps with any other circles or text
            boolean overlaps = false;
            for (int i = 0; i < adjList.size(); i++) {
                if (i != index) {
                    classroom other = adjList.get(i);
                    Rect otherBounds = new Rect();
                    textPaint.getTextBounds(other.name, 0, other.name.length(), otherBounds);

                    float otherTextX = other.x - otherBounds.width() / 2f;
                    float otherTextY;

                    if (i % 2 == 0) {
                        // Place the text below the circle
                        otherTextY = other.y + otherBounds.height() * 2;
                    } else {
                        // Place the text above the circle
                        otherTextY = other.y - otherBounds.height();
                    }

                    // Check if the text overlaps with other circles or text
                    if (textX < otherTextX + otherBounds.width() &&
                            textX + bounds.width() > otherTextX &&
                            textY < otherTextY + otherBounds.height() &&
                            textY + bounds.height() > otherTextY) {
                        overlaps = true;
                        break;
                    }
                }
            }

            // Adjust the text position vertically to avoid overlap
            if (overlaps && index % 2 == 0) {
                textY = c.y + bounds.height() * 3;
            } else if (overlaps && index % 2 != 0) {
                textY = c.y - bounds.height() * 2;
            }

            canvas.drawText(n, textX, textY, textPaint);

            for (classroom adjacentClass : c.adjacent) {
                // Draw the edges
                canvas.drawLine(c.x, c.y, adjacentClass.x, adjacentClass.y, edgePaint);
            }

        }
        // Calculate the display text position
        Rect displayBounds = new Rect();
        textPaint.getTextBounds(display, 0, display.length(), displayBounds);
        float displayTextX = canvas.getWidth() / 2f - displayBounds.width() / 2f;
        float displayTextY = canvas.getHeight() - displayBounds.height() * 2;
// Adjust the display text position towards the center
        displayTextX += canvas.getWidth() * 0.1f;

// Increase the text size
        float textSize = textPaint.getTextSize();
        textPaint.setTextSize(textSize * 1.5f);
// Draw the display text
        canvas.drawText(display, displayTextX-100, displayTextY-200, textPaint);

        if (onlytarget)
            shortestPath(adjList.get(0).name,t,canvas);

        // canvas.drawCircle(centerX, centerY, circleRadius, circlePaint);
        // shortestPath(adjList.get(1).name,adjList.get(4).name,canvas);
    }

    private float mLastTouchX;
    private float mLastTouchY;

    private static final float MAX_PERCENTAGE_OUT_OF_BOUNDS = 0.2f; // 10% out of bounds

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                mLastTouchX = x;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float dx = x - mLastTouchX;
                mPosX += dx;

                // Calculate the max and min position values based on the view size and the max percentage out of bounds
                float viewWidth = getWidth();
                float maxPosX = viewWidth * (1 + MAX_PERCENTAGE_OUT_OF_BOUNDS);
                float minPosX = viewWidth * -MAX_PERCENTAGE_OUT_OF_BOUNDS;

                // Adjust the restriction percentages for left and right panning
                float leftPanRestriction = 0.3f; // Adjust this value for a small left panning
                float rightPanRestriction = 0.3f; // Adjust this value for a small right panning

                // Calculate the restricted range for panning
                float restrictedMaxPosX = maxPosX - viewWidth * rightPanRestriction;
                float restrictedMinPosX = minPosX + viewWidth * leftPanRestriction;

                if (mPosX > restrictedMaxPosX) {
                    mPosX = restrictedMaxPosX;
                } else if (mPosX > restrictedMinPosX) {
                    mPosX = restrictedMinPosX;
                }
                if (mPosX < minPosX) {
                    mPosX = minPosX;
                } else if (mPosX > maxPosX) {
                    mPosX = maxPosX;
                }

                invalidate(t);
                mLastTouchX = x;
                break;
            }
        }
        return true;
    }



    public void shortestPath(String l1, String l2, Canvas canvas) {
        // Updating strings of start and end accordingly
        l1 = l1.replace(" ", "");
        l1 = l1.replace("-", "");
        l2 = l2.replace(" ", "");
        l2 = l2.replace("-", "");
        l1 = l1.toUpperCase();
        l2 = l2.toUpperCase();
        int[] prev = new int[adjList.size()];
        Queue<classroom> Q = new ArrayDeque<>();
        boolean[] visited = new boolean[adjList.size()];
        int i = 0;
        int d = -1;
        boolean r = false;
        String s = "";

        for (i = 0; i < adjList.size(); i++) {
            if (adjList.get(i).name.equals(l1))
                break;
        }
        // Actual implementation of BFS
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
                if (r) {
                    break;
                }
            }
            if (!r) {
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
            }

            // Highlight the shortest path on the canvas
            Paint pathPaint = new Paint();
            pathPaint.setColor(Color.RED);
            pathPaint.setStyle(Paint.Style.STROKE);
            pathPaint.setStrokeWidth(5);


            String[] nodes = s.split(" -> ");

            if (nodes.length >= 2) {
                classroom startClass = getClassroomByName(nodes[0]);
                classroom endClass = getClassroomByName(nodes[nodes.length - 1]);

                for (int index = 1; index < nodes.length; index++) {
                    classroom currentClass = getClassroomByName(nodes[index]);
                    canvas.drawLine(startClass.x, startClass.y, currentClass.x, currentClass.y, pathPaint);
                    startClass = currentClass;
                }
            }
        } else {
            System.out.println("Source does not exist");
            return;
        }
    }



    private classroom getClassroomByName(String name) {
        for (classroom c : adjList) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }

    public void invalidate(String target) {
        super.invalidate();
        Interior i=new Interior(target);
        MyGraph g=i.getFloor(target).Floorplan;
        //  t=target;
        setTGraph(g,target);

    }
}