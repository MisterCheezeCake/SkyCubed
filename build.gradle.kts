import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.20"
    alias(libs.plugins.loom)
    id("maven-publish")
}

base {
    archivesName.set(project.name.lowercase())
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    runs {
        getByName("client") {
            programArg("--quickPlayMultiplayer=hypixel.net")
            vmArg("-Ddevauth.enabled=true")
            vmArg("-Dskyblockapi.debug=true")
        }
    }

    mods {
        register("skycubed") {
            sourceSet("client")
        }
    }
}

repositories {
    maven(url = "https://nexus.resourcefulbees.com/repository/maven-public/")
    maven(url = "https://repo.hypixel.net/repository/Hypixel/")
    maven(url = "https://api.modrinth.com/maven")
    maven(url = "https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    mavenLocal()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.loader)
    modImplementation(libs.fabrickotlin)
    modImplementation(libs.fabric)

    modImplementation(libs.hypixelapi)
    modImplementation(libs.skyblockapi)
    modImplementation(libs.rconfig)
    modImplementation(libs.rconfigkt)
    modImplementation(libs.rlib)
    modImplementation(libs.rlibkt)
    modImplementation(libs.olympus)

    include(libs.hypixelapi)
    include(libs.skyblockapi)
    include(libs.rconfig)
    include(libs.rconfigkt)
    include(libs.rlib)
    include(libs.rlibkt)
    include(libs.olympus)

    modRuntimeOnly(libs.devauth)
    modRuntimeOnly(libs.modmenu)
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching(listOf("fabric.mod.json")) {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true

        excludeDirs.add(file("run"))
    }
}