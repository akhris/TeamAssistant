import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val exposedVersion: String by project
val kodeinVersion: String by project
val decomposeVersion: String by project
val SQLiteDriverVersion: String by project
val realmVersion: String by project
val kolorVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("io.realm.kotlin") version "1.12.0"
}

group = "com.akhris"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)

    //dependency injection:
    implementation("org.kodein.di:kodein-di:$kodeinVersion")

    //decompose:
    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")

    //    exposed:
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    //sqlite driver:
    implementation("org.xerial:sqlite-jdbc:$SQLiteDriverVersion")

    //for qualifiers
    implementation("javax.inject:javax.inject:1")

    //for key-value storage:
    implementation("com.russhwolf:multiplatform-settings:1.1.1")
    implementation("com.russhwolf:multiplatform-settings-coroutines:1.1.1")

    //realm:
    implementation("io.realm.kotlin:library-base:1.12.0")

    //https://github.com/jordond/materialkolor
    //generation theme from a seed color
    implementation("com.materialkolor:material-kolor:$kolorVersion")

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "TeamAssistant"
            packageVersion = "1.0.0"
            val iconsRoot = project.file("src/main/resources/")
            windows {
                iconFile.set(iconsRoot.resolve("drawables/round_groups_white_48dp.ico"))
            }
            linux {
                iconFile.set(iconsRoot.resolve("drawables/round_groups_white_48dp.png"))
            }
        }
    }
}
