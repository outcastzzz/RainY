plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.rainy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rainy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val key = property("appId")?.toString() ?: error(
            "You should add apikey into gradle .properties"
        )
        buildConfigField("String", "APP_ID", "\"$key\"")

        val openWeather = property("weatherKey")?.toString() ?: error(
            "You should add apikey into gradle .properties"
        )
        buildConfigField("String", "WEATHER_KEY", "\"$openWeather\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Basic
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.androidx.navigation.compose)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.negotiation)

    // Room
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.play.services)

    // Dagger
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    // Google
    implementation(libs.play.services.location)
    implementation(libs.androidx.ui.text.google.fonts)

    // MVI
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.extensions.coroutines)
    implementation(libs.mvikotlin.extensions.reaktive)

    // Decompose
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose)
    implementation(kotlin("script-runtime"))

    // Gson
    implementation(libs.gson)

    // Lottie
    implementation(libs.lottie.compose)

}