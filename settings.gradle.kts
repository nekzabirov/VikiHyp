pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/nekzabirov/KMM-Compose-Navigation")
            credentials {
                username = "nekzabirov"
                password = "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"
            }
        }
        maven {
            url = uri("https://repo.repsy.io/mvn/chrynan/public")
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            url = uri("https://maven.pkg.github.com/nekzabirov/KMM-Compose-Navigation")
            credentials {
                username = "nekzabirov"
                password = "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"
            }
        }
        maven {
            url = uri("https://repo.repsy.io/mvn/chrynan/public")
        }
    }
}

rootProject.name = "VikiHyp"
include(":shared")
include(":androidApp")