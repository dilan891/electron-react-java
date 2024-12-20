package com.example.electron_react_java.services;

import com.couchbase.lite.*;

import java.net.URI;
import java.net.URISyntaxException;

public class CouchBaseConfig {

    public void getStarted() throws CouchbaseLiteException, URISyntaxException{
        // One-off initialization
        CouchbaseLite.init();
        System.out.println("CBL Initialized");

        // Create a database
        Database database = new Database("mydb");
        System.out.println("Database created: mydb");

        // Create a new collection (like a SQL table) in the database.
        Collection collection = database.createCollection("pruebas", "zkymed");
        System.out.println("Collection created: " + collection);

        // Create a new document (i.e. a record)
        // and save it in a collection in the database.
        MutableDocument mutableDoc = new MutableDocument()
                .setString("version", "2.0")
                .setString("language", "Java");
        collection.save(mutableDoc);

        // Retrieve immutable document and log the database generated
        // document ID and some document properties
        Document document = collection.getDocument(mutableDoc.getId());
        if (document == null) {
            System.out.println("No such document :: " + mutableDoc.getId());
        }
        else {
            System.out.println("Document ID :: " + document.getId());
            System.out.println("Learning :: " + document.getString("language"));
        }

        // Retrieve and update a document.
        document = collection.getDocument(mutableDoc.getId());
        if (document != null) {
            collection.save(document.toMutable().setString("language", "Kotlin"));
        }

        // Create a query to fetch documents with language == "Kotlin"
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection))
                .where(Expression.property("language")
                        .equalTo(Expression.string("Kotlin")));

        try (ResultSet rs = query.execute()) {
            System.out.println("Number of rows :: " + rs.allResults().size());
        }


        System.out.println("Finish process");
    }

    public void getStartedOnline() throws CouchbaseLiteException, URISyntaxException {
        // One-off initialization
        CouchbaseLite.init();
        System.out.println("CBL Initialized");

        // Create a database
        Database database = new Database("mydb");
        System.out.println("Database created: mydb");

        // Create a new collection (like a SQL table) in the database.
        Collection collection = database.createCollection("hotel", "inventory");
        System.out.println("Collection created: " + collection);

        // Create a new document (i.e. a record)
        // and save it in a collection in the database.
        MutableDocument mutableDoc = new MutableDocument()
                .setString("version", "2.0")
                .setString("language", "Java");
        collection.save(mutableDoc);

        // Retrieve immutable document and log the database generated
        // document ID and some document properties
        Document document = collection.getDocument(mutableDoc.getId());
        if (document == null) {
            System.out.println("No such document :: " + mutableDoc.getId());
        } else {
            System.out.println("Document ID :: " + document.getId());
            System.out.println("Learning :: " + document.getString("language"));
        }

        // Retrieve and update a document.
        document = collection.getDocument(mutableDoc.getId());
        if (document != null) {
            collection.save(document.toMutable().setString("language", "Kotlin"));
        }

        // Create a query to fetch documents with language == "Kotlin"
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection))
                .where(Expression.property("language")
                        .equalTo(Expression.string("Kotlin")));

        try (ResultSet rs = query.execute()) {
            System.out.println("Number of rows :: " + rs.allResults().size());
        }
    }

    public void startRepl(String uri, Collection collection) throws URISyntaxException {
        CollectionConfiguration collConfig = new CollectionConfiguration();
                /*.setPullFilter((doc, flags) -> {
                       return (Boolean) doc.getBoolean("sync");
                });*/

        ReplicatorConfiguration replConfig = new ReplicatorConfiguration(
                new URLEndpoint(new URI(uri)))
                .addCollection(collection, collConfig)
                .setType(ReplicatorType.PUSH_AND_PULL)
                .setAuthenticator(new BasicAuthenticator("syncUser", "Abc1234/".toCharArray()));

        Replicator repl = new Replicator(replConfig);

        // Listen to replicator change events.
        repl.addChangeListener(change -> {
            System.out.println("Replicator state :: " + change.getStatus().getActivityLevel());

            if (change.getStatus().getError() != null) {
                System.err.println("Replication error: " + change.getStatus().getError());
            }

            // Imprimir los documentos cuando el estado sea "STOPPED" o "IDLE" (replicación completa o en pausa).
            if (change.getStatus().getActivityLevel() == ReplicatorActivityLevel.IDLE || change.getStatus().getActivityLevel() == ReplicatorActivityLevel.STOPPED) {
                printAllDocuments(collection);
            }
        });

        // Start replication.
        repl.start();

    }

    private void printAllDocuments(Collection collection) {
        // Crear una consulta para seleccionar todos los documentos
        Query query = QueryBuilder.select(SelectResult.all())
                .from(DataSource.collection(collection));

        try {
            ResultSet resultSet = query.execute();
            for (Result result : resultSet) {
                System.out.println(result.toMap());
            }
        } catch (Exception e) {
            System.err.println("Error al consultar los documentos: " + e.getMessage());
        }
    }

    public Collection getCollection() throws CouchbaseLiteException {
        Database database = new Database("mydb");
        return database.createCollection("pruebas", "zkymed");
    }

    public Collection getCollectionOnline() throws CouchbaseLiteException {
        Database database = new Database("mydb");
        System.out.println("hotel");
        return database.getCollection("hotel", "inventory");
    }

}
