plugins {
    // 应用公用的构建脚本逻辑
    // 每一个要使用公用逻辑的子项目都会加这么一行
    // 源文件在：buildSrc/src/main/kotlin/common-logic.gradle.kts
    id("buildsrc.convention.common-logic")

    kotlin("jvm")
    kotlin("plugin.lombok")
    kotlin("plugin.serialization")
    alias(libs.plugins.freefair)
}

group = "cn.zc"
version = "1.0"

dependencies {
    // util
    implementation(libs.commons.lang3)
    implementation(libs.commons.text)
    implementation(libs.guava)
    implementation(libs.gson)

    // lombok
    implementation(libs.lombok)
    annotationProcessor(libs.lombok)

    // net
    implementation(libs.netty)
    implementation(libs.flow.network)

    // logging
    implementation(libs.bundles.log4j)

    // minecraft
    implementation(libs.paper)
    implementation(libs.bundles.adventure)

    // kotlin
    implementation(libs.kotlin.serialization)
    // bugfix:
    // 修复特定场景下logger无法获取，进而报错的bug
    implementation(libs.kotlin.reflection)

    implementation(project(":utils"))
}