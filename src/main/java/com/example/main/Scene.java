package com.example.main;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.algorithm.AStarSearch;
import com.example.classes.Actor;

import com.example.classes.Agent;
import com.example.classes.Agv;
import com.example.classes.AutoAgv;
import com.example.classes.Constant;
import com.example.classes.Graph;
import com.example.classes.Position;
public class Scene {
    public Map<String,int[]> mapOfExits;
    public int harmfullness;
    public Vector<Position> groundPos;
    public Vector<Position> pathPos;
    public Vector<Position> doorPos;
    public Position[] ground;
    public Vector<Position>[][] danhsachke;
    public Vector<Actor> RemoveActors;
    public HashSet<Agent> agents;
    public HashSet<AutoAgv> autoAgvs;
    public Agv mAgv;
    public static int count = 0;
    public Graph graph;
    
    public Scene() {
        initGraph();
        this.RemoveActors = new Vector<Actor>();
        this.mapOfExits = new HashMap<String,int[]>();
        this.mapOfExits.put("Gate1",new int[]{50,13,0});
        this.mapOfExits.put("Gate2",new int[]{50,14,0});
        this.graph = new Graph(52, 28, this.danhsachke, this.pathPos);
        this.autoAgvs = new HashSet<AutoAgv>();
        this.agents = new HashSet<Agent>();
        AStarSearch.init(52,28,this.ground);
        this.mAgv = new Agv(this, 1, 14, ++count, true, this.graph);
        //createRandomAutogAgv();
    }
    
