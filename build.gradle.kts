plugins {
    id("java")
    id("application")
}

group = "com.marcoscherzer"
version = "0.1 preAlpha"

repositories {
    mavenCentral()
    maven {
        url = uri("https://www.googleapis.com/download/storage/maven-repositories")
    }
}

dependencies {
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("com.google.api-client:google-api-client:2.4.0")
    implementation("com.google.apis:google-api-services-gmail:v1-rev20250630-2.0.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.22.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.oauth-client:google-oauth-client-java6:1.34.1")
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    implementation("com.sun.activation:jakarta.activation:2.0.1")
    implementation("com.google.http-client:google-http-client:1.42.1")
    implementation("com.google.http-client:google-http-client-gson:1.42.1")
    implementation("commons-codec:commons-codec:1.15")
    implementation("commons-logging:commons-logging:1.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.apache.httpcomponents:httpcore:4.4.15")

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

