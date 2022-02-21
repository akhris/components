
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
}

val exposedVersion: String by project

group = "me.user"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)
//    exposed:
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
//sqlite driver:
    implementation("org.xerial:sqlite-jdbc:3.36.0.2")

    //domain core:
    implementation("com.github.akhris:domain-core:v0.1.1")
//    implementation("com.github.akhris:domain-core:master-30588e0ac2")

    //dependency injection:
    implementation("org.kodein.di:kodein-di-framework-compose:7.10.0")

    //reflection:
//    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    //serialization:
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    //decompose:
    implementation("com.arkivanov.decompose:decompose:0.5.1")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.5.1")

    //annotation processing:
    compileOnly("com.squareup:kotlinpoet:1.10.2")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Components"
            packageVersion = "1.0.0"
        }
    }
}