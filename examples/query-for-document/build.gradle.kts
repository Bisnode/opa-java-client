plugins {
    groovy
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.bisnode.opa:opa-java-client:0.0.2")
    implementation("org.slf4j:slf4j-simple:1.7.30")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.13")
    testImplementation("junit:junit:4.13")
}

application {
    mainClass.set("com.bisnode.opa.XmasHappening")
}
