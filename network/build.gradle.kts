plugins {
    id("buildsrc.convention.common-logic")
}

group = "cn.zc"
version = "1.0"

dependencies {
    // util
    implementation(libs.commons.lang3)
    implementation(libs.commons.text)
    implementation(libs.guava)
    implementation(libs.gson)

    // net
    implementation(libs.netty)
    implementation(libs.flow.network)

    // logging
    implementation(libs.bundles.log4j)

    // minecraft
    implementation(libs.paper)
    implementation(libs.bundles.adventure)

    // kotlin
    // bugfix:
    // 修复特定场景下logger无法获取，进而报错的bug
    implementation(libs.kotlin.reflection)

    implementation(project(":utils"))
}