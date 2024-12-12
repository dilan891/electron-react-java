package com.example.electron_react_java.models;

import java.util.HashMap;
import java.util.Map;

public class TestData {
    private String name;
    private int age;

    // Constructor vacío (obligatorio para la deserialización)
    public void MyData() {}

    public void MyData(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        map.put("type", "TestData");
        return map;
    }

    @Override
    public String toString() {
        return "MyData{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
