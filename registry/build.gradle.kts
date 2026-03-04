plugins {
    id("buildsrc.convention.common-logic")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":utils"))

    // logging
    implementation(libs.bundles.log4j)
    implementation(libs.kotlin.reflection)
}
