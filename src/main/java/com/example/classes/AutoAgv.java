package com.example.classes;

import static java.lang.System.currentTimeMillis;

import com.example.classes.stateOfAutoAgv.HybridState;
import com.example.classes.stateOfAutoAgv.RunningState;
import com.example.main.Scene;

public class AutoAgv extends Actor {
    public Graph graph;
    public Node[] path;
    public Node curNode;
    public Node endNode;
    public int cur;
    public double waitT;
    public int sobuocdichuyen;
    public double thoigiandichuyen;
    public HybridState hybridState;
    public double endX;
    public double endY;

    public double startX;
    public double startY;

    public AutoAgv(
            Scene scene,
            double x,
            double y,
            double endX,
            double endY,
            Graph graph,
            int id) {
        super(scene, x * 32, y * 32 , id, true);
        this.startX = x * 32;
        this.startY = y * 32;
        this.endX = endX * 32;
        this.endY = endY * 32;

        this.graph = graph;

        this.cur = 0;
        this.waitT = 0;
        this.curNode = this.graph.nodes[(int) x][(int) y];
        this.curNode.setState(StateOfNode2D.BUSY);
        this.endNode = this.graph.nodes[(int) endX][(int) endY];

        this.path = this.calPathAStar(this.curNode, this.endNode);
        this.sobuocdichuyen = 0;
        this.thoigiandichuyen = currentTimeMillis();
        this.estimateArrivalTime(x * 32, y * 32, endX * 32, endY * 32);
        this.hybridState = new RunningState(false);
    }

    public void preUpdate() {
        // this.move();
        // console.log(this.x, this.y);
        this.hybridState.move(this);
    }

    public Node[] calPathAStar(Node start, Node end) {
        return this.graph.calPathAStar(start, end);
    }

    public void changeTarget() {

        var agvsToGate1 = scene.mapOfExits.get("Gate1");
        var agvsToGate2 = scene.mapOfExits.get("Gate2");
        var choosenGate = agvsToGate1[2] < agvsToGate2[2] ? "Gate1" : "Gate2";
        var newArray = scene.mapOfExits.get(choosenGate);
        newArray[2]++;
        scene.mapOfExits.replace(choosenGate, newArray);

        this.startX = this.endX;
        this.startY = this.endY;

        var xEnd = newArray[0];
        var yEnd = newArray[1];

        this.endX = xEnd * 32;
        this.endY = yEnd * 32;

        var finalAGVs = scene.mapOfExits.get(choosenGate)[2];

        this.endNode = this.graph.nodes[xEnd][yEnd];

        this.path = this.calPathAStar(this.curNode, this.endNode);
        this.cur = 0;
        this.sobuocdichuyen = 0;
        this.thoigiandichuyen = currentTimeMillis();
        this.estimateArrivalTime(
                32 * this.startX,
                32 * this.startY,
                this.endX * 32,
                this.endY * 32);
    }
}
