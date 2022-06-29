package org.atypon.services;

import org.atypon.data.collection.JsonCollection;
import org.atypon.io.DirectoryCreator;
import org.atypon.io.DirectoryRemover;

import java.util.Optional;

public class CollectionService {
    private CollectionService() {

    }



    public static Optional<JsonCollection> addCollection(String databaseName, JsonCollection collection) {


            DatabaseService.getDatabase(databaseName).ifPresent(database -> database.addCollection(collection.getName()));
            DirectoryCreator.getInstance()
                    .createDirectory(databaseName
                            + "/"
                            + collection.getName()
                    );
            return Optional.of(collection);
        }



    public static void deleteCollection(String databaseName, String collectionName) {


        DatabaseService.getDatabase(databaseName).ifPresent(database -> database.getCollectionGroup().remove(collectionName));
        DirectoryRemover.getInstance()
                .deleteDirectory(databaseName
                        + "/"
                        + collectionName
                );

    }

}