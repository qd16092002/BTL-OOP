package com.example.classes;

import java.util.HashSet;

import com.example.main.Scene;

public class Actor {
    public double x;
    public double y;
    public int id;
    private int expectedTime;
    public HashSet<Actor> collidedActors;
    public Position velocity;
    public Scene scene;
    public boolean isAgv;
    Actor(
            Scene scene,
            double x,
            double y,
            int id,
            boolean isAgv
            ) {
        this.x = x;
        this.y = y;
        this.isAgv = isAgv;
        this.velocity = new Position(0, 0);
        this.scene = scene;
        this.id = id; 
        scene.add(this);
        this.collidedActors = new HashSet<Actor>();
    }
    public void setVelocity(double x, double y) { 
        this.velocity = new Position(x, y);
    }
    
    public int getExpectedTime() {
        return this.expectedTime;
    }

    public void estimateArrivalTime(double d, double e, double f, double g) {
        this.expectedTime = (int) Math
                .floor(Math.sqrt(Math.pow(f - d, 2) + Math.pow(g - e, 2)) * 0.085);
    }

    public void writeDeadline() {

    }

    public void eraseDeadline() {

    }
    public void preUpdate() {
        
    }
    public void freeze(Actor actor) {
        if (this.collidedActors == null) {
            this.collidedActors = new HashSet<Actor>();
        }

        if (!this.collidedActors.contains(actor)) {
            // Thêm actor
            this.collidedActors.add(actor);
        }
    }

    public void moveTo(double x, double y, double speed) {
        var dx = x - this.x;
        var dy = y - this.y;
        var len = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        if (len > speed) {
            dx = dx / len * speed;
            dy = dy / len * speed;
        }
        setVelocity(dx, dy);
        this.move();
    }

    public void move() {
        this.x += this.velocity.x;
        this.y += this.velocity.y;
    }
}
