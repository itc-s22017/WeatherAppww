// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    // Room データベースのプラグインで使用する
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    // Ktor 内部で使う kotlinx.serialization 用のプラグイン
    kotlin("plugin.serialization") version "1.9.0" apply false
}