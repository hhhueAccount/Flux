import org.gradle.kotlin.dsl.maven

dependencyResolutionManagement {
    // 配置所有子项目的maven仓库源
    @Suppress("UnstableApiUsage")
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("plugin.lombok") version "1.9.10"
        kotlin("plugin.serialization") version "2.3.0"
    }
}

plugins {
    // 快速为所有子项目下载并且应用依赖
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

include(
    "app",
    "utils",
    "network",
    "logic",
    "registry"
)

rootProject.name = "Flux"