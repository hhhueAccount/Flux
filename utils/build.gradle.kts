plugins {
    // 应用公用的构建脚本逻辑
    // 每一个要使用公用逻辑的子项目都会加这么一行
    // 源文件在：buildSrc/src/main/kotlin/common-logic.gradle.kts
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