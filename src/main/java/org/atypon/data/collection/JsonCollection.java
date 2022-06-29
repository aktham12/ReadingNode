package org.atypon.data.collection;

import java.util.HashMap;

public class JsonCollection {
    private String name;

    private HashMap<String, JsonDocument> documents;

    public JsonCollection() {
        documents = new HashMap<>();
    }

    public JsonCollection(String name) {
        this.name = name;
        documents = new HashMap<>();
    }

    public JsonDocument addDocument(JsonDocument document) {
        documents.put(document.getDocumentName(), document);
        return document;
    }

    public JsonDocument getDocument(String documentName) {
        return documents.get(documentName);
    }

    public HashMap<String, JsonDocument> getDocuments() {
        return documents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deleteDocument(String name) {
        documents.remove(name);
    }

    public void setDocuments(HashMap<String, JsonDocument> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "JsonCollection{" +
                "name='" + name + '\'' +
                '}';
    }
}
