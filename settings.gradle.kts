pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/nekzabirov/Firebase_KMM")
            credentials {
                username = "nekzabirov"
                password = "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"
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/nekzabirov/Firebase_KMM")
            credentials {
                username = "nekzabirov"
                password = "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"
            }
        }
    }
}

rootProject.name = "VikiHyp"
include(":shared")
include(":androidApp")