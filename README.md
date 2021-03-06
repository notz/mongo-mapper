# mongo-mapper [![Build Status](https://travis-ci.org/dozd/mongo-mapper.svg?branch=master)](https://travis-ci.org/dozd/mongo-mapper)

Mapping POJO for MongoDB has not been easier. Thanks to the new codecs feature in [MongoDB Java 3.0 driver](https://www.mongodb.com/blog/post/introducing-30-java-driver).
Simply mark your entities with annotation, create `EntityCodec` and that's it! Then use standard methods for storing and accessing data from MongoDB.

## Why us it?
- Simple and easy to use.
- Use standard (MongoDB) way for object manipulation.
- Works for synchronous as well as asynchronous version of MongoDB Java Driver.
- You can extend with your [own custom codecs](#custom-codecs).
- It's fast and small - only 13kB dependency covered by unit and integration tests.

## Installation

Mongo mapper is on Maven Central. Add following into your `pom.xml`.

##### Maven

```
<dependency>
    <groupId>eu.dozd</groupId>
    <artifactId>mongo-mapper</artifactId>
    <version>1.x.x</version>
</dependency>
```

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/eu.dozd/mongo-mapper/badge.svg?style=flat)](http://mvnrepository.com/artifact/eu.dozd/mongo-mapper)

##### Gradle

```
compile 'eu.dozd:mongo-mapper:1.x.x'
```

## Usage
1. Annotate your entities with `Entity`. Make sure every entity has exactly one String annotated with `Id`. All properties must have
correct getter and setter methods according Java Bean specification.

    ```java
    import eu.dozd.mongo.annotation.Entity;
    import eu.dozd.mongo.annotation.Id;
    
    @Entity
    public class Person {
        @Id
        String id;
        String name;
        int age;
    
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
    }
    ```

2. Create mapper `CodecProvider` by calling `MongoMapper.getProviders`.
    ```java
    CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
    ```

    - Usage for standard driver:
    
        ```java
            MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(codecRegistry).build();
        
            MongoClient client = new MongoClient(new ServerAddress("localhost", 27017), settings);
        ```
    
    - Usage for asynchronous driver:
    
        ```java
            ClusterSettings clusterSettings = ClusterSettings.builder().hosts(Arrays.asList(new ServerAddress("localhost", 27017))).build();
            MongoClientSettings settings = MongoClientSettings.builder().codecRegistry(codecRegistry)
                                                .clusterSettings(clusterSettings).build();
            
            MongoClient client = MongoClients.create(settings);
        ```
        
3. Access and store data like normal POJO.

    ```java
        MongoCollection<Person> collection = db.getCollection("persons", Person.class);
    
        Person person = new Person();
        person.setName("Foo Bar");
    
        // Store person normally.
        collection.insertOne(person);
    
        // Access data.
        Person person2 = collection.find.first()
    ```

## Features
- Entity reference - make sure all entities classes are annotated with `Entity`.
- Embedded entities - entities annotated with `Embedded` does not need to have an ID. 
- @java.beans.Transient - annotated getter with it.
- Feel free to create issue or pull request if you missing some functionality.

## Custom codecs
- You can create other Codecs for you special classes.
- Added [`BigDecimalCodec`](https://github.com/dozd/mongo-mapper/tree/master/src/main/java/eu/dozd/mongo/codecs/bigdecimal/BigDecimalCodec.java) 
and [`BigDecimalCodecProvider`](https://github.com/dozd/mongo-mapper/tree/master/src/main/java/eu/dozd/mongo/codecs/bigdecimal/BigDecimalCodecProvider.java) as an example.
- Don't forget to call `MongoMapper.addProvider(yourCustomCodecProvider)`.


## Eclipse
Eclipse uses its own Java compiler which is not strictly standard compliant and requires extra configuration.
In Java Compiler -> Annotation Processing -> Factory Path you need to add ClassIndex jar file.
See the [screenshot](https://github.com/atteo/classindex/issues/5#issuecomment-15365420).

## Licence
Copyright 2016 Zdenek Dolezal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## See also
- [MongoDB Java driver documentation](http://mongodb.github.io/mongo-java-driver/3.1/)
- [Codec and CodecRegistry](http://mongodb.github.io/mongo-java-driver/3.1/bson/codecs/)
