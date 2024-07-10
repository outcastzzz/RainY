pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RainY"
include(":app")
include(":data")
include(":domain")
include(":feature:main")
include(":feature:weather")
include(":common")
include(":feature:forecast")
include(":feature:selectCity")
include(":feature:searchCity")
include(":feature:settings")
include(":feature:splash")
include(":feature:widget")
