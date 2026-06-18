import java.util.Properties
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    java
    id("application")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) 
    }
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("org.example.Main")
}

dependencies {
    implementation(project(":string-utils"))
    implementation("org.apache.commons:commons-lang3:3.14.0")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "org.example.Main")
    }
}

abstract class PrintInfoTask : DefaultTask() {     

    @TaskAction    
    fun print() { 
        println("======================================") 
        println("Это моя первая пользовательская задача!") 
        println("Проект: ${project.name}") 
        println("Версия Gradle: ${project.gradle.gradleVersion}") 
        println("======================================") 
    }
}

tasks.register<PrintInfoTask>("printInfo") { 
    group = "Custom" 
    description = "Выводит информацию о проекте"
}

abstract class GenerateBuildPassportTask : DefaultTask() {

    @get:Input
    abstract val gitCommitHash: Property<String>

    @OutputFile
    val outputFile = project.file("src/main/resources/build-passport.properties")

    private val counterFile = project.file("${project.rootDir}/build-counter.txt")

    @TaskAction
    fun generate() {
        val props = Properties()
        
        var buildNumber = if (counterFile.exists()) {
            counterFile.readText().trim().toIntOrNull() ?: 0
        } else {
            0
        }
        buildNumber++
        counterFile.writeText(buildNumber.toString())

        val now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        
        props.setProperty("build.user", System.getenv("USERNAME") ?: System.getenv("USER") ?: "Unknown")
        props.setProperty("build.os", System.getProperty("os.name"))
        props.setProperty("build.java.version", System.getProperty("java.version"))
        props.setProperty("build.date", now)
        props.setProperty("build.number", buildNumber.toString())
        props.setProperty("build.git.hash", gitCommitHash.get())

        outputFile.parentFile.mkdirs()
        outputFile.writer().use { props.store(it, "Build Passport Information") }
        
        println("Паспорт сборки #$buildNumber обновлен. Hash: ${gitCommitHash.get()}")
    }
}

val generateBuildInfo = tasks.register<GenerateBuildPassportTask>("generateBuildInfo") {
    group = "Custom"
    description = "Создает файл build-passport.properties с Git Hash и номером сборки"

    val hash = try {
        val process = Runtime.getRuntime().exec("git rev-parse --short HEAD")
        process.waitFor()
        process.inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) {
        "no-git-repository"
    }
    
    gitCommitHash.set(hash)
}

tasks.named("processResources") {
    dependsOn(generateBuildInfo)
}