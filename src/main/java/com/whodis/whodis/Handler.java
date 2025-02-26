package com.whodis.whodis;

import java.net.*;
import java.util.Queue;
import java.util.*;
import java.io.*;

public class Handler implements Runnable {
    private ConcurrentStore cs;
    private Queue<Socket> queue;
    public Handler(ConcurrentStore cs, Queue<Socket> jobQueue) {
        this.cs = cs;
        this.queue = jobQueue;
    }

    public void run() {
        Socket cur;
        while (true) {
            synchronized(this.queue) {
                cur = this.queue.poll();
            }
            if (cur == null) {
                continue;
            }
            System.out.println("Handling...");
            this.handle(cur);
        }
    }
    private void handle(Socket sock) {
        InputStreamReader isr;
        OutputStreamWriter osw;
        try {
            isr = new InputStreamReader(sock.getInputStream());
            osw = new OutputStreamWriter(sock.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        BufferedReader br = new BufferedReader(isr);
        BufferedWriter bw = new BufferedWriter(osw);

        System.out.println("Handling " + sock.getRemoteSocketAddress());

        try {
            try {
                while (true) {
                    String command = br.readLine(); // GET or SET
                    String key = br.readLine();
                    if (command.equals("GET")) {
                        byte[] val = cs.get(key);
                        if (val == null) {
                            bw.write("NOT FOUND");
                        } else {
                            String encoded = Base64.getEncoder().encodeToString(val);
                            System.out.println("Getting " + key + " as " + encoded);
                            bw.write(encoded + "\n");
                            bw.write("DONE");
                        }
                        bw.flush();
                    } else if (command.equals("SET")) {
                        String encodedVal = br.readLine();
                        byte[] val = Base64.getDecoder().decode(encodedVal);
                        System.out.println("Setting " + key + " to " + encodedVal);
                        cs.set(key, val);
                        bw.write("DONE\n");
                        bw.flush();
                    } else {
                        System.err.println("Unknown command \"" + command + "\"");
                        break;
                    }
                }
            } catch (IOException e) {
                bw.write("FAIL\n");
                System.out.println(e);
                return;
            } finally {
                br.close();
                bw.close();
                isr.close();
                osw.close();
                sock.close();
            }
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        return;
    }
}
