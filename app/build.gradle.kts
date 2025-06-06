/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.14/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.google.code.gson:gson:2.10.1")


    // This dependency is used by the application.
    implementation(libs.guava)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// In your application plugin configuration or run task
application {
    mainClass.set("org.example.App")
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
// Or if you have a custom JavaExec task:
tasks.register<JavaExec>("myCustomRun") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("org.example.App")
    standardInput = System.`in` // This is the crucial line
}