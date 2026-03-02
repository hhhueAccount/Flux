plugins {
    // 应用公用的构建脚本逻辑
    // 每一个要使用公用逻辑的子项目都会加这么一行
    // 源文件在：buildSrc/src/main/kotlin/common-logic.gradle.kts
    id("buildsrc.convention.common-logic")

    application
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":utils"))

    implementation(libs.bundles.adventure)
    implementation(libs.gson)
}

application {
    mainClass = "cn.zc.app.MinecraftMainKt"
}
