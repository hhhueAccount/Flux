// 此文件中的代码是一个约定插件 - 一种 Gradle 机制，用于共享可重用的构建逻辑。
// buildSrc 是受到 Gradle 认可的目录，其中的每个插件在构建的其余部分中都可以轻松使用。

package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // 默认给所有子项目都加入测试依赖
    testImplementation(kotlin("test"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // 记录所有测试结果的信息，而不仅仅是失败的测试
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}

//编码设置
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
