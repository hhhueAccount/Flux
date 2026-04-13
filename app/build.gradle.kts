plugins {
    id("buildsrc.convention.common-logic")

    application
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":logic"))
    implementation(project(":network"))
    implementation(project(":registry"))
    implementation(libs.paper)
}

application {
    mainClass = "cn.zc.MinecraftMainKt"
}
