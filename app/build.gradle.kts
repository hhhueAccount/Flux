plugins {
    id("buildsrc.convention.common-logic")

    application
}

group = "cn.zc"
version = "1.0"

dependencies {
    implementation(project(":logic"))
    implementation(project(":network"))
}

application {
    mainClass = "cn.zc.app.MinecraftMainKt"
}
