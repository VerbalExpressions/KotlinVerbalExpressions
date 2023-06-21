package buildsrc.conventions

import org.gradle.api.plugins.JavaBasePlugin.DOCUMENTATION_GROUP

/**
 * Conventions for publishing.
 *
 * Mostly focused on Maven Central publishing, which requires:
 *
 * * a Javadoc JAR (even if the project is not a Java project)
 * * artifacts are signed (and Gradle's [SigningPlugin] is outdated and does not have good support for lazy config/caching)
 */

plugins {
    signing
    `maven-publish`
}


//region Publication Properties
// can be set in gradle.properties or environment variables, e.g. ORG_GRADLE_PROJECT_KotlinVerbalExpressions.ossrhUsername
val ossrhUsername = providers.gradleProperty("KotlinVerbalExpressions.ossrhUsername")
val ossrhPassword = providers.gradleProperty("KotlinVerbalExpressions.ossrhPassword")

val signingKeyId: Provider<String> =
    providers.gradleProperty("KotlinVerbalExpressions.signing.keyId")
val signingKey: Provider<String> =
    providers.gradleProperty("KotlinVerbalExpressions.signing.key")
val signingPassword: Provider<String> =
    providers.gradleProperty("KotlinVerbalExpressions.signing.password")
val signingSecretKeyRingFile: Provider<String> =
    providers.gradleProperty("KotlinVerbalExpressions.signing.secretKeyRingFile")

val isReleaseVersion = provider { !version.toString().endsWith("-SNAPSHOT") }

val sonatypeReleaseUrl = isReleaseVersion.map { isRelease ->
    if (isRelease) {
        "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    } else {
        "https://oss.sonatype.org/content/repositories/snapshots/"
    }
}
//endregion


//region POM convention
publishing {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.convention("KotlinVerbalExpressions")
            description.convention("Kotlin regular expressions made easy.")
            url.convention("https://github.com/VerbalExpressions/KotlinVerbalExpressions/")

            scm {
                connection.convention("scm:git:https://github.com/VerbalExpressions/KotlinVerbalExpressions/")
                developerConnection.convention("scm:git:https://github.com/VerbalExpressions/")
                url.convention("https://github.com/VerbalExpressions/KotlinVerbalExpressions")
            }

            licenses {
                license {
                    name.convention("MIT License")
                    url.convention("https://github.com/VerbalExpressions/KotlinVerbalExpressions/blob/master/LICENSE")
                }
            }

            developers {
                developer {
                    email.set("verexpression@gmail.com")
                }
            }
        }
    }
}
//endregion


//region Maven Central publishing/signing
val javadocJarStub by tasks.registering(Jar::class) {
    group = DOCUMENTATION_GROUP
    description = "Empty Javadoc Jar (required by Maven Central)"
    archiveClassifier.set("javadoc")
}

if (ossrhUsername.isPresent && ossrhPassword.isPresent) {
    publishing {
        repositories {
            maven(sonatypeReleaseUrl) {
                name = "SonatypeRelease"
                credentials {
                    username = ossrhUsername.get()
                    password = ossrhPassword.get()
                }
            }
            // Publish to a project-local Maven directory, for verification.
            // To test, run:
            // ./gradlew publishAllPublicationsToProjectLocalRepository
            // and check $rootDir/build/maven-project-local
            maven(rootProject.layout.buildDirectory.dir("maven-project-local")) {
                name = "ProjectLocal"
            }
        }

        // Maven Central requires Javadoc JAR, which our project doesn't
        // have because it's not Java, so use an empty jar.
        publications.withType<MavenPublication>().configureEach {
            artifact(javadocJarStub)
        }
    }

    signing {
        logger.lifecycle("publishing.gradle.kts enabled signing for ${project.path}")
        if (signingKeyId.isPresent && signingKey.isPresent && signingPassword.isPresent) {
            useInMemoryPgpKeys(signingKeyId.get(), signingKey.get(), signingPassword.get())
        } else {
            useGpgCmd()
        }
    }

    afterEvaluate {
        // Register signatures in afterEvaluate, otherwise the signing plugin creates
        // the signing tasks too early, before all the publications are added.
        signing {
            sign(publishing.publications)
        }
    }
}
//endregion


//region Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
// https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()

tasks.withType<AbstractPublishToMaven>().configureEach {
    mustRunAfter(signingTasks)
}
//endregion


//region publishing logging
tasks.withType<AbstractPublishToMaven>().configureEach {
    val publicationGAV = provider { publication?.run { "$group:$artifactId:$version" } }
    doLast("log publication GAV") {
        if (publicationGAV.isPresent) {
            logger.lifecycle("[task: ${path}] ${publicationGAV.get()}")
        }
    }
}
//endregion
