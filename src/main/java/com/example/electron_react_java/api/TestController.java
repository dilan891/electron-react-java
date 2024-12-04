package com.example.electron_react_java.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.electron_react_java.services.TestService;

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
}
