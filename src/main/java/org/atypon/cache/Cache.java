package org.atypon.cache;


import java.util.Set;

public interface Cache<K, V> {
    V put(K k, V v);

    V remove(K o);

    V get(K o);

    boolean containsKey(K key);

     Set<K> keySet();

    int size();


}
