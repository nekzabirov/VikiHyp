plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("native.cocoapods")
}

@OptIn(
    org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class,
    org.jetbrains.compose.ExperimentalComposeLibrary::class
)
kotlin {
    targetHierarchy.default()

    androidTarget()
    ios()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")

        //pod("FirebaseCore") { version = "10.15.0" }
        pod("FirebaseAuth") { version = "10.15.0" }
        pod("GoogleSignIn") { version = "7.0.0" }
        pod("FBSDKLoginKit") { version = "16.1.3" }

        framework {
            baseName = "shared"
            isStatic = true
        }

        //noPodspec()
    }

    sourceSets {
        val firebaseVersion = "1.0.0"

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.material3)
                api(compose.components.resources)

                api("com.nekzabirov.firebase:firebase_auth:$firebaseVersion")

                api("io.insert-koin:koin-core:3.4.3")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("androidx.activity:activity-compose:1.8.0-beta01")

                implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
                implementation("com.google.firebase:firebase-common")
                implementation("com.google.firebase:firebase-auth-ktx")
                implementation("com.google.android.gms:play-services-auth:20.7.0")
                implementation("com.facebook.android:facebook-login:16.1.3")
                implementation("io.insert-koin:koin-android:3.3.3")
            }
        }
    }
}

android {
    namespace = "com.vikihyp.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        resources.srcDirs("src/commonMain/resources")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // New syntax (credit goes to ʍѳђઽ૯ท in comment)
    kotlin {
        jvmToolchain(17)
    }
}