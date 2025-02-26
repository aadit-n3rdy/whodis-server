package com.whodis.whodis;

import java.util.HashMap;

public class ConcurrentStore {
    private HashMap<String, byte[]> map;

    public ConcurrentStore() {
        this.map = new HashMap<String, byte[]>();
    }

    public byte[] get(String key) {
        byte[] result;
        synchronized(this.map) {
            result = this.map.get(key);
        }
        return result;
    }

    public void set(String key, byte[] val) {
        synchronized(this.map) {
            if (this.map.containsKey(key)) {
                this.map.replace(key, val);
            } else {
                this.map.put(key, val);
            }
        }
    }
}
