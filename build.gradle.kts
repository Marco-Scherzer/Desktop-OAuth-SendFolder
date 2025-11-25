plugins {
    id("java")
    id("application")
    id("com.github.ben-manes.versions") version "0.51.0"
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


    implementation("com.formdev:flatlaf:3.4")
    implementation("com.formdev:flatlaf-extras:3.4")
    implementation("com.formdev:flatlaf-intellij-themes:3.4")
    implementation("com.formdev:flatlaf-swingx:3.4")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

dependencies {
    // Basis / Identity
    implementation("com.google.apis:google-api-services-oauth2:_")

    // Produktivität
    implementation("com.google.apis:google-api-services-gmail:_")
    implementation("com.google.apis:google-api-services-drive:_")
    implementation("com.google.apis:google-api-services-calendar:_")
    implementation("com.google.apis:google-api-services-docs:_")
    implementation("com.google.apis:google-api-services-sheets:_")
    implementation("com.google.apis:google-api-services-slides:_")
    implementation("com.google.apis:google-api-services-keep:_")

    // Kommunikation
    implementation("com.google.apis:google-api-services-people:_")
    implementation("com.google.apis:google-api-services-tasks:_")
    implementation("com.google.apis:google-api-services-classroom:_")
    implementation("com.google.apis:google-api-services-chat:_")

    // Video / Medien
    implementation("com.google.apis:google-api-services-youtube:_")
    implementation("com.google.apis:google-api-services-youtubeAnalytics:_")
    implementation("com.google.apis:google-api-services-photoslibrary:_")

    // Analytics
    implementation("com.google.apis:google-api-services-analytics:_")

    // Cloud Platform
    implementation("com.google.apis:google-api-services-cloudresourcemanager:_")
    implementation("com.google.apis:google-api-services-bigquery:_")
    implementation("com.google.apis:google-api-services-storage:_")
    implementation("com.google.apis:google-api-services-vision:_")
    implementation("com.google.apis:google-api-services-translate:_")
    implementation("com.google.apis:google-api-services-pubsub:_")
    implementation("com.google.apis:google-api-services-spanner:_")
    implementation("com.google.apis:google-api-services-sqladmin:_")
    implementation("com.google.apis:google-api-services-firestore:_")
    implementation("com.google.apis:google-api-services-logging:_")
    implementation("com.google.apis:google-api-services-monitoring:_")
    implementation("com.google.apis:google-api-services-cloudkms:_")
    implementation("com.google.apis:google-api-services-cloudiot:_")
    implementation("com.google.apis:google-api-services-cloudfunctions:_")
    implementation("com.google.apis:google-api-services-run:_")
    implementation("com.google.apis:google-api-services-container:_")
    implementation("com.google.apis:google-api-services-deploymentmanager:_")
    implementation("com.google.apis:google-api-services-servicenetworking:_")
    implementation("com.google.apis:google-api-services-cloudidentity:_")
    implementation("com.google.apis:google-api-services-iam:_")
    implementation("com.google.apis:google-api-services-ml:_")
    implementation("com.google.apis:google-api-services-dialogflow:_")
    implementation("com.google.apis:google-api-services-apigee:_")
    implementation("com.google.apis:google-api-services-cloudbilling:_")

    // Ads / Monetarisierung
    implementation("com.google.apis:google-api-services-adsense:_")
    implementation("com.google.apis:google-api-services-adexchangebuyer:_")
    implementation("com.google.apis:google-api-services-adexchangeseller:_")
    implementation("com.google.apis:google-api-services-displayvideo:_")
    implementation("com.google.apis:google-api-services-androidpublisher:_")
    implementation("com.google.apis:google-api-services-businessprofileperformance:_")


    // Weitere APIs
    implementation("com.google.apis:google-api-services-admin-directory:_")
    implementation("com.google.apis:google-api-services-admin-reports:_")
    implementation("com.google.apis:google-api-services-playdeveloperreporting:_")
    implementation("com.google.apis:google-api-services-playcustomapp:_")
    implementation("com.google.apis:google-api-services-fitness:_")
    implementation("com.google.apis:google-api-services-tagmanager:_")
    implementation("com.google.apis:google-api-services-webmasters:_")
}


application {
    mainClass.set("com.marcoscherzer.msimplegooglemailer.MSimpleConsoleGoogleMailer")
}

tasks.test {
    useJUnitPlatform()
}


