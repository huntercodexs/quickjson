package com.huntercodexs.quickjson;

import com.huntercodexs.quickjson.core.QuickJsonData;
import com.huntercodexs.quickjson.core.QuickJsonExtractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuickJson {

    private Object jsonObject;

    QuickJsonData qjData;
    QuickJsonExtractor qjExtractor;

    public QuickJson() {
        this.qjData = new QuickJsonData();
    }

    public QuickJson(String jsonString) {
        this.jsonObject = jsonString;
        this.qjExtractor = new QuickJsonExtractor();
    }

    private static Object arrayFixToJson(Object array) {

        List<List<String>> arraySave = new ArrayList<>();

        while (true) {

            if (array.toString().contains(", [")) {

                arraySave.add(Collections.singletonList(array.toString()
                        .replaceFirst("(.+, )\\[([0-9a-zA-Z, _+.-]+)]", "$2")
                        .replaceAll(", \\{@ARRAY}", "")
                        .replaceFirst("]$", ""))
                );

                array = array.toString()
                        .replaceFirst("(.+, )\\[([0-9a-zA-Z, _+.-]+)]", "$1{@ARRAY}");

            } else {
                break;
            }
        }

        String[] splitter = array.toString()
                .replaceFirst("^\\[", "")
                .replaceFirst("]$", "")
                .split(",");

        List<Object> result = new ArrayList<>();

        int index = 0;
        for (String item : splitter) {

            String itemClear = item
                    .trim()
                    .replaceFirst("^\"", "")
                    .replaceFirst("\"$", "");

            if (itemClear.matches("^[0-9]+$")) {//Numeric
                result.add(itemClear);
            } else if (itemClear.matches("^[a-zA-Z].*$")) {//String
                result.add("\""+itemClear+"\"");
            } else if (itemClear.matches("^\\{@ARRAY}")) {//Array List
                result.add(arrayFixToJson(arraySave.get(index)));
                index+=1;
            }
        }

        return result;
    }

    private static Object hashMapFixToJson(Object hashMap) {
        String hashMapFix = hashMap.toString().replaceAll(
                "([a-zA-Z][0-9a-zA-Z-_+)(}{!@#$%&;Çç/.\\\\ ]+)([,}\\]:=])",
                "\"$1\"$2");

        hashMapFix = hashMapFix.replaceAll("(\")(=)([0-9\"\\[{])", "$1:$3");

        return hashMapFix;
    }

    private StringBuilder process() {

        StringBuilder json = new StringBuilder();

        this.qjData.getDataToJson().forEach((key, val) -> {
            json.append("\"").append(key).append("\"");
            json.append(":");

            if (val.toString().matches("^[0-9]+$")) {//Numeric
                json.append(val);
            } else if (val.toString().matches("^[a-zA-Z].*$")) {//String
                json.append("\"").append(val).append("\"");
            } else if (val.toString().matches("^(\\[]|\\{})$")) {//Array List Empty / Hash Map Empty
                json.append(val);
            } else if (val.toString().matches("^\\[.*$")) {//Array List
                json.append(arrayFixToJson(val));
            } else if (val.toString().matches("^\\{\".*$")) {//JSON Object
                json.append(val);
            } else if (val.toString().matches("^\\{[a-zA-Z][0-9a-zA-Z]+=.*$")) {//HashMap
                json.append(hashMapFixToJson(val));
            }

            json.append(",");
        });

        return json;
    }

    public void stdout(String id, Object value) {
        if (this.qjData.isStdoutOn()) {
            System.out.println("[QUICK JSON] =============[" + id + "]> " + value);
        }
    }

    public void setStdoutOn(boolean stdoutOn) {
        this.qjData.setStdoutOn(stdoutOn);
    }

    public void setStrictMode(boolean strictMode) {
        this.qjData.setStrictMode(strictMode);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">add</p>
     *
     * <p style="color: #CDCDCD">Add field for JSON creator</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @param field (String)
     * @param value (Object)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void add(String field, Object value) {
        this.qjData.add(field, value);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">addAll</p>
     *
     * <p style="color: #CDCDCD">
     *     Add all values in one single time, but pay attention in the fields and values, keep in mind that
     *     each field and value should be informed in equal quantity: "field", "value1", "field2", "value2"
     *     and so on. However, if you omitted the last value, it will automatically considered as a null or
     *     empty value as a result.
     * </p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.addAll("name", "John", "lastname", "Smith");
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @param values (Object...)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void addAll(Object... values) {
        for ( int i = 0; i < values.length; i++ ) {
            if (i*2 > values.length-1) break;
            String field = String.valueOf(values[i*2]);
            Object value;
            try {
                value = values[i*2+1];
            } catch (ArrayIndexOutOfBoundsException e) {
                value = "";
            }
            this.add(field, value);
        }
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">remove</p>
     *
     * <p style="color: #CDCDCD">Remove one element from the data structure to create a JSON String</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.remove("name");
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @param field (String)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void remove(String field) {
        this.qjData.remove(field);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">clear</p>
     *
     * <p style="color: #CDCDCD">Reset the data structure that would be used to create on JSON String data</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.clear();
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void clear() {
        this.qjData.clear();
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">update</p>
     *
     * <p style="color: #CDCDCD">Change an value of the specific field</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.update("name", "Mary");
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @param field (String)
     * @param value (Object)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void update(String field, Object value) {
        this.qjData.update(field, value);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">get</p>
     *
     * <p style="color: #CDCDCD">Retrieve one specific field value informing the field name</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     Object result = qj.get("name");
     *     System.out.println(result);
     * }
     * </pre></blockquote>
     *
     * @param field (String)
     * @return Object (Value from field)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public Object get(String field) {
        return this.qjData.get(field);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">getObject</p>
     *
     * <p style="color: #CDCDCD">Retrieve one specific field value informing the field name</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("age", 35);
     *
     *     String result = qj.json();
     *
     *     QuickJson qj2 = new QuickJson(result);
     *     Object result1 = qj2.getObject("name");
     *     Object result2 = qj2.getObject("lastname");
     *     Object result3 = qj2.getObject("age");
     *
     *     Assert.assertEquals("John", result1);
     *     Assert.assertEquals("Smith", result2);
     *     Assert.assertEquals("35", result3);
     * }
     * </pre></blockquote>
     *
     * @param field (String)
     * @return Object (Value from field)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public Object getObject(String field) {
        return this.qjExtractor.smartExtraction(this.jsonObject, field);
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">create</p>
     *
     * <p style="color: #CDCDCD">
     *     Definitive create the JSON Data string using the values populated previously
     * </p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("age", 35);
     *
     *     qj.create(null);
     *
     *     String result = qj.json();
     *     System.out.println(result);
     * }
     * </pre></blockquote>
     *
     * <p>Result</p>
     *
     * <blockquote><pre>
     * {"name":"John","age":35,"lastname":"Smith"}
     * </pre></blockquote>
     *
     * @param mainField (String)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void create(String mainField) {

        if (mainField == null || mainField.isEmpty()) {

            if (this.qjData.getDataToJson().isEmpty()) {
                this.qjData.setJsonFinal("{}");
                return;
            }

        } else {

            if (this.qjData.getDataToJson().isEmpty()) {
                this.qjData.setJsonFinal("{\""+mainField+"\":{}}");
                return;
            }
        }

        String jsonFinal = String.valueOf(process()).replaceFirst(",$", "");
        this.qjData.setJsonFinal("{"+jsonFinal+"}");

    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">json</p>
     *
     * <p style="color: #CDCDCD">Create a JSON Data string quickly</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
     *     qj.add("reference", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\"}");
     *     qj.add("family",
     *          Arrays.asList(
     *              "mother", "July Smith",
     *              "father", "Luis Smith",
     *              Arrays.asList(
     *                  "sister", "Elen Smith", "age", 22
     *              ),
     *              Arrays.asList(
     *                  "brother", "Igor Smith", "age", 24
     *              )
     *          )
     *     );
     *
     *     String result = qj.json();
     *     System.out.println(result);
     * }
     * </pre></blockquote>
     *
     * <p>Result</p>
     *
     * <blockquote><pre>
     * {"reference":{"name":"Sarah Wiz","parental":"friend"},"address":["Street 1", 200, "New York City"],"name":"John","family":["mother", "July Smith", "father", "Luis Smith", ["brother", "Igor Smith", "age", 24], ["sister", "Elen Smith", "age", 22]],"age":35,"contacts":[12345678, 98789789, 12424242],"lastname":"Smith"}
     * </pre></blockquote>
     *
     * @return String (JSON String value)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public String json() {
        if (this.qjData.getJsonFinal() == null) {
            this.create(null);
        }
        return this.qjData.getJsonFinal();
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">merge</p>
     *
     * <p style="color: #CDCDCD">Merge more than one json string in one single json</p>
     *
     * @param jsonString (List&lt;String&gt;)
     * @param mainField (String)
     * @return String (JSON String value formatted)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public static String merge(List<String> jsonString, String mainField) {
        if (mainField == null || mainField.isEmpty()) {
            mainField = "Object";
        }

        StringBuilder jsonResponse = new StringBuilder("{\"" + mainField + "\": [");

        for (String jsonItem : jsonString) {
            jsonResponse.append(jsonItem).append(",");
        }

        return (jsonResponse+"]}").replaceFirst(",]}", "]}");
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">print</p>
     *
     * <p style="color: #CDCDCD">
     *     Print the value from a data structure to known which values it will be used to create a JSON Data string
     * </p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.print();
     * }
     * </pre></blockquote>
     *
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public void print() {
        if (this.qjData.getDataToJson().isEmpty()) {
            System.out.println("Empty !");
        } else {
            this.qjData.getDataToJson().forEach((k, v) -> {
                System.out.println("\"" + k + "\":\"" + v + "\"");
            });
        }
    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">prettify</p>
     *
     * <p style="color: #CDCDCD">Retrieve the JSON Data string result formatted in a beaut style</p>
     *
     * <p>
     *     If you want to prettify the JSON response output this method is very appropriate, see the
     *     examples below to understand how to use and what kind of response you can receive
     * </p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("fullname", "John Smith Viz");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
     *     qj.add("reference", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\",\"contact\":{\"phone\":\"1234567890\",\"email\":\"john@email.com\"}}");
     *     qj.add("family",
     *             Arrays.asList(
     *                     "mother", "July Smith",
     *                     "father", "Luis Smith",
     *                     Arrays.asList(
     *                             "sister", "Elen Smith", "age", 22
     *                     ),
     *                     Arrays.asList(
     *                             "brother", "Igor Smith", "age", 24
     *                     )
     *             )
     *     );
     *
     *     String result = qj.prettify();
     *     System.out.println(result);
     * }
     * </pre></blockquote>
     *
     * <p>Result</p>
     *
     * <blockquote><pre>
     * {
     *    "reference":{
     *       "name":"Sarah Wiz",
     *       "parental":"friend",
     *       "contact":{
     *          "phone":"1234567890",
     *          "email":"john@email.com"
     *       }
     *    },
     *    "address":[
     *       "Street 1",
     *       200,
     *       "New York City"
     *    ],
     *    "name":"John",
     *    "fullname":"John Smith Viz",
     *    "family":[
     *       "mother",
     *       "July Smith",
     *       "father",
     *       "Luis Smith",
     *       [
     *          "brother",
     *          "Igor Smith",
     *          "age",
     *          24
     *       ],
     *       [
     *          "sister",
     *          "Elen Smith",
     *          "age",
     *          22
     *       ]
     *    ],
     *    "age":35,
     *    "contacts":[
     *       12345678,
     *       98789789,
     *       12424242
     *    ],
     *    "lastname":"Smith"
     * }
     * </pre></blockquote>
     *
     * @return String (JSON String value formatted)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public String prettify() {
        StringBuilder pretty = new StringBuilder();
        String json = this.json()
                .replaceAll("], ?\\[", "],[")
                .replaceAll(" ?, ", ",");
        StringBuilder tab = new StringBuilder();

        for (int i = 0; i < json.length(); i++) {

            String ch = String.valueOf(json.charAt(i));

            switch (ch) {
                case "{":
                case "[":
                    tab.append("\t");
                    pretty.append(ch).append("\n").append(tab);
                    break;
                case "}":
                case "]":
                    tab.deleteCharAt(tab.length() - 1);
                    pretty.append("\n").append(tab).append(ch);
                    break;
                case ",":
                    if (!String.valueOf(json.charAt(i+1)).equals("[")) {
                        pretty.append(ch).append("\n").append(tab);
                    } else {
                        pretty.append(ch);
                    }
                    break;
                default:
                    pretty.append(ch);
                    break;
            }

        }

        return pretty.toString();
    }
}
