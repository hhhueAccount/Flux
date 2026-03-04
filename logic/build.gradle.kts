plugins {
    id("buildsrc.convention.common-logic")
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
}