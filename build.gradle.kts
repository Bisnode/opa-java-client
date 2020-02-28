import org.gradle.api.JavaVersion.VERSION_11

version = "0.0.1-SNAPSHOT"
group = "com.bisnode.opa"

val ossrhUsername: String by project
val ossrhPassword: String by project

plugins {
    groovy
    java
    `java-library`
    `maven-publish`
    maven
    signing
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
    withJavadocJar()
    withSourcesJar()
}
dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")
    implementation("org.slf4j:slf4j-api:1.7.30")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.7")
    testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.26.0")
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
    sign(publishing.publications["mavenJava"])
}