    public void createRandomAutogAgv() {
        var r =(int) Math.floor(Math.random() * this.pathPos.size());
    while (!Constant.validDestination((int)this.pathPos.get(r).x,(int) this.pathPos.get(r).y, 1, 13)) {
      r =(int) Math.floor(Math.random() * this.pathPos.size());
    }
    if (this.graph != null) {
        ++count;
      var tempAgv = new AutoAgv(this, 1, 13, this.pathPos.get(r).x, this.pathPos.get(r).y, this.graph,count);
      
      this.autoAgvs.add(tempAgv);
    }
    }
    public void createAgent() {
        ++count;
        var r1 = (int) Math.floor(Math.random() * this.doorPos.size());
        var r2 = (int) Math.floor(Math.random() * this.doorPos.size());
        
      var agent = new Agent(
        this,
        this.doorPos.get(r1),
        this.doorPos.get(r2),
        this.ground,
        count
      );
      this.agents.add(agent);
      this.graph.setAgents(agents);
    }
    public String update(String mes) {
        mAgv.preUpdate(mes);
        var jsonArray = new JSONArray();
        var jsonAgv = new JSONObject();
        jsonAgv.put("ID",mAgv.id);
        jsonAgv.put("active",true);
        jsonAgv.put("isAgv",mAgv.isAgv);
        jsonAgv.put("X",Math.floor(mAgv.x));
        jsonAgv.put("Y",Math.floor(mAgv.y));
        jsonArray.add(jsonAgv);
        for (var actor: agents) {
            actor.preUpdate();
            var jsonObject = new JSONObject();
            jsonObject.put("ID",actor.id);
            jsonObject.put("active",true);
            jsonObject.put("isAgv",actor.isAgv);
            jsonObject.put("X",Math.floor(actor.x));
            jsonObject.put("Y",Math.floor(actor.y));
            jsonArray.add(jsonObject);
        }
        graph.updateState();
        for (var actor: autoAgvs) {
            actor.preUpdate();
            var jsonObject = new JSONObject();
            jsonObject.put("ID",actor.id);
            jsonObject.put("active",true);
            jsonObject.put("isAgv",actor.isAgv);
            jsonObject.put("X",Math.floor(actor.x));
            jsonObject.put("Y",Math.floor(actor.y));
            jsonArray.add(jsonObject);
        }
        for (var item: RemoveActors) {
            if (item.isAgv) autoAgvs.remove(item);
            else {
                agents.remove(item);
                graph.removeAgent((Agent)item);
            }
            var jsonObject = new JSONObject();
            jsonObject.put("ID",item.id);
            jsonObject.put("active",false);
            jsonArray.add(jsonObject);
        }
        RemoveActors.clear();
        return jsonArray.toJSONString();
    }
    public void add(Actor actor) {

    }
    public void remove(Actor actor) {
        this.RemoveActors.add(actor);
    }
    public void initGraph() {
        try {
            this.groundPos = new Vector<Position>();
            this.pathPos = new Vector<Position>();
            this.doorPos = new Vector<Position>();
            this.danhsachke = new Vector[52][28];
            for (int i = 0; i < 52; i++) 
            for (int j = 0; j < 28; j++) 
                danhsachke[i][j] = new Vector<Position>();
            var ground = new int[52][28];
            var path = new int[52][28];
            var door = new int[52][28];
            var parser = new JSONParser();
            var jsonObject = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir")+"/src/assets/tilemaps/json/hospital.json"));
            var layers = (JSONArray) jsonObject.get("layers");
            
            for (var layer: layers) {
                
                var item = (JSONObject) layer;
                String name = (String) item.get("name");
                Vector<Position> pos;
                if  (name.equals("ground")) {
                    int i = 0;
                    int j = 0;
                    var data = (JSONArray) item.get("data");
                    
                    for (int k = 0; k < (int) data.size(); k++) {
                        
                        i += j / 52;
                        j %= 52;
                        ground[j][i] = ((Long)data.get(k)).intValue();  
                        j++;
                    }
                }
                if  (name.equals("path")) {
                    int i = 0;
                    int j = 0;
                    var data = (JSONArray) item.get("data");
                    
                    for (int k = 0; k < (int) data.size(); k++) {
                        i += j / 52;
                        j %= 52;
                        path[j][i] = ((Long)data.get(k)).intValue();
                        j++;
                        
                    }
                }
                if  (name.equals("door")) {
                    int i = 0;
                    int j = 0;
                    var data = (JSONArray) item.get("data");
                    
                    for (int k = 0; k < (int) data.size(); k++) {
                        i += j / 52;
                        j %= 52;
                        door[j][i] = ((Long)data.get(k)).intValue();
                        j++;
                        
                    }
                }
            }
            
            for (int i = 0; i < 52; i++) {
                for (int j = 0; j < 28; j++) {
             
                    if (ground[i][j] > 0) this.groundPos.add(new Position(i,j));
                    if (path[i][j] > 0) this.pathPos.add(new Position(i,j)); 
                    if (door[i][j] > 0) this.doorPos.add(new Position(i,j));
                }
            }
            this.ground  = this.groundPos.toArray(new Position[this.groundPos.size()]);
            for (int i = 0; i <  this.pathPos.size(); i++) {
                
                for (int j = 0; j <  this.pathPos.size(); j++) 

                    if (checkNeibor(i, j, path)) {
                        this.danhsachke[(int)this.pathPos.get(i).x][(int)this.pathPos.get(i).y].add
                                (new Position(this.pathPos.get(j).x, this.pathPos.get(j).y));
                    }
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }
    public boolean checkNeibor(int i, int j, int[][] path) {
        var x1 = (int) this.pathPos.get(i).x;
        var y1 = (int) this.pathPos.get(i).y;
        var x2 = (int) this.pathPos.get(j).x;
        var y2 = (int) this.pathPos.get(j).y;
        
        if (Math.abs(x1 - x2) + Math.abs(y1 - y2) !=1 ) return false;
       if (path[x1][y1] == 12 && x1 == x2 && y1 - 1 == y2) return true;
        if (path[x1][y1] == 20 && x1 == x2 && y1 + 1 == y2) return true; 
        if (path[x1][y1] == 28 && x1 + 1 == x2 && y1 == y2) return true; 
        if (path[x1][y1] == 36 && x1 - 1 == x2 && y1 == y2) return true; 
        if (path[x1][y1] != 12 && path[x1][y1] != 20 && path[x1][y1] != 28 && path[x1][y1] != 36) {
            if (path[x2][y2] == 12 && x1 == x2 && y1 - 1 == y2) return true;
            if (path[x2][y2] == 20 && x1 == x2 && y1 + 1 == y2) return true; 
            if (path[x2][y2] == 28 && x1 + 1 == x2 && y1 == y2) return true; 
            if (path[x2][y2] == 36 && x1 - 1 == x2 && y1 == y2) return true; 
            //return true;
        }
        return false;
    }
    
}
