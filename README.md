[![Build Status](https://travis-ci.org/zsmb13/KotlinVerbalExpressions.svg?branch=master)](https://travis-ci.org/zsmb13/KotlinVerbalExpressions)

# KotlinVerbalExpressions

This is a Kotlin implementation of [VerbalExpressions](https://github.com/VerbalExpressions),
mostly based on the Java, Swift, and Scala implementations.

### Examples

Simple URL test:

```kotlin
val verex = VerEx()
    .startOfLine()
    .then("http")
    .maybe("s")
    .then("://")
    .maybe("www")
    .anythingBut(" ")
    .endOfLine()

val url = "https://www.google.com"

// regular test with VerEx method
if (verex.test(url)) {
    println("Correct url")
}

// test with infix extension
if (url matches verex) {
    println("Correct url")
}
```

Replacing strings:

```kotlin
val str = "I like birds and bridges"

val verex = VerEx()
    .then("b")
    .anythingBut(" ").zeroOrMore()

val result = verex.replace(str, "trains")

println(result) // I like trains and trains
```

For more usage examples, see the included tests.

### Installation

##### Gradle

```kts
// build.gradle.kts

repositories {
    jcenter()
    // or, alternatively:
    maven("https://dl.bintray.com/zsmb13/KotlinVerbalExpressions/")
}

dependencies {
    implementation("co.zsmb:kotlinverbalexpressions:0.1")
}
```

##### Maven

_Note that Maven requires a `-jvm` suffix_

```xml

<dependency>
    <groupId>co.zsmb</groupId>
    <artifactId>kotlinverbalexpressions-jvm</artifactId>
    <version>0.1</version>
</dependency>
```
