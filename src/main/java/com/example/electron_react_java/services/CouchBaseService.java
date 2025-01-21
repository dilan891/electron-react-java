package com.example.electron_react_java.services;

import com.couchbase.lite.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouchBaseService {

    public static List<Map<String, Object>> viewDatabase(String scopeName,String collectionName) throws CouchbaseLiteException {
        CouchbaseLite.init();
        List<Map<String, Object>> documents = new ArrayList<>();
        System.out.println(collectionName);
        System.out.println(scopeName);
        try (Database database = new Database("mydb")) {
            Collection collection = database.getCollection(collectionName, scopeName);

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

    /**
     * Busca por nombre de documento
     * @param value
     * @param scopeName
     * @param collectionName
     * @throws CouchbaseLiteException
     */
    public static List<Map<String, Object>> searchDocument(String fieldName, Object value,String scopeName,String collectionName) throws CouchbaseLiteException {
        CouchbaseLite.init();

        try (Database database = new Database("mydb")) {
            Collection collection = database.getCollection(collectionName, scopeName);

            if (collection != null) {
                Query query = QueryBuilder.select(SelectResult.all())
                        //Busca en la coleccion
                        .from(DataSource.collection(collection))
                        // Condición dinámica basada en el campo y el valor
                        .where(Expression.property(fieldName).equalTo(Expression.value(value)));

                try (ResultSet result = query.execute()) {
                    for (Result row : result) {
                        Dictionary all = row.getDictionary(collection.getName());
                        if (all != null) {
                            //System.out.println(all.toMap());
                            return List.of(all.toMap());
                        }
                        else{
                            System.out.println("No se encontraron documentos");
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }



    public static void insertDocument(Map<String, Object> data,String scopeName,String collectionName) throws CouchbaseLiteException {
        CouchbaseLite.init();

        try (Database database = new Database("mydb")) {
            System.out.println(collectionName);
            System.out.println(scopeName);
            Collection collection = database.getCollection(collectionName, scopeName);

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
