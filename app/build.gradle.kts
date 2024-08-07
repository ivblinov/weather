plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.examples.weather"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.online.lite.app"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Dagger:
    implementation (libs.dagger)
    implementation(libs.androidx.monitor)
    ksp (libs.dagger.compiler)

    // Moshi:
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    ksp(libs.moshi.kotlin.codegen)

    // ViewModel:
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.fragment.ktx)

    // Retrofit:
    implementation(libs.retrofit)

    // Navigation:
    implementation (libs.navigation.fragment)
    implementation (libs.navigation.ui)

    // com.google.android.gms:
    implementation(libs.services.location)
}