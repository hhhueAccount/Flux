plugins {
    id("buildsrc.convention.common-logic")
    kotlin("plugin.serialization")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":utils"))

    // util
    implementation(libs.commons.lang3)
    implementation(libs.commons.text)
    implementation(libs.commons.collection)
    implementation(libs.guava)
    implementation(libs.gson)
    implementation(libs.jetbrains.annotation)
    implementation(libs.serialization)
    implementation(libs.serialization.core)

    // net
    implementation(libs.netty.buffer)
    implementation(libs.netty.tansport)
    implementation(libs.flow.network)

    // logging
    implementation(libs.bundles.log4j)

    // minecraft
    implementation(libs.paper) // 包含几乎所有adventure lib依赖
    implementation(libs.adventure.nbt)
}