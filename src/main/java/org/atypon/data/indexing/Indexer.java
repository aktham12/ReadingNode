package org.atypon.data.indexing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.atypon.datastructers.BPlusTree.BTree;

import java.util.ArrayList;

public interface Indexer {
    void makeIndexOn(String property, ArrayNode jsonNode);

    ArrayList<JsonNode> get(String property, String exactValue);

    void addToIndexed(String property, JsonNode node);

    String[] getAllPropertyIndexed();

    void addToAllIndexed(JsonNode node);

    void deleteFromIndexed(String property, JsonNode node);

    void deleteFromAllIndexed(JsonNode node);

    void deleteSpecificKey(String property, String key);

    boolean has(String key);

    BTree<String, BTree<String, ArrayList<JsonNode>>> getIndexed();

    void setIndexed(BTree<String, BTree<String, ArrayList<JsonNode>>> indexed);


}
