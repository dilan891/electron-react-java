package com.example.electron_react_java.services;

import com.couchbase.lite.*;

import javax.xml.crypto.Data;
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
        Collection collection = database.createCollection("myCollection", "myScope");
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

    public Replicator startRepl(String uri, Collection collection) throws URISyntaxException {
        CollectionConfiguration collConfig = new CollectionConfiguration()
                .setPullFilter((doc, flags) -> "Java".equals(doc.getString("language")));

        ReplicatorConfiguration replConfig = new ReplicatorConfiguration(
                new URLEndpoint(new URI(uri)))
                .addCollection(collection, collConfig)
                .setType(ReplicatorType.PUSH_AND_PULL)
                .setAuthenticator(new BasicAuthenticator("username", "password".toCharArray()));

        Replicator repl = new Replicator(replConfig);

        // Listen to replicator change events.
        // Use `token.remove()` to stop the listener
        ListenerToken token = repl.addChangeListener(change -> {
            System.out.println("Replicator state :: " + change.getStatus().getActivityLevel());
        });

        // Start replication.
        repl.start();

        return repl;
    }

    public Collection getCollection() throws CouchbaseLiteException {
        Database database = new Database("mydb");
        return database.createCollection("myCollection", "myScope");
    }

}
