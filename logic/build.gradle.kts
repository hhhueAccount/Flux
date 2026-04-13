plugins {
    id("buildsrc.convention.common-logic")
    kotlin("plugin.serialization")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":network"))
    implementation(project(":utils"))
    implementation(libs.jetbrains.annotation)
    implementation(libs.bundles.adventure)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.bundles.log4j)
    implementation(libs.netty.buffer)
    implementation(libs.netty.tansport)
    implementation(libs.serialization)
    implementation(libs.serialization.core)
}