package com.whodis.whodis;

import java.io.*;
import java.util.*;
import java.net.*;
import java.nio.charset.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter server IP address: ");
        String ip = sc.nextLine();
        System.out.println("Enter port: ");
        int port = sc.nextInt();
        
        Socket sock = new Socket(ip, port);

        BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

        boolean done = false;

        while (!done) {
            System.out.println("0 - set\n1 - get\n2 - exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 0:
                    handleSet(br, bw, sc);
                    break;
                case 1:
                    handleGet(br, bw, sc);
                    break;
                case 2:
                default:
                    done = true;
                    break;
            }
        }
    
        sock.close();
        sc.close();
    }

    static void handleSet(BufferedReader br, BufferedWriter bw, Scanner sc) throws IOException {
        sc.nextLine();
        System.out.println("Enter the key: ");
        String key = sc.nextLine();
        System.out.println("Enter the value: ");
        String value = sc.nextLine();
        System.out.println(key + " " + value);
        byte[] data = value.getBytes();
        bw.write("SET\n" + key + "\n" + Base64.getEncoder().encodeToString(data) + "\n");
        bw.flush();
        String status = br.readLine();
        if (status != null && status.compareTo("DONE") == 0) {
            System.out.println("Set successfully");
        } else {
            System.out.println("Failed");
        }
    }

    static void handleGet(BufferedReader br, BufferedWriter bw, Scanner sc) throws IOException {
        sc.nextLine();
        System.out.println("Enter the key: ");
        String key = sc.nextLine();
        System.out.println("Getting key " + key);
        bw.write("GET\n" + key + "\n");
        bw.flush();
        String encoded = br.readLine();
        if (encoded.compareTo("NOT FOUND") == 0) {
            System.out.println("Key not found");
            return;
        } else {
            byte[] buf = Base64.getDecoder().decode(encoded);
            String str = new String(buf, StandardCharsets.UTF_8);
            System.out.println("Value: " + str);
        }
    }
}
