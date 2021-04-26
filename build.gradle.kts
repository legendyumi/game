import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.41"
    application
}

application{
    mainClassName = "org.ym.game.AppKt"
}
//Ctrl+Shif+A  自动配置
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.shaunxiao:kotlinGameEngine:v0.0.1")
//    implementation("com.github.shaunxiao:kotlinGameEngine:v0.0.2")
}
repositories {
    mavenCentral()
    maven {
        setUrl("https://jitpack.io")
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}