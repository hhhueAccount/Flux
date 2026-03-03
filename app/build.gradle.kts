plugins {
    id("buildsrc.convention.common-logic")

    application
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":utils"))
    implementation(project(":logic"))
    implementation(project(":network"))

    implementation(libs.bundles.adventure)
    implementation(libs.gson)
}

application {
    mainClass = "cn.zc.app.MinecraftMainKt"
}
