plugins {
    kotlin("jvm") version "1.3.61"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val ktorVersion = "1.2.6"

repositories {
    mavenCentral()
    // Maven central doesn't contain atomicfu-common:0.14.1.
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api("io.ktor:ktor-server-netty:$ktorVersion")
    api("io.ktor:ktor-client-okhttp:$ktorVersion")
    api("io.ktor:ktor-client-cio:$ktorVersion")
    api("io.ktor:ktor-client-android:$ktorVersion")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}