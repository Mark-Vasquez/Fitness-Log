/** Top-level build file where you can add configuration options common to all sub-projects/modules.
 * This is where you declare the plugins and its version, leaving the individual app modules to use.
 */
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("androidx.room") version "2.6.1" apply false
}

// Compile time dependencies that generate code during the build, like type-safe actions
buildscript {
    dependencies {
        val navVersion = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}