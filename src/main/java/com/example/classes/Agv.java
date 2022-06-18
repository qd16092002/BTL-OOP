package com.example.classes;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.example.main.Scene;

public class Agv extends Actor{
    public Graph graph;
    public Node curNode;
    public Node nextNode;
    public boolean keyW,keyA,keyS,keyD;
    public Agv(Scene scene, double x, double y, int id, boolean isAgv, Graph graph) {
        super(scene, x * 32, y * 32, id, isAgv);
        this.graph = graph;
        this.curNode = this.nextNode = graph.nodes[(int)x][(int)y];
        this.curNode.setState(StateOfNode2D.BUSY);
        /*if (graph.nodes[6][18].nodeN!=null) System.out.println(graph.nodes[6][18].nodeN.x+" "+graph.nodes[6][18].nodeN.y);
        if (graph.nodes[6][18].nodeW!=null) System.out.println(graph.nodes[6][18].nodeW.x+" "+graph.nodes[6][18].nodeW.y);
        if (graph.nodes[6][18].nodeS!=null) System.out.println(graph.nodes[6][18].nodeS.x+" "+graph.nodes[6][18].nodeS.y);
        if (graph.nodes[6][18].nodeE!=null) System.out.println(graph.nodes[6][18].nodeE.x+" "+graph.nodes[6][18].nodeE.y);*/
    }
    
    public void preUpdate(String mes) {
        
        try {
            var parser = new JSONParser();  
            //System.out.println(mes + mes.length());
            var jsonObject = (JSONObject) parser.parse(mes);
            this.keyW =(boolean) jsonObject.get("w");
            this.keyA =(boolean) jsonObject.get("a");
            this.keyS =(boolean) jsonObject.get("s");
            this.keyD =(boolean) jsonObject.get("d");
            if (keyW && keyS) keyW = keyS = false;
            if (keyA && keyD) keyA = keyD = false;
            
            if (this.nextNode.x * 32 == this.x  && this.nextNode.y * 32 == this.y ) {
                boolean w = this.keyW && (this.nextNode.nodeN != null) ;
                boolean a = this.keyA && (this.nextNode.nodeW != null) ;
                boolean s = this.keyS && (this.nextNode.nodeS != null) ;
                boolean d = this.keyD && (this.nextNode.nodeE != null) ;
                
                if (w) {
                    if (this.curNode.nodeN != this.nextNode || (!a && !d)) {
                        this.curNode = this.nextNode;
                        this.nextNode = this.nextNode.nodeN;
                        //System.out.println(100);
                    }
                }
                else if (a) {
                    if (this.curNode.nodeW != this.nextNode || (!w && !s)) {
                        this.curNode = this.nextNode;
                        this.nextNode = this.nextNode.nodeW;
                        //System.out.println(200);
                    }
                }
                else if (s) {
                    if (this.curNode.nodeS != this.nextNode || (!a && !d)) {
                        this.curNode = this.nextNode;
                        this.nextNode = this.nextNode.nodeS;
                        //System.out.println(300);
                    }
                }
                else if (d) {
                    if (this.curNode.nodeE != this.nextNode || (!w && !s)) {
                        this.curNode = this.nextNode;
                        this.nextNode = this.nextNode.nodeE;
                        //System.out.println(400);
                    }
                }
                //System.out.println(this.x+" "+this.y+" "+nextNode.x+ " "+nextNode.y);
            }
            boolean w = this.keyW && (this.curNode.nodeN == this.nextNode) ;
            boolean a = this.keyA && (this.curNode.nodeW == this.nextNode) ;
            boolean s = this.keyS && (this.curNode.nodeS == this.nextNode) ;
            boolean d = this.keyD && (this.curNode.nodeE == this.nextNode) ;
            if (w || a || s || d) this.moveTo(nextNode.x * 32, nextNode.y * 32, 1);
            System.out.println(this.x+" "+this.y);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }
}
