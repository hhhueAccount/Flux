plugins {
    id("buildsrc.convention.common-logic")
    kotlin("plugin.serialization")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":utils"))
    implementation(libs.bundles.log4j)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.commons.collection)
    implementation(libs.commons.io)
    implementation(libs.jackson)
    implementation(libs.paper)
    implementation(libs.bundles.adventure)
    implementation(libs.serialization)
    implementation("io.leangen.geantyref:geantyref:2.0.1")
}
