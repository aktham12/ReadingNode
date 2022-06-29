package org.atypon.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.atypon.data.collection.Database;
import org.atypon.data.collection.JsonCollection;
import org.atypon.data.collection.JsonDocument;
import org.atypon.io.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Getter
public class CRUDService {

    private Database currentDatabase;
    private JsonCollection currentCollection;
    private JsonDocument currentDocument;


    public CRUDService(CRUDServiceBuilder crudServiceBulider) {
        currentDatabase = crudServiceBulider.getCurrentDatabase();
        currentCollection = crudServiceBulider.getCurrentCollection();
        currentDocument = crudServiceBulider.getCurrentDocument();
    }

    public void insert(String json) {
        try {
            currentDocument.insert(json);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMany(ArrayList<String> jsons) {
        try {

            currentDocument.insertMany(jsons);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName() + ".json", currentDocument.getNodesArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateOne(String property, String propertyValue, String newValue) {
        try {

            currentDocument.updateOne(property, propertyValue, newValue);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName(), currentDocument.getNodesArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateMany(String property, String propertyValue, String newValue) {
        try {
            currentDocument.updateMany(property, propertyValue, newValue);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName() + ".json", currentDocument.getNodesArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteOne(String property, String propertyValue) {
        try {
            currentDocument.delete(property, propertyValue);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName() + ".json", currentDocument.getNodesArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMany(String property, String propertyValue) {
        try {
            currentDocument.deleteMany(property, propertyValue);
            JsonWriter.getInstance().write(currentDatabase.getDatabaseName() + "/" + currentCollection.getName() + "/" + currentDocument.getDocumentName() + ".json", currentDocument.getNodesArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<JsonNode> find(String property, String propertyValue) {
        return currentDocument.find(property, propertyValue);
    }

    public synchronized void MakeIndexOn(String property) {
            currentDocument.makeIndexOn(property);
    }


    public void setCurrentDatabase(String currentDatabase) {
        DatabaseService.getDatabase(currentDatabase).ifPresent(database -> this.currentDatabase = database);
    }

    public void setCurrentCollection(String currentCollection) {
        this.currentCollection = currentDatabase.getCollection(currentCollection);
    }

    public void setCurrentDocument(String currentDocument) {
        this.currentDocument = currentCollection.getDocument(currentDocument);
    }

    @Getter
    public static class CRUDServiceBuilder {
        private Database currentDatabase;
        private JsonCollection currentCollection;
        private JsonDocument currentDocument;

        public  CRUDServiceBuilder currentDatabase(String databaseName) {
            DatabaseService.getDatabase(databaseName).ifPresent(database -> this.currentDatabase = database);
            return this;
        }

        public CRUDServiceBuilder currentCollection(String collectionName) {
            DatabaseService.getDatabase(currentDatabase.getDatabaseName()).filter(
                    database -> database.getCollection(collectionName) != null
            ).map(
                    database -> this.currentCollection = database.getCollection(collectionName)
            );


            return this;
        }

        public CRUDServiceBuilder currentDocument(String documentName) {
            this.currentDocument = currentCollection.getDocument(documentName);
            return this;
        }

        public CRUDService build() {
            return new CRUDService(this);
        }
    }


}




