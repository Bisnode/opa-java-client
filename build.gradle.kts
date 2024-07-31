import org.gradle.api.JavaVersion.VERSION_11
import java.util.*

version = "0.4.4"
group = "com.bisnode.opa"

plugins {
    groovy
    java
    `java-library`
    `maven-publish`
    signing
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
    withJavadocJar()
    withSourcesJar()
}
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.slf4j:slf4j-api:2.0.11")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.10.1")
    testImplementation("org.apache.groovy:groovy:4.0.18")
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")
    testImplementation("com.github.tomakehurst:wiremock-jre8:3.0.1")
    testImplementation("net.bytebuddy:byte-buddy:1.14.14")
}

repositories {
    mavenCentral()
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
                    connection.set("scm:git:git://github.com/Bisnode/opa-java-client.git")
                    developerConnection.set("scm:git:ssh://github.com:Bisnode/opa-java-client.git")
                    url.set("https://github.com/Bisnode/opa-java-client.git")
                }
                developers {
                    developer {
                        name.set("Igor Rodzik")
                        email.set("igor.rodzik@bisnode.com")
                        organization.set("Bisnode")
                        organizationUrl.set("https://www.bisnode.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val ossrhUsername: String? by project
            val ossrhPassword: String? by project
            name = "OSSRH"
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    val decodedKey = signingKey
        ?.let { Base64.getDecoder().decode(signingKey) }
        ?.let { String(it) }
    useInMemoryPgpKeys(decodedKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
