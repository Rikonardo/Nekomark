import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.rikonardo.nekomark"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

task("buildProperties") {
    this.temporaryDir.mkdirs()
    val propsFile = this.temporaryDir.resolve("build.properties")
    val buildProps = org.jetbrains.kotlin.konan.properties.Properties()
    buildProps.setProperty("version", project.version as String)
    buildProps.store(propsFile.outputStream(), null)
}

tasks.jar {
    dependsOn("buildProperties")
    from(project.tasks.getByName("buildProperties").temporaryDir) {
        include("build.properties").into("com/rikonardo/nekomark")
    }
}

tasks.shadowJar {
    dependsOn("buildProperties")
    from(project.tasks.getByName("buildProperties").temporaryDir) {
        include("build.properties").into("com/rikonardo/nekomark")
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("com.rikonardo.nekomark.launcher.LauncherMainKt")
}
