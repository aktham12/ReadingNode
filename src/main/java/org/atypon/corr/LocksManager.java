package org.atypon.corr;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksManager {
    private LocksManager() {}
    private static final Map<String, ReentrantReadWriteLock> writeLockHashMap = new ConcurrentHashMap<>();


    public static ReentrantReadWriteLock getLock(String key) {
        if (!writeLockHashMap.containsKey(key)) {
            writeLockHashMap.put(key, new ReentrantReadWriteLock());
        }
        return writeLockHashMap.get(key);
    }



}
