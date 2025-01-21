package com.example.electron_react_java.api;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.example.electron_react_java.models.CollectioNames;
import com.example.electron_react_java.models.TestData;
import com.example.electron_react_java.services.CouchBaseConfig;
import com.example.electron_react_java.services.CouchBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.electron_react_java.services.TestService;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class TestController {
    private final CouchBaseService couchBaseService;
    private final CouchBaseConfig base;

    @Autowired
    public TestController(CouchBaseService couchBaseService, CouchBaseConfig base) {
        this.couchBaseService = couchBaseService;
        this.base = base;
    }

    // tag::get-aggregate-root[]
    @GetMapping("/test")
    public ResponseEntity<String> greeting() {
        String greeting = "Llegue de Java";
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<String> greeting2() {
        String greeting = "Llegueeeeeeeeeeeeeee";
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @GetMapping("/testCouch")
    public ResponseEntity<List<Map<String, Object>>> greeting3() throws CouchbaseLiteException {
        List<Map<String, Object>> documents = CouchBaseService.viewDatabase("zkymed","pruebas");

        if (documents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 si no hay contenido
        }

        return new ResponseEntity<>(documents, HttpStatus.OK); // Devuelve la lista directamente
    }

    @PostMapping("/testInsertCouch")
    public ResponseEntity<String> greeting4(@RequestBody TestData data) throws CouchbaseLiteException {
        String greeting = "Hola mundo a " + data.getName();
        //Ejecuta prueba de conexion con couchBase lite
        CouchBaseService.insertDocument(data.toMap(),"zkymed","pruebas");
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    @GetMapping("/testSyncLocal")
    public ResponseEntity<String> greeting5() throws CouchbaseLiteException, URISyntaxException {
        String greeting = "sincronizando";
        Collection collection = base.getCollection();
        try {
            // Intentar iniciar la replicación
            base.startRepl("ws://localhost:4984/pruebas", collection);
            // Si se inicia correctamente, devolver éxito
            return new ResponseEntity<>("Replication started successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción lanzada y devolver un error con detalle
            String errorMessage = "Failed to start replication: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/testSyncOnline")
    public ResponseEntity<String> greeting7(@RequestBody CollectioNames names) throws CouchbaseLiteException, URISyntaxException {
        String greeting = "sincronizando";
        String scopeName = names.getScopeName();
        String collectionName = names.getCollectionName();
        Collection collection = base.getCollectionOnline(collectionName,scopeName);
        try {
            // Intentar iniciar la replicación
            String uriService = "wss://enfl0gmtoy6qlunr.apps.cloud.couchbase.com:4984/test";
            base.startRepl(uriService, collection);
            // Si se inicia correctamente, devolver éxito
            return new ResponseEntity<>("Replication started successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción lanzada y devolver un error con detalle
            String errorMessage = "Failed to start replication: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/createBase")
    public ResponseEntity<String> greeting6() throws CouchbaseLiteException, URISyntaxException {
        String greeting = "Creando base de datos";
        try {
            // Intentar iniciar la replicación
            base.getStarted();
            // Si se inicia correctamente, devolver éxito
            return new ResponseEntity<>("Base created successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción lanzada y devolver un error con detalle
            String errorMessage = "Failed to create base: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Crea la colección y el scope en la base de datos local
    @PostMapping("/createCollectionLocal")
    public ResponseEntity<String> createCollectionLocal(@RequestBody CollectioNames bodyData) throws CouchbaseLiteException, URISyntaxException {
        try {
            String scopeName = bodyData.getScopeName();
            String collectionName = bodyData.getCollectionName();
            // Intentar iniciar la replicación
            base.getStartedOnline(scopeName,collectionName);
            // Si se inicia correctamente, devolver éxito
            return new ResponseEntity<>("Base created successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción lanzada y devolver un error con detalle
            String errorMessage = "Failed to create base: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retorna los documentos de la base de datos
     * @param names Objeto con el nombre del scope y la colección
     * */
    @GetMapping("/getDocumentsOnline")
    public ResponseEntity<List<Map<String, Object>> > greeting9(@RequestBody CollectioNames names) throws CouchbaseLiteException, URISyntaxException {
        String scopeName = names.getScopeName();
        String collectionName = names.getCollectionName();
        List<Map<String, Object>> data = couchBaseService.viewDatabase(scopeName,collectionName);

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * Inserta un documento en la base de datos
     * */
    @PostMapping("/insertDocumentOnline")
    public ResponseEntity<String> greeting11(@RequestBody TestData data) throws CouchbaseLiteException {
        String scopeName = data.getNames().getScopeName();
        String collectionName = data.getNames().getCollectionName();
        couchBaseService.insertDocument(data.toMap(),scopeName,collectionName);

        String greeting = "Documento insertado en " + scopeName;
        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }

    //Busca por nombre
    @GetMapping("/findByName")
    public ResponseEntity<List<Map<String, Object>>> findByName(@RequestParam String name) throws CouchbaseLiteException {
        List<Map<String, Object>> data = couchBaseService.searchDocument("name",name,"inventory","hotel");
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 si no hay contenido
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    //Busca por edad
    @GetMapping("/findByAge")
    public ResponseEntity<List<Map<String, Object>>> findByAge(@RequestParam Integer age) throws CouchbaseLiteException {
        List<Map<String, Object>> data = couchBaseService.searchDocument("age",age,"inventory","hotel");
        if (data == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 si no hay contenido
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
