package org.atypon.services;

import org.atypon.data.collection.JsonDocument;
import org.atypon.io.DirectoryCreator;

import java.util.Optional;

public class DocumentService {


    public static Optional<JsonDocument> addDocument(String databaseName, String collectionName, JsonDocument document) {

            DatabaseService.getDatabase(databaseName).ifPresent(db -> db.getCollection(collectionName).addDocument(document));
            DirectoryCreator.getInstance().createFile(
                    databaseName
                            + "/"
                            + collectionName,
                    document.getDocumentName()
            );

            return Optional.of(document);

    }

    public static void deleteDocument(String databaseName, String collectionName, String documentName) {


            DatabaseService.getDatabase(databaseName).ifPresent(db -> db.getCollection(collectionName).deleteDocument(documentName));
            DirectoryCreator.getInstance().createDirectory(
                    databaseName
                            + "/"
                            + collectionName
                            + "/"
                            + documentName);
        }
    }



