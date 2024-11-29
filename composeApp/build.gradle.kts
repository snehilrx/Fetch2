import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSearialize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sql)
}

sqldelight {
    linkSqlite = true

    databases {
        create("Database") {
            packageName.set("com.otaku.fetch2")
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        all {
            languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
        val desktopMain by getting

        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            implementation(libs.androidx.multidex)
            implementation(libs.koin.annotations)
            implementation(libs.android.driver)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)
            implementation(libs.primitive.adapters)
            implementation(libs.androidx.window)
        }
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ksoup)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.androidx.paging.common)
            implementation(libs.androidx.paging.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coroutines.extensions)
            implementation(libs.landscapist.coil3)
            implementation(libs.androidx.paging3.extensions)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.tabler.icons)
            implementation(libs.voyager.tab.navigator)
            api(libs.compose.webview.multiplatform)
            api(libs.kmm.viewmodel.core)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        nativeMain.dependencies {
            implementation(libs.native.driver)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.sql.driver)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.vlcj)
            implementation(libs.ffmpeg)
        }
    }
}

android {
    namespace = "com.otaku.fetch2"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.otaku.fetch2"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.otaku.fetch2"
            packageVersion = "1.0.0"
        }
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED") // recommended but not necessary

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
        buildTypes.release.proguard {
            configurationFiles.from("compose-desktop.pro")
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", "io.insert-koin:koin-ksp-compiler:1.3.1")
    ksp(libs.koin.ksp.compiler)
}

// Ensure all Kotlin compile tasks depend on kspCommonMainKotlinMetadata for prior code processing.
tasks.withType<KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

// Make all SourcesJar tasks depend on kspCommonMainKotlinMetadata.
afterEvaluate {
    tasks.filter { task: Task ->
        task.name.contains("SourcesJar", true)
    }.forEach {
        it.dependsOn("kspCommonMainKotlinMetadata")
    }
}
