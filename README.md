# QUICK JSON

This repository can be used to generate more easily JSON String data from a Java Object and to create a Java
Object from any JSON Data as well. See the Releases session to verify which are the versions (releases) based 
on java version.

## Pre Requisites

- Java: [ 8 | 11 | 17 | 21 ]

## Data Type Compatible

- String
- Integer
- Object
- List
- HashMap

## How to use

> NOTE: See the @Tests in the path src/test/java/com/huntercodexs/quickjson/QuickJsonTest.java in each release

- Instance the Objects

<code>
    
    QuickJson qj = new QuickJson();
	QuickJsonBuilder qjBuilder = new QuickJsonBuilder();
	QuickJsonExtractor qjExtractor = new QuickJsonExtractor();

</code>

- Create a field

<code>

    this.qj.add("name", "John");
    this.qj.print();

</code>

- Create mote than one field in a single time

<code>

    this.qj.addAll("name", "John", "lastname", "Smith");
    this.qj.print();

    this.qj.addAll("name", "Mary", "lastname", "Smith", "age");
    this.qj.print();

</code>

- Remove fields

<code>

    this.qj.add("name", "John");
    this.qj.print();

    this.qj.remove("name");
    this.qj.print();

</code>

- Clear all fields

<code>

    this.qj.addAll("name", "John", "lastname", "Smith");
    this.qj.print();

    this.qj.addAll("name", "Mary", "lastname", "Smith", "age");
    this.qj.print();

    this.qj.clear();
    this.qj.print();

</code>

- Update field

<code>

    this.qj.add("name", "John");
    this.qj.print();

    this.qj.update("name", "Mary");
    this.qj.print();

</code>

- Read one field

<code>

    this.qj.add("name", "John");
    this.qj.add("lastname", "Smith");
    this.qj.add("age", 35);

    Object result = this.qj.get("name");
    Assert.assertEquals("John", result.toString());

    result = this.qj.get("lastname");
    Assert.assertEquals("Smith", result.toString());

    result = this.qj.get("age");
    Assert.assertEquals(35, result);

</code>

- Read Object

<code>

    String result = "{\"name\":\"John\",\"age\":35,\"lastname\":\"Smith\"}"

    QuickJson qj2 = new QuickJson(result);
    Object result1 = qj2.getObject("name");
    Object result2 = qj2.getObject("lastname");
    Object result3 = qj2.getObject("age");

    Assert.assertEquals("John", result1);
    Assert.assertEquals("Smith", result2);
    Assert.assertEquals("35", result3);

</code>

- Create one JSON (using create())

<code>

    this.qj.add("name", "John");
    this.qj.add("lastname", "Smith");
    this.qj.add("age", 35);

    this.qj.create(null);

    String result = this.qj.json();
    Assert.assertEquals("{\"name\":\"John\",\"age\":35,\"lastname\":\"Smith\"}", result);

</code>

- Create one JSON (using json())

<code>

    this.qj.add("name", "John");
    this.qj.add("lastname", "Smith");
    this.qj.add("age", 35);

    String result = this.qj.json();
    Assert.assertEquals("{\"name\":\"John\",\"age\":35,\"lastname\":\"Smith\"}", result);

</code>

- Merge more than one JSON in a single JSON

<code>

    List<String> jsonList = new ArrayList<>();
    jsonList.add(json1);
    jsonList.add(json2);
    jsonList.add(json3);

    String merge = QuickJson.merge(jsonList, "employee");

</code>

- Print the JSON result in the pretty output

<code>

    String result = this.qj.prettify();

</code>

- Extract a specific data from one json field (standard)

<code>

    Object extract = this.qjExtractor.standardExtraction(jsonData, "name");

</code>

- Extract a specific data from one json field (smart)

<code>

    Object extract = this.qjExtractor.smartExtraction(jsonData, "contacts");

</code>

- Build a Java Object from one JSON Data

<code>

    QuickJsonDto build = (QuickJsonDto) this.qjBuilder.build(jsonData, QuickJsonDto.class);

</code>

- Build one JSON Data from some Java Object

<code>

    Object jsonFinal = this.qjBuilder.build(quickJsonDto);

</code>

## Releases

- https://github.com/huntercodexs/quickjson/releases/tag/r.1.21.0
- https://github.com/huntercodexs/quickjson/releases/tag/r.1.17.0
- https://github.com/huntercodexs/quickjson/releases/tag/r.1.11.0
- https://github.com/huntercodexs/quickjson/releases/tag/r.1.8.0

