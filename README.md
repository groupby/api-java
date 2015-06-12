GroupBy Search API
========

This project uses a custom version of the maven lifecycle, please follow the steps carefully to ensure a
successful build.

### To install:

    mvn
    
This will by default run the goals `clean` and `install`.


### To test:

    mvn -Punit
    
This will install the project and run all unit tests. By default, tests are not run.


### To add a dependency to this project:
The uber jar must be used to ensure shaded dependencies are included correctly.

#### Maven

```xml
<dependency>
  <groupId>com.groupbyinc</groupId>
  <artifactId>api-java-flux</artifactId>
  <version>2.0.160</version>
  <classifier>uber</classifier>
</dependency>
```

#### Gradle

```gradle
compile group: 'com.groupbyinc', name: 'api-java-flux', version: '2.0.160', classifier: 'uber'
```

### Examples

#### Searching

```java
CloudBridge bridge = new CloudBridge("<client key>", "myCustomerId");
Query query = new Query().setQuery("dvd");
Results results = bridge.search(query);
```
