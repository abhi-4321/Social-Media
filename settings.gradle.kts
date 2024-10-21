pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(uri("https://repo1.maven.org/maven2"))
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(uri("https://jitpack.io"))
    }
}

rootProject.name = "Social Media"
include(":app")
