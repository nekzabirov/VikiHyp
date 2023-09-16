plugins {
    id("com.android.library").version("8.1.0-rc01").apply(false)
    kotlin("multiplatform").version("1.9.0").apply(false)
    id("org.jetbrains.compose").version("1.5.0").apply(false)
    kotlin("native.cocoapods").version("1.9.0").apply(false)
    kotlin("plugin.serialization").version("1.9.0").apply(false).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    subprojects {
        delete(buildDir)
    }
}