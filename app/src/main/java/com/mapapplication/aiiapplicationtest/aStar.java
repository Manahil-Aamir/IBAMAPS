package com.mapapplication.aiiapplicationtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class aStar{
    Node start;
    Node target;
    public ArrayList<Node> way;
    MyGraph g;

    String floor;

    //aStar between two buildings
    public aStar(Node start, Node target){
        this.start=start;
        this.target=target;
        Node n=path(start,target);
        way = printPath(target);
    }

    //aStar between building and a building in which class/rooms/labs is situated
    //BFS to reach class from floor entrance
    public aStar(Node start,Node target, classroom c){

        this.start=start;
        this.target=target;
        Node n=path(start,target);
        way = printPath(target);
        Interior interior = new Interior(c.name);
        g= interior.getPath(c.name, true);
      //  g=interior.TargetClassgetGraph(c.name);
    }

    //aStar between building in which class/rooms/labs is situated and any building
    //BFS to reach entrance of the floor
    public aStar(classroom c, Node start,Node target){
        Interior interior = new Interior(c.name);
        interior.getPath(c.name, false);
        this.start=start;
        this.target=target;
        Node n = path(start,target);
        way = printPath(target);

    }

    //aStar between same/different buildings of provided classes/rooms/labs
    public aStar(classroom c1, Node start,Node target, classroom c2){
        Interior interior1 = new Interior(c1.name);

        if (start.name.equals(target.name)){

        }
        interior1.getPath(c1.name, false);
        this.start = start;
        this.target = target;
        Node n = path(start, target);
        way = printPath(target);
        Interior interior2 = new Interior(c2.name);
        interior2.getPath(c2.name, true);
    }


    //aStar code to determine path
    public Node path(Node start, Node target){
        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.f = start.g + start.calculateHeuristic(target);
        openList.add(start);

        while(!openList.isEmpty()){
            Node n = openList.peek();
            if(n == target){
                return n;
            }

            for(Node.Edge edge : n.neighbors){
                Node m = edge.node;
                double totalWeight = n.g + edge.weight;

                if(!openList.contains(m) && !closedList.contains(m)){
                    m.parent = n;
                    m.g = totalWeight;
                    m.f = m.g + m.calculateHeuristic(target);
                    openList.add(m);
                } else {

                    if(totalWeight < m.g){
                        m.parent = n;
                        m.g = totalWeight;
                        m.f = m.g + m.calculateHeuristic(target);
                        if(closedList.contains(m)){
                            closedList.remove(m);
                            openList.add(m);
                        }
                    }
                }
            }

            openList.remove(n);
            closedList.add(n);
        }
        return null;
    }


    //printing path between buildings on terminal
    public static ArrayList printPath(Node target){
        Node n = target;
        if(n==null)
            return null;
        ArrayList<Node> p = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        while(n.parent != null){
            ids.add(n.name);
            p.add(n);
            n = n.parent;
        }
        ids.add(n.name);
        p.add(n);
        Collections.reverse(ids);
        return p;
    }

}