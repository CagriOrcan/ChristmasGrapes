
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    tasks.create("testClasses")
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
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
    
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat.v161)
            implementation("com.google.android.material:material:1.11.0")
            implementation(compose.components.uiToolingPreview)
            implementation(libs.ktor.client.android)
            implementation(libs.android.driver)
            implementation(libs.ktor.client.android.v237)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.openai.client)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.gotrue.kt)
            implementation(libs.postgrest.kt)
            implementation(libs.ktor.client.core.v237)
            implementation(libs.kotlinx.coroutines.core.v173)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.kotlinx.coroutines.core)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.navigation.compose)

            implementation(libs.openai.client.v382)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.native.driver)
        }
    }
}

android {
    namespace = "org.lamysia.christmasgrapes"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.lamysia.christmasgrapes"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("WishDatabase") {
            packageName.set("org.lamysia.christmasgrapes.db")
        }
    }
}


dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.firebase.database.ktx)
    implementation(libs.transport.runtime)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.lamysia.christmasgrapes.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.lamysia.christmasgrapes"
            packageVersion = "1.0.0"
        }
    }
}
