plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

val minestomVersion: String by project

group = "pink.zak.minestom"
version = "0.1.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://repo.spongepowered.org/maven") }
}

dependencies {
    implementation("com.github.Minestom:Minestom:$minestomVersion")
    //implementation("net.minestom.server:Minestom:1.0")

    // Configs
    implementation("com.typesafe:config:1.4.1")

    implementation("org.projectlombok:lombok:1.18.18")
    annotationProcessor("org.projectlombok:lombok:1.18.18")
}

tasks.processResources {
    expand("version" to project.version)
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    manifest {
        attributes["Main-Class"] = "run.ExtensionLauncher"
    }
}