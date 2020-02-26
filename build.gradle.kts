import org.gradle.api.JavaVersion.VERSION_11

version = "0.0.1-SNAPSHOT"
group = "com.bisnode.opa"

plugins {
    groovy
    java
    `java-library`
    `maven-publish`
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
    withJavadocJar()
    withSourcesJar()
}
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.9")
    testImplementation(platform("org.spockframework:spock-bom:2.0-M1-groovy-2.5"))
    testImplementation("org.spockframework:spock-core")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.26.0")
}

tasks.test {
    useJUnitPlatform()
}
tasks.javadoc {
    source = sourceSets["main"].allJava
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "opa-java-client"
            from(components["java"])
            pom {
                name.set("OPA Java Client")
                description.set("Lightweight Java library for Open Policy Agent")
                url.set("https://github.com/Bisnode/opa-java-client")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                scm {
                    url.set("https://github.com/Bisnode/opa-java-client.git")
                }
            }
        }
    }
}
