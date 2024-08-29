package com.example.demo.cache.db;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Repository<T> {

    static final Repository INSTANCE = new Repository();
    private static final Gson gson = new Gson();
    private static final String filePath = "../db.json";
    private Map<String, T> map = new HashMap<>();

    private Repository() {
    }

    public static Repository instance() {
        return INSTANCE;
    }


    @SneakyThrows
    public T getValue(String key) {
        if (map.keySet().size() == 0) {
            // Create a FileReader object to read the JSON file
            FileReader reader = null;
            try {
                reader = new FileReader(filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            // Parse the JSON file using JsonParser
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            map = new Gson().fromJson(new Gson().toJson(jsonObject), Map.class);
        }
        Thread.sleep(2000);
        return map.get(key);
    }

    @SneakyThrows
    public void putValue(String key, T value) {
        Thread.sleep(2000);
        map.put(key, value);
    }


}
