# opa-java-client
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.bisnode.opa/opa-java-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.bisnode.opa/opa-java-client) ![build](https://github.com/Bisnode/opa-java-client/workflows/build/badge.svg)

OPA java client is a wrapper for [OPA REST API](https://www.openpolicyagent.org/docs/latest/rest-api/). The goal was to create client that is lightweight and framework independent. It's built for current Bisnode needs, including:
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
Our library is using Jackson for (de)serialization so, objects that you are passing/retrieving using this client should have either proper Jackson-friendly configuration or - the solution working in most cases - getters and setters for fields you want to pass/retrieve to/from OPA. 
 [More information about Jackson](https://github.com/FasterXML/jackson-docs).

### Query for document
```java
OpaQueryApi client = OpaClient.builder()
                        .opaConfiguration("http://localhost:8181")
                        .build();

DesiredResponse response = client.queryForDocument(new QueryForDocumentRequest(yourDTO, "path/to/document"), DesiredResponse.class);

// Do whatever you like with the response
```

### Setting Headers

You can add headers that will be included in all requests to the OPA server:

```java
// Add a single header
OpaClient client = OpaClient.builder()
                     .opaConfiguration("http://localhost:8181")
                     .header("X-API-Key", "your-api-key")
                     .header("Authorization", "Bearer your-token")
                     .build();

// Add multiple headers at once
Map<String, String> headers = new HashMap<>();
headers.put("X-API-Key", "your-api-key");
headers.put("X-Tenant-ID", "tenant-123");
headers.put("Authorization", "Bearer your-token");

OpaClient client = OpaClient.builder()
                     .opaConfiguration("http://localhost:8181")
                     .headers(headers)
                     .build();

// You can also combine both approaches
OpaClient client = OpaClient.builder()
                     .opaConfiguration("http://localhost:8181")
                     .headers(headers)
                     .header("X-Request-ID", "req-456")
                     .build();
```

### Query for a list of documents

This requires [commons-lang3](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3) to be present on your classpath.

```java
OpaQueryApi client = OpaClient.builder()
        .opaConfiguration("http://localhost:8181")
        .build();
        
ParameterizedType type = TypeUtils.parameterize(List.class, DesiredResponse.class);
        
List<DesiredResponse> response = client.queryForDocument(new QueryForDocumentRequest(yourDTO, "path/to/document"), type);

// Do whatever you like with the response
```

####Example
Example project is in `examples/query-for-document` directory.
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

`OpaServerConnectionException` is thrown when connection problems with OPA server occur.

### Interface segregation
Every OPA (Data, Policy, Query) API has it's own interface. So, for example, if you want to use client only for querying, you can use `OpaQueryApi` as projection. Thanks to that, in your code there will be exposed only methods that are needed by you, while not allowing to mess with policies and data.

Available interface projections:
- `OpaQueryApi`
- `OpaPolicyApi`
- `OpaDataApi`

## Developing and building
Build process and dependency management is done using Gradle.
Tests are written in spock.

## Contribution

Interested in contributing? Please, start by reading [this document](https://github.com/Bisnode/opa-java-client/blob/master/CONTRIBUTING.md).
