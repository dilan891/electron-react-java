package com.example.electron_react_java.services;

import com.couchbase.lite.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouchBaseService {
    public static List<Map<String, Object>> viewDatabase() throws CouchbaseLiteException {
        CouchbaseLite.init();
        List<Map<String, Object>> documents = new ArrayList<>();

        try (Database database = new Database("mydb")) {
            Collection collection = database.getCollection("myCollection", "myScope");

            if (collection != null) {
                Query query = QueryBuilder.select(SelectResult.all()).from(DataSource.collection(collection));

                try (ResultSet result = query.execute()) {
                    for (Result row : result) {
                        Dictionary all = row.getDictionary(collection.getName());
                        if (all != null) {
                            documents.add(all.toMap());
                        }
                    }
                }
            }
        }
        System.out.println("Documentos encontrados: " + documents.size());
        //print documents
        for (Map<String, Object> document : documents) {
            System.out.println(document);
        }
        return documents;
    }

    public static void insertDocument(Map<String, Object> data) throws CouchbaseLiteException {
        CouchbaseLite.init();

        try (Database database = new Database("mydb")) {
            Collection collection = database.getCollection("myCollection", "myScope");

            if (collection != null) {
                MutableDocument mutableDoc = new MutableDocument()
                        .setData(data);
                collection.save(mutableDoc);

                System.out.println("Id del documento insertado: " + mutableDoc.getId());
                System.out.println("Documento insertado");
            }else {
                System.out.println("La coleccion no existe");
            }
        }
    }

}
