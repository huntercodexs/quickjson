package com.huntercodexs.quickjson.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.huntercodexs.quickjson.core.QuickJsonAbstract.*;

public class QuickJsonBuilder {

    private boolean strictMode;
    private final List<Object> arraySave;
    private final List<Object> jsonSave;

    QuickJson qj;
    QuickJsonExtractor qjExtract;

    public QuickJsonBuilder() {
        this.qj = new QuickJson();
        this.qjExtract = new QuickJsonExtractor();
        this.strictMode = false;
        this.arraySave = new ArrayList<>();
        this.jsonSave = new ArrayList<>();
    }

    private String hashMapArrayExtractor(String json) {

        int arrayCounter = 0;
        List<Object> arrayData = new ArrayList<>();

        while (true) {
            arrayCounter += 1;

            if (!json.matches(".*"+HASHMAP_ARRAY_REGEXP+".*")) {
                break;
            }

            arrayData.add(json.replaceFirst(HASHMAP_ARRAY_REGEXP+".*", "$1"));
            json = json.replaceFirst(HASHMAP_ARRAY_REGEXP, ":@ARRAYOBJECT"+arrayCounter+"@");
        }

        for (Object value : arrayData) {
            this.arraySave.add(value
                    .toString()
                    .replaceFirst(".*"+HASHMAP_ARRAY_REGEXP, "$1")
                    .replaceFirst(":", ""));
        }

        return json;
    }

    private String hashMapJsonExtractor(String json) {

        int jsonCounter = 0;
        List<Object> jsonData = new ArrayList<>();

        while (true) {
            jsonCounter += 1;

            if (!json.matches(".*"+HASHMAP_JSON_REGEXP+".*")) {
                break;
            }

            jsonData.add(json.replaceFirst(HASHMAP_JSON_REGEXP+".*", "$1"));
            json = json.replaceFirst(HASHMAP_JSON_REGEXP, ":@JSONOBJECT"+jsonCounter+"@");
        }

        for (Object value : jsonData) {
            this.jsonSave.add(value
                    .toString()
                    .replaceFirst(".*"+HASHMAP_JSON_REGEXP, "$1")
                    .replaceFirst(":", ""));
        }

        return json;
    }

    private HashMap<Object, Object> hashMapExtractor(Object jsonObj) {

        String json = String.valueOf(jsonObj);
        json = this.hashMapArrayExtractor(json);
        json = this.hashMapJsonExtractor(json);
        HashMap<Object, Object> hashMap = new HashMap<>();

        int jsonCounter = 1;
        int arrayCounter = 1;

        String[] jsonFields = json
                .replaceFirst("\\{", "")
                .replaceFirst("}", "")
                .split(",");

		for (String jsonField : jsonFields) {

			String[] keyValue = jsonField.split(":");

			String jf = keyValue[0]
					.trim()
					.replaceFirst("^\"", "")
					.replaceFirst("\"$", "");

			String jv = keyValue[1]
					.trim()
					.replaceFirst("^\"", "")
					.replaceFirst("\"$", "");

			if (jv.contains("@JSONOBJECT" + jsonCounter + "@")) {
				jv = jv.replaceFirst(jv, String.valueOf(jsonSave.get(jsonCounter - 1)));
				jsonCounter += 1;

			} else if (jv.contains("@ARRAYOBJECT" + arrayCounter + "@")) {
				jv = jv.replaceFirst(jv, String.valueOf(arraySave.get(arrayCounter - 1)));
				arrayCounter += 1;
			}

			hashMap.put(jf, jv);
		}

        return hashMap;
    }

    public void setStrictMode(boolean mode) {
        this.strictMode = mode;
    }

