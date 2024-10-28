package com.huntercodexs.quickjson.core;

import static com.huntercodexs.quickjson.core.QuickJsonAbstract.*;

public class QuickJsonExtractor {

    private int position;
    private int length;
    private int total;

    private int jsonOpenCounter = 0;
    private int arrayOpenCounter = 0;

    private String json = "";
    private String ch4r = "";
    private String prevChar = "";
    private StringBuilder result = new StringBuilder();

    public QuickJsonExtractor() {
    }

    private void reset() {
        this.json = "";
        this.ch4r = "";
        this.prevChar = "";
        this.jsonOpenCounter = 0;
        this.arrayOpenCounter = 0;
        this.result = new StringBuilder();
    }

    private boolean jsonOk(String json, String field) {
        return json != null && !json.isEmpty() && json.matches("^.*(\"" + field + "\":).*$");
    }

    private boolean fieldOk(String field) {
        return field != null && !field.isEmpty();
    }

    private String jsonFilter(String json, String field) {
        return json
                .replaceAll("\n", "")
                .replaceAll("\r", "")
                .replaceAll("\t", "")

                .replaceAll("\", ([0-9])", "\",$1")
                .replaceAll("([0-9]), \"", "$1,\"")

                .replaceAll("\", \"", "\",\"")
                .replaceAll("\", \\[", "\",[")
                .replaceAll("\", \\{", "\",{")

                .replaceAll("\": \"", "\":\"")
                .replaceAll("\": \\[", "\":[")
                .replaceAll("\": \\{", "\":{")

                .replaceAll("], \"", "],\"")
                .replaceAll("], \\[", "],[")
                .replaceAll("], \\{", "],{")

                .replaceAll("}, \"", "},\"")
                .replaceAll("}, \\[", "},[")
                .replaceAll("}, \\{", "},{")

                .replaceAll(JSON_FIELD_REGEXP[0].replaceFirst(TARGET, field), JSON_FIELD_REGEXP[1]);
    }

    private void arrayExtract() {

        for (int i = this.total; i < this.json.length(); i++) {

            this.ch4r = String.valueOf(json.charAt(i));

            if (this.ch4r.equals("[") && !this.prevChar.equals("\\")) {
                this.arrayOpenCounter += 1;
            } else if (this.ch4r.equals("]") && !this.prevChar.equals("\\")) {
                this.arrayOpenCounter -= 1;
            }

            if (this.arrayOpenCounter == 0) {
                this.result.append(this.json.charAt(i));
                break;
            }

            this.result.append(this.json.charAt(i));
            this.prevChar = this.ch4r;

        }

        if (this.arrayOpenCounter > 0) {
            throw new RuntimeException("Critical Error - Invalid Array: " + this.result);
        }

    }

    private void jsonExtract() {

        for (int i = this.total; i < this.json.length(); i++) {

            this.ch4r = String.valueOf(this.json.charAt(i));

            if (this.ch4r.equals("{") && !this.prevChar.equals("\\")) {
                this.jsonOpenCounter += 1;
            } else if (this.ch4r.equals("}") && !this.prevChar.equals("\\")) {
                this.jsonOpenCounter -= 1;
            }

            if (this.jsonOpenCounter == 0) {
                this.result.append(this.json.charAt(i));
                break;
            }

            this.result.append(this.json.charAt(i));
            this.prevChar = this.ch4r;

        }

        if (this.arrayOpenCounter > 0) {
            throw new RuntimeException("Critical Error - Invalid JSON: " + this.result);
        }

    }

    private void strExtract() {

        for (int i = this.total; i < this.json.length(); i++) {

            this.ch4r = String.valueOf(this.json.charAt(i));

            if (i > this.total && this.ch4r.equals("\"") && !this.prevChar.equals("\\")) {
                break;
            }

            if (!this.ch4r.equals("\"") && !this.ch4r.equals("\\") || this.prevChar.equals("\\")) {
                this.result.append(this.json.charAt(i));
            }

            this.prevChar = this.ch4r;

        }

    }

