pluginManagement {
    repositories {
        maven { url = uri("/home/hatch/maven-mirror") }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("/home/hatch/maven-mirror") }
        google()
        mavenCentral()
    }
}
rootProject.name = "orbit-browser"
include(":app")