    /**
     * <h6 style="color: #FFFF00; font-size: 11px">build</h6>
     *
     * <p style="color: #CDCDCD">Convert data from any JSON String to Object</p>
     *
     * <p>IMPORTANT: This support only five type of data:</p>
     * <ul>
     *     <li>Object</li>
     *     <li>Integer</li>
     *     <li>String</li>
     *     <li>List</li>
     *     <li>HashMap</li>
     * </ul>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     QuickJsonBuilder qjBuilder = new QuickJsonBuilder();
     *
     *     HashMap<String, Object> map = new HashMap<>();
     *     map.put("map1", "Map 1 Value Test");
     *     map.put("map2", 345);
     *     map.put("map3", Arrays.asList("Array 1", "Array 2", 222, "Array 3"));
     *     map.put("map4", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\"}");
     *
     *     qj.setStrictMode(false);
     *     qj.add("type", "Person");
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("fullname", "John Smith Viz");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
     *     qj.add("numbers", Arrays.asList(1, 2, 3, 4, 5, 6));
     *     qj.add("reference", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\"}");
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
     *     qj.add("map", map);
     *
     *     String jsonResult = qj.json();
     *
     *     qjBuilder.setStrictMode(false);
     *     QuickJsonDto build = (QuickJsonDto) qjBuilder.build(jsonResult, QuickJsonDto.class);
     *
     *     System.out.println(build);
     * }
     * </pre></blockquote>
     *
     * @param jsonData (Object)
     * @param classT (Class&lt;T&gt;)
     * @return Object (Object based on Class Type argument)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public <T> T build(Object jsonData, Class<T> classT) {

        Field[] fields = classT.getDeclaredFields();

        try {

            T instanceClass = ReflectionUtils.accessibleConstructor(classT).newInstance();

            for (Field field : fields) {

                String currentField = field.getName();
                Object fieldValue = this.qjExtract.smartExtraction(jsonData, currentField);

                if (fieldValue == null && this.strictMode) {
                    throw new RuntimeException("Invalid data to mapper, field not found: " + currentField);
                }

                Field field1 = classT.getField(currentField);
                String typeF = field1.getType().toString();

                switch (typeF) {
                    case "class java.lang.Object":
                        field1.set(instanceClass, fieldValue);
                        break;

                    case "class java.lang.Integer":
                        field1.set(instanceClass, Integer.parseInt(String.valueOf(fieldValue)));
                        break;

                    case "class java.lang.String":
                        field1.set(instanceClass, String.valueOf(fieldValue));
                        break;

                    case "interface java.util.List":

                        if (fieldValue != null) {

                            String[] arr = fieldValue.toString()
                                    .replaceFirst("^\\[", "")
                                    .replaceFirst("]$", "")
                                    .split(",");

                            field1.set(instanceClass, Arrays.asList(arr));

                        } else {
                            field1.set(instanceClass, null);
                        }

                        break;

                    case "class java.util.HashMap":

                        if (fieldValue != null) {
                            field1.set(instanceClass, hashMapExtractor(fieldValue));
                        } else {
                            field1.set(instanceClass, null);
                        }
                        break;

                }
            }

            return (T) instanceClass;

        } catch (
                NoSuchMethodException |
                InvocationTargetException |
                InstantiationException |
                IllegalAccessException |
                NoSuchFieldException e
        ) {
            throw new RuntimeException(e);
        }

    }

    /**
     * <h6 style="color: #FFFF00; font-size: 11px">build</h6>
     *
     * <p style="color: #CDCDCD">Convert data from any Object to JSON String</p>
     *
     * <p>IMPORTANT: This support only five type of data:</p>
     * <ul>
     *     <li>Object</li>
     *     <li>Integer</li>
     *     <li>String</li>
     *     <li>List</li>
     *     <li>HashMap</li>
     * </ul>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     QuickJsonBuilder qjBuilder = new QuickJsonBuilder();
     *
     *     HashMap<String, Object> map = new HashMap<>();
     *     map.put("map1", "Map 1 Value Test");
     *     map.put("map2", 345);
     *     map.put("map3", Arrays.asList("Array 1", "Array 2", 222, "Array 3"));
     *     map.put("map4", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\"}");
     *
     *     qj.setStrictMode(false);
     *     qj.add("type", "Person");
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("fullname", "John Smith Viz");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
     *     qj.add("numbers", Arrays.asList(1, 2, 3, 4, 5, 6));
     *     qj.add("reference", "{\"name\":\"Sarah Wiz\",\"parental\":\"friend\"}");
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
     *     qj.add("map", map);
     *
     *     String jsonResult = qj.json();
     *
     *     qjBuilder.setStrictMode(false);
     *     QuickJsonDto build = (QuickJsonDto) qjBuilder.build(jsonResult, QuickJsonDto.class);
     *
     *     Object jsonFinal = qjBuilder.build(quickJsonDto);
     *
     *     System.out.println(jsonFinal);
     * }
     * </pre></blockquote>
     *
     * @param object (Object: Java Object to convert for JSON Object)
     * @return Object (Data Object)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public Object build(Object object) {

        Class<?> classT = object.getClass();
        Field[] fields = classT.getDeclaredFields();

        String[] packageSplit = classT.getName().split("\\.");
        String className = packageSplit[packageSplit.length-1];

        String dataJson = object.toString()
                .replaceFirst(className+"\\(", "{")
                .replaceFirst("\\)$", "}")
                .replaceAll("("+FIELD+")=", "\"$1\":")
                .replaceAll("(\""+FIELD+"\"):("+STRINGED+")", "$1:\"$2\"");

		try {

		    for (Field field : fields) {

                Field field1 = classT.getField(field.getName());
                String fieldName = field1.getName();

                Object content = this.qjExtract.smartExtraction(dataJson, fieldName);

                this.qj.add(fieldName, content);

            }

            return this.qj.json();

		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Critical Error: " + e.getMessage());
		}
    }

}
