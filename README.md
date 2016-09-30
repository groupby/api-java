GroupBy Search API
========
 
![release](https://img.shields.io/maven-central/v/com.groupbyinc/api-java-flux.svg)
![license](https://img.shields.io/github/license/groupby/api-java.svg)

Please follow the steps carefully to ensure a successful build.

### To install:

    mvn
    
This will by default run the goals `clean` and `install`.


### To test:

    mvn -Punit
    
This will install the project and run all unit tests. By default, tests are not run.


### Add this library as a dependency to your project:
The Uber JAR must be used to ensure shaded dependencies are included correctly.

#### Maven

```xml
<dependency>
  <groupId>com.groupbyinc</groupId>
  <artifactId>api-java-flux</artifactId>
  <version>VERSION</version>
  <classifier>uber</classifier>
</dependency>
```

#### Gradle

```gradle
compile group: 'com.groupbyinc', name: 'api-java-flux', version: 'VERSION', classifier: 'uber'
```

### Examples

#### Searching

```java
CloudBridge bridge = new CloudBridge("<client-key>", "<customer-id>");
Query query = new Query().setQuery("dvd");
Results results = bridge.search(query);
```