    private void intExtract() {

        for (int i = this.total; i < this.json.length(); i++) {

            this.ch4r = String.valueOf(this.json.charAt(i));

            if (!this.ch4r.matches("^[0-9]$")) {
                break;
            }

            this.result.append(this.json.charAt(i));

        }

    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">standardExtraction</p>
     *
     * <p style="color: #CDCDCD">Retrieve the value from on specific json</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     QuickJsonExtractor qjExtractor = new QuickJsonExtractor();
     *
     *     HashMap&lt;String, Object&gt; map = new HashMap&lt;&gt;();
     *     map.put("map1", "Map 1 Value Test");
     *     map.put("map2", 345);
     *     map.put("map3", Arrays.asList("Array 1", "Array 2", 222, "Array 3"));
     *
     *     qj.setStdoutOn(false);
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("fullname", "John Smith Viz");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
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
     *     String result = qj.json();
     *
     *     Object extract = qjExtractor.standardExtraction(result, "name");
     *     extract = qjExtractor.standardExtraction(result, "lastname");
     *     extract = qjExtractor.standardExtraction(result, "fullname");
     *     extract = qjExtractor.standardExtraction(result, "age");
     *     extract = qjExtractor.standardExtraction(result, "address");
     *     extract = qjExtractor.standardExtraction(result, "contacts");
     *     extract = qjExtractor.standardExtraction(result, "reference");
     *     extract = qjExtractor.standardExtraction(result, "family");
     *     extract = qjExtractor.standardExtraction(result, "map");
     *
     * }
     * </pre></blockquote>
     *
     * @param jsonObj (String)
     * @param field (String)
     * @return Object (Extracted Value from JSON Data)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public Object standardExtraction(Object jsonObj, String field) {

        String[] split;
        String json = String.valueOf(jsonObj);

        String subArray1Extract = json
                .replaceFirst(SUB_ARRAY1_REGEXP[0].replaceFirst(TARGET, field), SUB_ARRAY1_REGEXP[1]);

        split = subArray1Extract.split("\\{@EXTRACT}");

        if (split.length > 1 && !split[1].isEmpty()) {
            return split[1];
        }

        String subArrayExtract = json
                .replaceFirst(SUB_ARRAY2_REGEXP[0].replaceFirst(TARGET, field), SUB_ARRAY2_REGEXP[1]);

        split = subArrayExtract.split("\\{@EXTRACT}");

        if (split.length > 1 && !split[1].isEmpty()) {
            return split[1];
        }

        String jsonExtract = json
                .replaceFirst(JSON_REGEXP[0].replaceFirst(TARGET, field), JSON_REGEXP[1]);

        split = jsonExtract.split("\\{@EXTRACT}");

        if (split.length > 1 && !split[1].isEmpty()) {
            return split[1].replaceFirst("(},.*)", "}");
        }

        String arrayExtract = json
                .replaceFirst(ARRAY_REGEXP[0].replaceFirst(TARGET, field), ARRAY_REGEXP[1]);

        split = arrayExtract.split("\\{@EXTRACT}");

        if (split.length > 1 && !split[1].isEmpty()) {
            return split[1];
        }

        String dataExtract = json.replaceFirst(STR_REGEXP[0].replaceFirst(TARGET, field), STR_REGEXP[1]);

        split = dataExtract.split("\\{@EXTRACT}");

        if (split.length > 1) {
            return split[1]
                    .replaceFirst("}$", "")
                    .replaceFirst("^\\{", "")
                    .replaceFirst("(\".*)", "")
                    .trim();
        }

        return null;

    }

    /**
     * <p style="color: #FFFF00; font-size: 11px">smartExtraction</p>
     *
     * <p style="color: #CDCDCD">Retrieve the value from on specific json using advanced method</p>
     *
     * <p>Example</p>
     *
     * <blockquote><pre>
     * public void test() {
     *     QuickJson qj = new QuickJson();
     *     QuickJsonExtractor qjExtractor = new QuickJsonExtractor();
     *
     *     HashMap&lt;String, Object&gt; map = new HashMap&lt;&gt;();
     *     map.put("map1", "Map 1 Value Test");
     *     map.put("map2", 345);
     *     map.put("map3", Arrays.asList("Array 1", "Array 2", 222, "Array 3"));
     *
     *     qj.setStdoutOn(false);
     *     qj.add("name", "John");
     *     qj.add("lastname", "Smith");
     *     qj.add("fullname", "John Smith Viz \\\"Don\\\"");
     *     qj.add("age", 35);
     *     qj.add("address", Arrays.asList("Street 1", "200", "New York City"));
     *     qj.add("contacts", Arrays.asList("12345678", "98789789", "12424242"));
     *     qj.add("reference", "{\"parental\":\"mother\",\"name\":\"Sarah Wiz\",\"alias\":\"mom\"}");
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
     *     String result = qj.json();
     *     System.out.println(result);
     *
     *     Object extract;
     *
     *     extract = qjExtractor.smartExtraction(result, "notExist");
     *     extract = qjExtractor.smartExtraction(result, "age");
     *     extract = qjExtractor.smartExtraction(result, "name");
     *     extract = qjExtractor.smartExtraction(result, "lastname");
     *     extract = qjExtractor.smartExtraction(result, "fullname");
     *     extract = qjExtractor.smartExtraction(result, "address");
     *     extract = qjExtractor.smartExtraction(result, "contacts");
     *     extract = qjExtractor.smartExtraction(result, "reference");
     *     extract = qjExtractor.smartExtraction(result, "family");
     *     extract = qjExtractor.smartExtraction(result, "map");
     *
     * }
     * </pre></blockquote>
     *
     * @param jsonObj (String)
     * @param field (String)
     * @return Object (Extracted Value from JSON Data)
     * @see <a href="https://github.com/huntercodexs/quickjson">Quick JSON (GitHub)</a>
     * @author huntercodexs (powered by jereelton-devel)
     * */
    public Object smartExtraction(Object jsonObj, String field) {

        this.reset();

        String json = String.valueOf(jsonObj);

        if (!jsonOk(json, field) || !fieldOk(field)) return "";

        this.json = jsonFilter(json, field);
        this.position = this.json.indexOf("\""+field+"\":");
        this.length = ("\""+field+"\":").length();

        /*
         * When not is primary field in the JSON or String, for example:
         * Suppose that you are trying to retrieve one field called "name", and the JSON String is:
         *
         * {"person":{"name":"Mary Viz"}}
         *
         * According to the string above the field "name" is not a primary field, so you need to get
         * the value from "person" field firstly and so get the "name" inside the value from "person"
         */
        if (this.position > 1 && String.valueOf(this.json.charAt(this.position -1)).equals("{")) {
            this.position = this.json.indexOf(",\""+field+"\":");
            this.length = (",\""+field+"\":").length();
        }

        this.total = this.position + this.length;
        this.ch4r = String.valueOf(this.json.charAt(this.total));

        //Array
        if (this.ch4r.equals("[")) {
            arrayExtract();
        }
        //JSON
        else if (this.ch4r.equals("{")) {
            jsonExtract();
        }
        //String
        else if (this.ch4r.equals("\"")) {
            strExtract();
        }
        //Integer
        else if (this.ch4r.matches("^[0-9]$")) {
            intExtract();
        }

        return String.valueOf(this.result);
    }

}
