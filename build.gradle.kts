plugins {
    id("java")
    id("application")
}

group = "com.marcoscherzer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.googleapis.com/download/storage/maven-repositories")
    }
}

dependencies {
    // JNA für native Windows-Zugriffe
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")

    // Google API Core
    implementation("com.google.api-client:google-api-client:2.4.0")

    // Gmail API Wrapper
    implementation("com.google.apis:google-api-services-gmail:v1-rev20250630-2.0.0")

    // Google Auth Library
    implementation("com.google.auth:google-auth-library-oauth2-http:1.22.0")

    // OAuth2 Flow mit Jetty
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.oauth-client:google-oauth-client-java6:1.34.1")

    // Jakarta Mail & Activation
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("com.sun.activation:jakarta.activation:2.0.1")

    // JSON & HTTP
    implementation("com.google.http-client:google-http-client:1.42.1")
    implementation("com.google.http-client:google-http-client-gson:1.42.1")

    // Apache Commons
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-logging:commons-logging:1.2")

    // Guava
    implementation("com.google.guava:guava:31.1-jre")

    // Apache HTTP
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpcore:4.4.15")

    // JUnit für Tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("com.marcoscherzer.msimplegooglemailer.MSimpleConsoleGoogleMailer")
}


tasks.test {
    useJUnitPlatform()
}
