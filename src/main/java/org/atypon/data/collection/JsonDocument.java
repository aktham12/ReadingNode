package org.atypon.data.collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import org.atypon.data.indexing.BTreeIndexer;
import org.atypon.data.indexing.Indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


@Getter
public class JsonDocument {

    private final String documentName;
    private final ObjectMapper mapper;
    private final String id;
    private ArrayNode nodesArray;
    private  Indexer indexer;
    private int i = 0;

    public JsonDocument(String documentName) {
        this.documentName = documentName;
        this.mapper = new ObjectMapper();
        this.id = UUID.randomUUID().toString();
        nodesArray = mapper.createArrayNode();
        indexer = new BTreeIndexer();
    }

    public JsonDocument(String documentName, String id) {
        this.documentName = documentName;
        this.mapper = new ObjectMapper();
        nodesArray = mapper.createArrayNode();
        indexer = new BTreeIndexer();
        this.id = id;
    }

    public void insert(String json) throws IOException {
        if(json==null || json.equals("")) {
            throw new IllegalArgumentException("Json is null or empty");
        }
        JsonNode node = mapper.readTree(wrapId(json));
        nodesArray.add(node);
        this.updateIndex(node);
    }
    public void insertMany(ArrayList<String> jsons) throws IOException {
        if(jsons==null || jsons.isEmpty()) {
            throw new IllegalArgumentException("Json is null or empty");
        }
        for (String s : jsons) {
            insert(s);
        }
    }

    public void updateOne(String property, String propertyValue, String newValue) throws IOException {
        if(property==null || property.equals("") || propertyValue==null || propertyValue.equals("") || newValue==null || newValue.equals("")) {
            throw new IllegalArgumentException("property, propertyValue or newValue is null or empty");
        }
        int i = 0;
        for (JsonNode node : nodesArray) {
            if (node.get(property).asText().equals(propertyValue)) {
                JsonNode temp = mapper.readTree(wrapId(newValue));
                nodesArray.set(i, temp);
                this.removeFromIndexed(node);
                this.updateIndex(temp);
                break;
            }
            i++;
        }
    }
    public void updateMany(String property, String propertyValue, String newValue) throws IOException {
        if(property==null || property.equals("") || propertyValue==null || propertyValue.equals("") || newValue==null || newValue.equals("")) {
            throw new IllegalArgumentException("property, propertyValue or newValue is null or empty");
        }
        for (JsonNode node : nodesArray) {
            if (node.get(property).asText().equals(propertyValue)) {
                updateOne(property, propertyValue, newValue);
            }
        }

    }

    public void delete(String property, String propertyValue) throws IOException {
        if(property==null || property.equals("") || propertyValue==null || propertyValue.equals("")) {
            throw new IllegalArgumentException("property or propertyValue is null or empty");
        }
        int i = 0;
        for (JsonNode nodes : nodesArray) {

            if (nodes.get(property).asText().equals(propertyValue)) {
                nodesArray.remove(i);
                this.removeFromIndexed(nodes);
                break;
            }
            i++;
        }
    }

    public void deleteMany(String property, String propertyValue) throws IOException {
        if (property == null || property.equals("") || propertyValue == null || propertyValue.equals("")) {
            throw new IllegalArgumentException("property or propertyValue is null or empty");
        }

        ArrayNode temp = mapper.createArrayNode();
        for (JsonNode nodes : nodesArray) {
            if (!nodes.get(property).asText().equals(propertyValue)) {
                temp.add(nodes);
            } else
                this.removeFromIndexed(nodes);
        }
        this.nodesArray = temp;
    }


    public ArrayList<JsonNode> find(String property, String propertyValue) {
        if (property == null || property.equals("") || propertyValue == null || propertyValue.equals("")) {
            throw new IllegalArgumentException("property or propertyValue is null or empty");
        }
        if (indexer.has(property)) {
            return indexer.get(property, propertyValue);
        } else {
            return this.findForNonIndex(property, propertyValue);
        }
    }


    public ArrayList<JsonNode> findForNonIndex(String property, String propertyValue) {
        ArrayList<JsonNode> temp = new ArrayList<JsonNode>();

        for (int i = 0; i < nodesArray.size(); i++) {
            if (nodesArray.get(i).get(property).asText().equals(propertyValue)) {
                temp.add(nodesArray.get(i));
            }

        }
        return temp;
    }

    private String wrapId(String json) {
        return "{\"_id\":\"" + id + "\"," + json.substring(1);
    }


    public ArrayNode getNodesArray() {
        return nodesArray;
    }


    public void makeIndexOn(String property) {
        indexer.makeIndexOn(property, nodesArray);
    }

    private void updateIndex(JsonNode newValue) {
        if (i == 0) {
            i++;
            indexer.makeIndexOn("_id", this.getNodesArray());
        } else {
            for (String property : indexer.getAllPropertyIndexed()) {
                indexer.addToIndexed(property, newValue);
            }
        }
    }

    private void removeFromIndexed(JsonNode value) {
        for (String property : indexer.getAllPropertyIndexed()) {
            if (value.has(property)) {
                indexer.deleteFromIndexed(property, value);
            }
        }
    }

    public void setNodesArray(ArrayNode node) {
        this.nodesArray = node;
    }

    @Override
    public String toString() {
        return "JsonDocument{" +
                "documentName='" + documentName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public BTreeIndexer getIndexer() {
        return (BTreeIndexer) indexer;
    }

    public void setIndexer(Indexer indexer) {
        this.indexer = indexer;
    }
}
