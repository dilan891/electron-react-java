package com.example.electron_react_java;

import com.couchbase.lite.CouchbaseLiteException;
import com.example.electron_react_java.services.CouchBaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;

@SpringBootApplication
public class ElectronReactJavaApplication {

	public static void main(String[] args) throws URISyntaxException, CouchbaseLiteException {
		//init couchbase
		var base = new CouchBaseConfig();
		//base.getStarted();
		SpringApplication.run(ElectronReactJavaApplication.class, args);
	}

}
