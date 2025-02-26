package com.whodis.whodis;

import java.net.*;
import java.io.*;
import java.util.*;

public class Master implements Runnable {
    private ConcurrentStore cs;
    private ServerSocket socket;
    private Queue<Socket> jobQueue;
    private Handler[] handlers;

    public Master(int port, int handlerCount) throws IOException{
        this.socket = new ServerSocket(port);
        this.jobQueue = new LinkedList<Socket>();
        this.cs = new ConcurrentStore();
        this.handlers = new Handler[handlerCount];
        for (int i = 0; i < handlerCount; i++) {
            this.handlers[i] = new Handler(cs, jobQueue);
            Thread tmp = new Thread(this.handlers[i]);
            tmp.start();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket cur = socket.accept();
                System.out.println("Accepted conn from " + cur.getRemoteSocketAddress());
                this.jobQueue.add(cur);
            } catch (IOException e) {
                System.out.println(e);
                break;
            }
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
