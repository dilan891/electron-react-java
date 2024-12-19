package com.example.electron_react_java.api;

import com.couchbase.lite.Collection;
import com.couchbase.lite.CouchbaseLiteException;
import com.example.electron_react_java.models.TestData;
import com.example.electron_react_java.services.CouchBaseConfig;
import com.example.electron_react_java.services.CouchBaseService;
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
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
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
        CouchBaseConfig base = new CouchBaseConfig();
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
    public ResponseEntity<String> greeting7() throws CouchbaseLiteException, URISyntaxException {
        String greeting = "sincronizando";
        CouchBaseConfig base = new CouchBaseConfig();
        Collection collection = base.getCollectionOnline();
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
        CouchBaseConfig base = new CouchBaseConfig();
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

    @GetMapping("/createBaseOnline")
    public ResponseEntity<String> greeting8() throws CouchbaseLiteException, URISyntaxException {
        String greeting = "Creando base de datos";
        CouchBaseConfig base = new CouchBaseConfig();
        try {
            // Intentar iniciar la replicación
            base.getStartedOnline();
            // Si se inicia correctamente, devolver éxito
            return new ResponseEntity<>("Base created successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción lanzada y devolver un error con detalle
            String errorMessage = "Failed to create base: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getHotelDocumentsOnline")
    public ResponseEntity<List<Map<String, Object>> > greeting9() throws CouchbaseLiteException, URISyntaxException {
        String greeting = "Obteniendo documentos";
        CouchBaseService service = new CouchBaseService();
        List<Map<String, Object>> data = service.viewDatabase("inventory","hotel");

        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
