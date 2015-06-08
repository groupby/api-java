api-java
========

Java API


This project uses a custom version of the maven lifecycle, please follow the steps carefully to ensure a
successful build.

### To install:

    mvn
    
This will by default run the goals `clean` and `install`.


### To test:

    mvn -Punit
    
This will install the project and run all unit tests. By default, tests are not run.


### To add a dependency to this project:

    <dependency>
      <groupId>com.groupbyinc</groupId>
      <artifactId>api-java-flux</artifactId>
      <version>2.0.160</version>
      <classifier>uber</classifier>
    </dependency>

The uber jar must be used to ensure shaded dependencies are included correctly.