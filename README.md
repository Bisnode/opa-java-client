# opa-java-client
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bisnode.opa/opa-java-client/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.bisnode.opa/opa-java-client)

OPA java client is a wrapper for OPA rest API. The goal was to create client that is lightweight and framework independent. It's built for current Bisnode needs, including:
 - creating documents
 - creating policies
 - querying for documents
## Installation
**Prerequisites:** Java 11 or higher

Add library using maven:
```xml
<dependency>
    <groupId>com.bisnode.opa</groupId>
    <artifactId>opa-java-client</artifactId>
    <version>{version}</version>
</dependency>
```
or Gradle
```groovy
implementation 'com.bisnode.opa:opa-java-client:{version}'
```

## Usage

### Query for document
```java
        OpaQueryApi client = OpaClient.builder()
                                .opaConfiguration("http://localhost:8181")
                                .build();

        DesiredResponse response = client.queryForDocument(new QueryForDocumentRequest(yourDTO, "path/to/document"), DesiredResponse.class);
        
        // Do whatever you like with the response
```
### Create policy
```java
        OpaPolicyApi client = OpaClient.builder()
                                     .opaConfiguration("http://localhost:8181")
                                     .build();

        void createOrUpdatePolicy(new OpaPolicy("your_policy_id", "content of the policy"));
```
### Create document
```java
        OpaDataApi client = OpaClient.builder()
                                     .opaConfiguration("http://localhost:8181")
                                     .build();

        void createOrOverwriteDocument(new OpaDocument("path/to/document", "content of document (json)"));
```

### Error handling
Error handling is done via exceptions. This means that if any error occurs, runtime exception which is subclass of `OpaClientException` is thrown. For now, there is simple error message returned.

### Interface segregation
Every OPA (Data, Policy, Query) API has it's own interface. So, for example, if you want to use client only for querying, you can use `OpaQueryApi` as projection. Thanks to that, in your code there will be exposed only methods that are needed by you, while not allowing to mess with policies and data.

Available interface projections:
- `OpaQueryApi`
- `OpaPolicyApi`
- `OpaDataApi`

## Developing and building
Library is using Lombok, so if you are using Intellij Idea, it's recommended to use Lombok Intellij plugin. 
Build process and dependency management is done using Gradle.
Tests are written in spock.



