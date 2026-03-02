plugins {
    id("buildsrc.convention.common-logic")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(libs.paper)
    implementation(libs.bundles.adventure)
    implementation(libs.netty)
    implementation(libs.flow.network)
}