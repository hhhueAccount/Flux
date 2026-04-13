plugins {
    id("buildsrc.convention.common-logic")
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(libs.jackson)
    implementation(libs.paper)
}