package com.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.httpserver.RunHttpServer;
import com.example.main.Scene;
import com.example.websocketserver.RunWebSocket;

public class App {
   
    public static void main(String[] args) throws Exception {
        var scene = new Scene();
        RunHttpServer httpServer = new RunHttpServer();
        RunWebSocket webSocket = new RunWebSocket(scene);
        httpServer.start();
        webSocket.start();
        
        /*var count =0;
        while (true) {
            if (count%1000==0) scene.createAgent();
            if (count%400==0) scene.createRandomAutogAgv();
            count++;
            System.out.println(scene.update());
        }*/
        //System.out.println("hello");
    }
}

