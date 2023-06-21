plugins {
    buildsrc.conventions.`kotlin-jvm`
    buildsrc.conventions.publishing
}

group = "co.zsmb"
version = "1.0.0-SNAPSHOT"

kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
