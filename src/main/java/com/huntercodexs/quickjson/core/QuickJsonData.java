package com.huntercodexs.quickjson.core;

import java.util.HashMap;

public class QuickJsonData {

    private String jsonFinal;
    private boolean stdoutOn;
    private boolean strictMode;
    private HashMap<String, Object> dataToJson;

    public QuickJsonData() {
        this.stdoutOn = false;
        this.strictMode = false;
        this.dataToJson = new HashMap<>();
    }

    public String getJsonFinal() {
        return this.jsonFinal;
    }

    public void setJsonFinal(String jsonFinal) {
        this.jsonFinal = jsonFinal;
    }

    public boolean isStdoutOn() {
        return this.stdoutOn;
    }

    public void setStdoutOn(boolean stdoutOn) {
        this.stdoutOn = stdoutOn;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public HashMap<String, Object> getDataToJson() {
        return this.dataToJson;
    }

    public void setDataToJson(HashMap<String, Object> dataToJson) {
        this.dataToJson = dataToJson;
    }

    public void add(String field, Object value) {
        this.dataToJson.putIfAbsent(field, value);
    }

    public void update(String field, Object value) {
        this.dataToJson.put(field, value);
    }

    public void remove(String field) {
        this.dataToJson.remove(field);
    }

    public void clear() {
        this.dataToJson.clear();
    }

    public Object get(String field) {
        return this.dataToJson.get(field);
    }


}
