package com.example.websocketserver;

import java.io.InputStream;

public class ReiceiverWebSocket extends Thread{
    public InputStream in;
    ReiceiverWebSocket (InputStream in) {
        this.in = in;
    }
    public void run() {
        try {
            
            while (true) {
                //System.out.println(200);    
                byte[] buff = new byte[1024];
                int lineData = in.read(buff);
                for (int i = 0; i < lineData - 6; i++) {
                    buff[i + 6] = (byte) (buff[i % 4 + 2] ^ buff[i + 6]);
                }
                String line = new String(buff, 6, lineData - 6, "UTF-8");
                System.out.println(line);
                
            }
        } catch (Exception e) {
            //TODO: handle exception
            
        }
    }
    
}
