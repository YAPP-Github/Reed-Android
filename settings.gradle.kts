rootProject.name = "Booket-Android"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

include(
    ":app",

    ":core:common",
    ":core:data:api",
    ":core:data:impl",
    ":core:datastore:api",
    ":core:datastore:impl",
    ":core:designsystem",
    ":core:model",
    ":core:network",
    ":core:ui",

    ":feature:home",
    ":feature:library",
    ":feature:login",
    ":feature:main",
    ":feature:record",
    ":feature:screens",
    ":feature:search",
    ":feature:settings",
    ":feature:webview",
    ":feature:detail",
)
include(":feature:onboarding")
